/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.server.services;

import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.competencia.Competencia;
import com.tdk.domain.torneo.competencia.EstadoCompetencia;
import com.tdk.domain.torneo.competencia.Llave;
import com.tdk.domain.torneo.competencia.LlaveForma;
import com.tdk.domain.torneo.competencia.LlaveLucha;
import com.tdk.domain.torneo.competencia.TipoEstadoCompetencia;
import com.tdk.services.CompetenciaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.services.UtilServiceRemote;
import com.tdk.utils.CalcBitInverso;
import com.tdk.utils.TDKServerException;
import com.thorplatform.jpa.JPAService;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

/**
 *
 * @author fernando
 */
public class CompetenciaServiceBean extends JPAService implements CompetenciaServiceRemote {

    private TorneoServiceRemote torneoService;
    private UtilServiceRemote utilService;
    private static long counter = 0;

    public Llave crearLlave(Llave llave) {
        Llave other = validarLlave(llave);
        if (other != null) {
            throw new TDKServerException("La llave ya existe");
        }
        getEntityManager().persist(llave);
        return llave;
    }

    public void modificarLlave(Llave llave) {
        Llave other = validarLlave(llave);
        if (other != null && !other.equals(llave)) {
            throw new TDKServerException("La llave ya existe");
        }
        getEntityManager().merge(llave);
    }

    public void eliminarLlave(Long idLlave) {
        Llave llave = getEntityManager().find(Llave.class, idLlave);
        Vector<Competencia> vector = new Vector<Competencia>();

        //elimino las relaciones de las competencias y las guardo
        //en un vector.
        eliminarCompetencia(llave.getCompetencia(), vector);

        getEntityManager().remove(llave);

        for (Competencia c : vector) {
            getEntityManager().remove(c);
        }


    }

    private void eliminarCompetencia(Competencia c, Vector<Competencia> v) {
        if (c != null) {

            eliminarCompetencia(c.getCompentenciaLeft(), v);
            eliminarCompetencia(c.getCompentenciaRight(), v);

            //rompo las relaciones
            c.setCompentenciaLeft(null);
            c.setCompentenciaRight(null);
            c.setCompetenciaPadre(null);

            //actualizo sus dependencias
            getEntityManager().merge(c);

            //agrego al vector para su posterior eliminacion
            v.add(c);
        }
    }

    public List<Llave> listarLlave(String patron) {
        if (patron == null) {
            throw new TDKServerException("El tipo de busqueda no puede ser nulo.");
        }
        List<Llave> llaves = new ArrayList<Llave>();
        List<LlaveForma> llaveForma = listarLlaveFormas(patron);
        List<LlaveLucha> llaveLucha = listarLlaveLucha(patron);

        llaves.addAll(llaveForma);
        llaves.addAll(llaveLucha);

        return llaves;

    }

    public List<LlaveLucha> listarLlaveLucha(String patron) {
        if (patron == null) {
            throw new TDKServerException("El tipo de busqueda no puede ser nulo.");
        }
        Query query = getEntityManager().createQuery("Select l from LlaveLucha l " +
                "where lower(l.cinturon.descripcion) like :patron OR" +
                " lower(l.categoria.descripcion) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public List<LlaveForma> listarLlaveFormas(String patron) {
        if (patron == null) {
            throw new TDKServerException("El tipo de busqueda no puede ser nulo.");
        }
        Query query = getEntityManager().createQuery("Select l from LlaveForma l " +
                "where lower(l.cinturon.descripcion) like :patron OR" +
                " lower(l.categoria.descripcion) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public void modificarCompetencia(Competencia competencia) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /////////////Helpers/////////////
    private Llave validarLlave(Llave llave) {
        Llave other = null;

        String sqlL = "Select l From LlaveLucha l where l.categoriaLucha.id = :idCategoria " +
                "l.cinturon.id = :idCinturon";

        String sqlF = "Select l From LlaveForma l where l.categoriaForma.id = :idCategoria " +
                "l.cinturon.id = :idCinturon";

        boolean isLucha = (llave instanceof LlaveLucha);

        Query query = getEntityManager().createQuery(isLucha ? sqlL : sqlF);
        query.setParameter("idCategoria", llave.getCategoria().getId());
        query.setParameter("idCinturon", llave.getCinturon().getId());


        try {
            other = (Llave) query.getSingleResult();
        } catch (Throwable ex) {
        }

        return other;
    }
    
     public Llave crearLlave(Llave llave, List<Competidor> competidores) {

        if (competidores == null || competidores.isEmpty()) {
            throw new TDKServerException("No hay competidores para esta llave");
        }
    
        return convertListTollave(competidores, llave);
    }
    
     public LlaveLucha crearLlaveLucha(Cinturon cinturon, CategoriaLucha categoria, Torneo torneo) {
        List<Competidor> competidores = getTorneoService().listarCompetidoresLucha(cinturon.getId(), categoria.getId(), torneo.getId());

        if (competidores == null || competidores.isEmpty()) {
            throw new TDKServerException("No hay competidores para esta llave");
        }
        LlaveLucha llave = new LlaveLucha();
        llave.setCinturon(cinturon);
        llave.setCategoria(categoria);
        llave.setTorneo(torneo);

        competidores = CalcBitInverso.orderList(competidores);

        return (LlaveLucha) convertListTollave(competidores, llave);
    }

    public LlaveForma crearLlaveForma(Cinturon cinturon, CategoriaForma categoria, Torneo torneo) {
        List<Competidor> competidores = getTorneoService().listarCompetidoresForma(cinturon.getId(), categoria.getId(), torneo.getId());

        if (competidores == null || competidores.isEmpty()) {
            throw new TDKServerException("No hay competidores para esta llave");
        }
        LlaveForma llave = new LlaveForma();
        llave.setCinturon(cinturon);
        llave.setCategoria(categoria);
        llave.setTorneo(torneo);

        competidores = CalcBitInverso.orderList(competidores);

        return (LlaveForma) convertListTollave(competidores, llave);
    }

    private Llave convertListTollave(List<Competidor> list, Llave llave) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("La lista es nula");
        }

        Competencia c = llave.getCompetencia();
        if (c == null) {
            c = crearCompetencia(new Competencia());

            c = insertNodeFromList(list, c);

            llave.setCompetencia(c);
        }

        getEntityManager().persist(llave);

        return llave;
    }

    private Competencia insertNodeFromList(List<Competidor> list, Competencia dad) {
        if (dad == null) {
            dad = crearCompetencia(new Competencia());
        }

        if (list.size() == 2) {
            dad.setCompetidorAzul(list.get(0));
            dad.setCompetidorRojo(list.get(1));
        } else if (list.size() == 3) {
            dad.setCompetidorRojo(list.get(0));
            list.remove(0);
            dad.setCompentenciaLeft(insertNodeFromList(list, dad.getCompentenciaLeft()));
        } else {
            int x1 = list.size() % 2 == 0 ? list.size() / 2 : list.size() + 1;
            int x2 = list.size() ;

            dad.setCompentenciaLeft(insertNodeFromList(subList(list, 0, x1), dad.getCompentenciaLeft()));
            dad.setCompentenciaRight(insertNodeFromList(subList(list, x1, x2), dad.getCompentenciaRight()));
        }

        return crearCompetencia(dad);
    }
    
    private List subList(List list, int ini, int fin) {
        List newList = new ArrayList();
        int i = ini;
        for( ; i < fin; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

    private Competencia crearCompetencia(Competencia c) {
        if (c == null) {
            return null;
        }
        if (c.getNumero() == null) {
            Long n = getUtilService().getNuevoNumero(Competencia.class);
            c.setNumero(n);
        }

        boolean enCondiciones = c.getCompetidorAzul() != null && c.getCompetidorRojo() != null;
        
        if (c.getId() == null) {
            c.setEstadoActual(crearEstadoCompetencia(enCondiciones ? TipoEstadoCompetencia.EN_CONDICIONES : TipoEstadoCompetencia.PENDIENTE));
            getEntityManager().persist(c);
        } else {
            if (enCondiciones && !c.getEstadoActual().getTipoEstado().equals(TipoEstadoCompetencia.EN_CONDICIONES)) {
                actualizarEstadoCompetencia(TipoEstadoCompetencia.EN_CONDICIONES, c);
            }
            getEntityManager().merge(c);
        }

        return c;
    }

    private TorneoServiceRemote getTorneoService() {
        if (torneoService == null) {
            TorneoServiceBean ts = new TorneoServiceBean();
            ts.setContext(this);
            torneoService = ts;
        }
        return torneoService;
    }

    private UtilServiceRemote getUtilService() {
        if (utilService == null) {
            UtilServiceBean ut = new UtilServiceBean();
            ut.setContext(this);
            utilService = ut;
        }
        return utilService;
    }

    private EstadoCompetencia crearEstadoCompetencia(TipoEstadoCompetencia tipoEstado) {
        EstadoCompetencia estado = new EstadoCompetencia();
        estado.setFechaDesde(getUtilService().getDiaHora());
        estado.setTipoEstado(tipoEstado);
        getEntityManager().persist(estado);
        return estado;
    }
    
    
    private void actualizarEstadoCompetencia(TipoEstadoCompetencia tipoEstado, Competencia competencia) {
        EstadoCompetencia estadoOld = competencia.getEstadoActual();
        estadoOld.setCompetencia(competencia);
        
        if (competencia.getEstados() == null)
            competencia.setEstados(new ArrayList<EstadoCompetencia>());
        
        competencia.getEstados().add(estadoOld);

        //Inserto el nuevo estado
        EstadoCompetencia estadoNew = crearEstadoCompetencia(tipoEstado);
        competencia.setEstadoActual(estadoNew);
    }

    public void finalizarCompentecia(Competencia competencia) {
        
        if (competencia.getCompetidorGanador() == null) {
            throw new TDKServerException("Seleccione un ganador para finalizar la competencia.");
        }
        
        if (competencia.getEstadoActual().getTipoEstado().equals(TipoEstadoCompetencia.FINALIZADA)) {
            throw new TDKServerException("La competencia ya se encuentra finalizada.");        //Actualizo el estado actual y lo paso al historial
        }
        
        //actualizo la competencia con su nuevo estado
        actualizarEstadoCompetencia(TipoEstadoCompetencia.FINALIZADA, competencia);
        
        //Tengo que trasladar el ganador a la prox. competencia
        //Obtengo la prox. competencia
        Competencia nextCompetencia = competencia.getCompetenciaPadre();
        
        if (nextCompetencia != null) {
        
            Competidor ganador = competencia.getCompetidorGanador();
            
            if (nextCompetencia.getCompentenciaLeft().equals(competencia)) {
                nextCompetencia.setCompetidorAzul(ganador);
            } else if (nextCompetencia.getCompentenciaRight().equals(competencia)) {
                nextCompetencia.setCompetidorRojo(ganador);
            }
            
            boolean enCondiciones = nextCompetencia.getCompetidorAzul() != null && 
                    nextCompetencia.getCompetidorRojo() != null;
            
            if (enCondiciones) {
                actualizarEstadoCompetencia(TipoEstadoCompetencia.EN_CONDICIONES, nextCompetencia);
            }
            
            getEntityManager().merge(nextCompetencia);
            
        }

        getEntityManager().merge(competencia);
    }

    public Competencia recuperarCompetencia(Long idCompetencia, boolean completa) {
        Competencia c = getEntityManager().find(Competencia.class, idCompetencia);
        if (completa) {
            c.getEstados().size();
        }
        return c;
    }
}
