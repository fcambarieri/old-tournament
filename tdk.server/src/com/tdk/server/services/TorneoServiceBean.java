/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.server.services;

import com.tdk.domain.Institucion;
import com.tdk.domain.Sexo;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.CompetidorCategoria;
import com.tdk.domain.torneo.EstadoCompetidor;
import com.tdk.domain.torneo.EstadoTorneo;
import com.tdk.domain.torneo.InstitucionCompetidorDTO;
import com.tdk.domain.torneo.TipoEstadoCompetidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.services.UtilServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.jpa.IsTransactional;
import com.thorplatform.jpa.JPAService;
import com.thorplatform.jpa.JPATransactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author fernando
 */
public class TorneoServiceBean extends JPAService implements TorneoServiceRemote {

    private UtilServiceRemote utilService;
    private static final int MAX_RESULT = 100;

    public Torneo crearTorneo(Torneo torneo) {
        Torneo other = recuperarTorneoPorDescripcion(torneo.getNombre());
        if (other != null) {
            throw new TDKServerException("Ya existe un torneo con el nombre " + torneo.getNombre());
        }
        getEntityManager().persist(torneo);
        return torneo;
    }

    public void modificarTorneo(Torneo torneo) {
        Torneo other = recuperarTorneoPorDescripcion(torneo.getNombre());
        if (other != null && !other.equals(torneo)) {
            throw new TDKServerException("Ya existe un torneo con el nombre " + torneo.getNombre());
        }
        verificarCambioEstadoTorneo(torneo);
        getEntityManager().merge(torneo);
    }

    private void verificarCambioEstadoTorneo(Torneo torneo) {
        Torneo attached = getEntityManager().find(Torneo.class, torneo.getId());
        if (!attached.getEstadoTorneo().getTipoEstadoTorneo().equals(torneo.getEstadoTorneo().getTipoEstadoTorneo())) {
            utilService = getUtilService();
            EstadoTorneo estado = new EstadoTorneo();
            estado.setFechaDesde(utilService.getDiaHora());
            estado.setTipoEstadoTorneo(torneo.getEstadoTorneo().getTipoEstadoTorneo());
            torneo.setEstadoTorneo(estado);

            //paso el viejo estado a la lista de estados
            EstadoTorneo estadoViejo = attached.getEstadoTorneo();
            estadoViejo.setTorneo(torneo);
            torneo.getHistorialEstados().add(estadoViejo);
        }
    }

    public List<Torneo> listarTorneos(String patron) {
        Query query = getEntityManager().createQuery("Select t from Torneo t " +
                "where lower(t.nombre) like :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public Torneo recuperarTorneo(Long idTorneo, boolean completo) {
        Torneo torneo = getEntityManager().find(Torneo.class, idTorneo);
        if (completo) {
            torneo.getHistorialEstados().size();
        }
        return torneo;
    }

    public Torneo recuperarTorneoVigente(Date fechaDesde) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Torneo recuperarTorneoPorDescripcion(String nombre) {
        Torneo torneo = null;
        Query query = getEntityManager().createQuery("Select t from Torneo t " +
                "where lower(t.nombre) = :param");
        query.setParameter("param", nombre.trim().toLowerCase());
        try {
            torneo = (Torneo) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de un torneo con el nombre " + nombre);
        } catch (NoResultException ex) {
            return null;
        }

        return torneo;
    }

    public Cinturon crearCinturon(Cinturon cinturon) {
        Cinturon other = recuperarCinturonPorDescripcion(cinturon.getDescripcion());
        if (other != null) {
            throw new TDKServerException("ya existe un cinturon con la descripción " + cinturon.getDescripcion());
        }
        getEntityManager().persist(cinturon);

        return cinturon;
    }

    public void modificarCinturon(Cinturon cinturon) {
        Cinturon other = recuperarCinturonPorDescripcion(cinturon.getDescripcion());
        if (other != null && !other.equals(cinturon)) {
            throw new TDKServerException("ya existe un cinturon con la descripción " + cinturon.getDescripcion());
        }
        getEntityManager().merge(cinturon);
    }

    public Cinturon recuperarCinturon(Long idCinturon) {
        return getEntityManager().find(Cinturon.class, idCinturon);
    }

    public Cinturon recuperarCinturonPorDescripcion(String descripcion) {
        Cinturon cinturon = null;
        Query query = getEntityManager().createQuery("Select c from Cinturon c " +
                "where lower(c.descripcion) = :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + descripcion.trim().toLowerCase() + STARTING_WITH_WILDCARD);
        try {
            cinturon = (Cinturon) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de un cinturon con la descripción " + descripcion);
        } catch (NoResultException ex) {
            return null;
        }

        return cinturon;
    }

    public List<Cinturon> listarCinturones(String patron) {
        Query query = getEntityManager().createQuery("Select count(c) from Cinturon c " +
                "where lower(c.descripcion) like :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();

        if (count == 0) {
            throw new TDKServerException("No hay resultados. Intente con otro filtro");
        }
        query = getEntityManager().createQuery("Select c from Cinturon c " +
                "where lower(c.descripcion) like :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);

        return query.getResultList();
    }

    public CategoriaForma crearCategoriaForma(CategoriaForma categoriaForma) {
        Categoria other = recuperarCategoriaPorDescripcion(categoriaForma.getDescripcion(), CategoriaForma.class);
        if (other != null) {
            throw new TDKServerException("Ya existe la categoria con la descripción " + categoriaForma.getDescripcion());
        }
        if (!validarEdadCategoriaForma(categoriaForma)) {
            throw new TDKServerException("Ya existe la categoria con las edades en los rango " + categoriaForma.getEdadInferior() + " a " + categoriaForma.getEdadSuperior());
        }
        getEntityManager().persist(categoriaForma);
        return categoriaForma;
    }

    public void modificarCategoriaForma(CategoriaForma categoriaForma) {
        Categoria other = recuperarCategoriaPorDescripcion(categoriaForma.getDescripcion(), CategoriaForma.class);
        if (other != null && !other.equals(categoriaForma)) {
            throw new TDKServerException("Ya existe la categoria con la descripción " + categoriaForma.getDescripcion());
        }

        if (!validarEdadCategoriaForma(categoriaForma)) {
            throw new TDKServerException("Ya existe la categoria con las edades " +
                    categoriaForma.getEdadInferior() + " a " + categoriaForma.getEdadSuperior());
        }

        getEntityManager().merge(categoriaForma);
    }

    public List<CategoriaForma> listarCategoriasForma(String descripcion) {
        Query query = getEntityManager().createQuery("Select c from CategoriaForma c " +
                "where lower(c.descripcion) like :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + descripcion.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public Categoria recuperarCategoriaPorDescripcion(String descripcion, Class<? extends Categoria> claseName) {
        Categoria categoria = null;
        Query query = getEntityManager().createQuery("Select c from " + claseName.getSimpleName() + " c " +
                "where lower(c.descripcion) = :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + descripcion.trim().toLowerCase() + STARTING_WITH_WILDCARD);
        try {
            categoria = (Categoria) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de un cinturon con la descripción " + descripcion);
        } catch (NoResultException ex) {
            return null;
        }
        return categoria;
    }

    public void eliminarCinturon(Long idCinturon) {
        Cinturon cinturon = getEntityManager().find(Cinturon.class, idCinturon);
        getEntityManager().remove(cinturon);
    }

    public void eliminarCategoria(Long idCategoria) {
        Categoria categoria = getEntityManager().find(Categoria.class, idCategoria);
        getEntityManager().remove(categoria);
    }

    public CategoriaLucha crearCategoriaLuchas(CategoriaLucha categoriaLucha) {
        Categoria other = recuperarCategoriaPorDescripcion(categoriaLucha.getDescripcion(), CategoriaLucha.class);
        if (other != null) {
            throw new TDKServerException("Ya existe una categoría con la descripción " + categoriaLucha.getDescripcion());
        }

        //other = recuperarCategoriaLuchaPorSexoEdad(categoriaLucha.getEdadInferior(), categoriaLucha.getEdadSuperior(), categoriaLucha.getSexo());
        if (!validarCategoriaLucha(categoriaLucha)) {
            throw new TDKServerException("Ya existe una categoría con las edades " + categoriaLucha.getEdadInferior() + " - " + categoriaLucha.getEdadSuperior() + " para el sexo " + categoriaLucha.getSexo());
        }
        getEntityManager().persist(categoriaLucha);

        return categoriaLucha;
    }

    public void modificarCategoriaLucha(CategoriaLucha categoriaLucha) {
        Categoria other = recuperarCategoriaPorDescripcion(categoriaLucha.getDescripcion(), CategoriaLucha.class);
        if (other != null && !other.equals(categoriaLucha)) {
            throw new TDKServerException("Ya existe una categoría con la descripción " + categoriaLucha.getDescripcion());
        }

        if (!validarCategoriaLucha(categoriaLucha)) {
            throw new TDKServerException("Ya existe una categoría con las edades " + categoriaLucha.getEdadInferior() + " - " + categoriaLucha.getEdadSuperior() + " para el sexo " + categoriaLucha.getSexo());
        }
        CategoriaLucha attached = getEntityManager().find(CategoriaLucha.class, categoriaLucha.getId());
        updateList(attached.getPesos(), categoriaLucha.getPesos());

        getEntityManager().merge(categoriaLucha);
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<CategoriaLucha> listarCategoriasLucha(String descripcion) {
        Query query = getEntityManager().createQuery("Select c from CategoriaLucha c " +
                "where lower(c.descripcion) like :param");
        query.setParameter("param", STARTING_WITH_WILDCARD + descripcion.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public UtilServiceRemote getUtilService() {
        UtilServiceBean utilServiceBean = new UtilServiceBean();
        utilServiceBean.setContext(this);
        return utilServiceBean;
    }

    public Competidor crearCompetidor(Competidor competidor) {
        if (!validarCompetidor(competidor)) {
            throw new TDKServerException("Ya existe un competidor con los datos ingresados");
        }
        getEntityManager().persist(competidor);
        return competidor;
    }

    public void modificarCompetidor(Competidor competidor) {
        Competidor other = recuperarCompetidor(competidor);
        if (other != null && !other.equals(competidor)) {
            throw new TDKServerException("Ya existe un competidor con los datos ingresados");
        }

        other = getEntityManager().find(Competidor.class, competidor.getId());

        changeEstadoCompetidor(other.getCompetidorCategoriaForma(), competidor.getCompetidorCategoriaForma());
        changeEstadoCompetidor(other.getCompetidorCategoriaLucha(), competidor.getCompetidorCategoriaLucha());
        getEntityManager().merge(competidor);
    }

    public Competidor recuperarCompetidor(Long idCompetidor, boolean completo) {
        Competidor c = getEntityManager().find(Competidor.class, idCompetidor);
        if (completo) {
            c.getAlumno().getPersonaFisica().getContactos().size();
            c.getTorneo().getHistorialEstados().size();
            if (c.getCompetidorCategoriaForma() != null) {
                c.getCompetidorCategoriaForma().getEstados().size();
            }
            if (c.getCompetidorCategoriaLucha() != null) {
                c.getCompetidorCategoriaLucha().getEstados().size();
            }
        }
        return c;
    }

    public List<Competidor> listarCompetidores(String patron) {
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(distinct c) from KeywordPersona kp , Competidor c " +
                "where lower(kp.keyword) like :patron AND kp.persona.id = c.alumno.personaFisica.id");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Demacioados resultados, filtre con más datos</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>No hay resultados</HTML>");
        }

        query = getEntityManager().createQuery("Select distinct c From KeywordPersona kp , Competidor c" +
                "where lower(kp.keyword) like :patron AND kp.persona.id = c.alumno.personaFisica.id");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public Competidor recuperarCompetidor(Competidor c) {
        Query query = getEntityManager().createQuery("Select c From Alumno al, Competidor c " +
                "where c.alumno.id = al.id AND al.id = :idAlumno AND c.torneo.id = :idTorneo");
        query.setParameter("idAlumno", c.getAlumno().getId());
        query.setParameter("idTorneo", c.getTorneo().getId());

        Competidor competidor = null;
        try {
            competidor = (Competidor) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe mas de un competidor llamado " + c.getAlumno().getPersonaFisica().getDisplayName());
        } catch (NoResultException ex) {
        }

        return competidor;
    }

    public boolean validarCompetidor(Competidor c) {
        Query query = getEntityManager().createQuery("Select c From Alumno al, Competidor c " +
                "where c.alumno.id = al.id AND al.id = :idAlumno AND c.torneo.id = :idTorneo");
        query.setParameter("idAlumno", c.getAlumno().getId());
        query.setParameter("idTorneo", c.getTorneo().getId());
        try {
            Competidor competidor = (Competidor) query.getSingleResult();
            return c.equals(competidor);
        } catch (NonUniqueResultException ex) {
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }

    public List<Competidor> listarCompetidoresPorTorneo(Long idTorneo, String patron) {

        if (idTorneo == null) {
            throw new TDKServerException("Seleccione un torneo");
        }
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }

        Query query = getEntityManager().createQuery("select distinct c from KeywordPersona kp , Competidor c " +
                "where lower(kp.keyword) like :patron AND kp.persona.id = c.alumno.personaFisica.id " +
                "and c.torneo.id = :idTorneo");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        query.setParameter("idTorneo", idTorneo);
        return query.getResultList();
    }

    public List<Competidor> listarCompetidoresPorTorneoEInstitucion(Long idTorneo, Long idInstitucion, String patron) {
        if (idTorneo == null) {
            throw new TDKServerException("Seleccione un torneo");
        }
        if (idTorneo == null) {
            throw new TDKServerException("Seleccione una institución");
        }
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }

        Query query = getEntityManager().createQuery("select distinct c from KeywordPersona kp , Competidor c  " +
                "where lower(kp.keyword) like :patron AND kp.persona.id = c.alumno.personaFisica.id " +
                "and c.torneo.id = :idTorneo " +
                "and c.alumno.institucion.id = :idInstitucion");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        query.setParameter("idTorneo", idTorneo);
        query.setParameter("idInstitucion", idInstitucion);
        return query.getResultList();
    }

    public List<InstitucionCompetidorDTO> listarInstitucionCompetidorDTOPorTorneo(Long idTorneo, String patron) {

        if (idTorneo == null) {
            throw new TDKServerException("Seleccione un torneo");
        }
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        List<InstitucionCompetidorDTO> list = new ArrayList<InstitucionCompetidorDTO>();

        Query query = getEntityManager().createQuery("select distinct ti.institucion From TorneoInstitucion ti, Competidor c " +
                " where ti.torneo.id = :idTorneo and ti.institucion.id = c.alumno.institucion.id");
        query.setParameter("idTorneo", idTorneo);
        List<Institucion> institucionList = query.getResultList();

        if (institucionList.size() == 0) {
            throw new TDKServerException("No hay resultados. Ingrese mas filtros");
        }
        for (int i = 0; i < institucionList.size(); i++) {
            Institucion it = institucionList.get(i);
            List<Competidor> competidores = listarCompetidoresPorTorneoEInstitucion(idTorneo, it.getId(), patron);
            for (Competidor c : competidores) {
                if (c.getCompetidorCategoriaForma() != null) {
                    c.getCompetidorCategoriaForma().getEstados().size();
                    System.out.println(c.getAlumno().getPersonaFisica().getDisplayName() + " " + c.getCompetidorCategoriaForma().getEstados().size());
                }

                if (c.getCompetidorCategoriaLucha() != null) {
                    c.getCompetidorCategoriaLucha().getEstados().size();
                    System.out.println(c.getAlumno().getPersonaFisica().getDisplayName() + " " + c.getCompetidorCategoriaLucha().getEstados().size());
                }
            }
            list.add(new InstitucionCompetidorDTO(it, competidores));

        }

        return list;
    }

    public void eliminarInstitucionDelTorneo(Long idTorneo, Long idInstitucion) {

        List<Competidor> competidores = listarCompetidoresPorTorneoEInstitucion(idTorneo, idInstitucion, STARTING_WITH_WILDCARD);

        for (Competidor c : competidores) {
            getEntityManager().remove(c);
        }

        Query query = getEntityManager().createQuery("Delete from TorneoInstitucion ti " +
                "where ti.torneo.id = :idTorneo AND ti.institucion.id = :idInstitucion");
        query.setParameter("idTorneo", idTorneo);
        query.setParameter("idInstitucion", idInstitucion);
        query.executeUpdate();
    }

    public void eliminarCompetidor(Long idCompetidor) {
        Competidor competidor = getEntityManager().find(Competidor.class, idCompetidor);
        getEntityManager().remove(competidor);
    }

    private EstadoCompetidor crearEstadoCompetidor(TipoEstadoCompetidor tipoEstadoCompetidor) {
        EstadoCompetidor estado = new EstadoCompetidor();
        estado.setFechaDesde(getUtilService().getDiaHora());
        estado.setTipoEstado(tipoEstadoCompetidor);
        getEntityManager().persist(estado);
        return estado;
    }

    private EstadoCompetidor crearEstadoCompetidor(TipoEstadoCompetidor tipoEstadoCompetidor, CompetidorCategoria c) {
        EstadoCompetidor estado = new EstadoCompetidor();
        estado.setFechaDesde(getUtilService().getDiaHora());
        estado.setTipoEstado(tipoEstadoCompetidor);
        estado.setCompetidorCategoria(c);
        getEntityManager().persist(estado);
        return estado;
    }

    private void changeEstadoCompetidor(CompetidorCategoria attached, CompetidorCategoria dettached) {
        if (attached != null && dettached != null) {
            if (!attached.getEstadoCompetidor().getTipoEstado().equals(dettached.getEstadoCompetidor().getTipoEstado())) {
                EstadoCompetidor newEstado = crearEstadoCompetidor(dettached.getEstadoCompetidor().getTipoEstado());
                dettached.setEstadoCompetidor(newEstado);

                EstadoCompetidor oldEstado = attached.getEstadoCompetidor();
                oldEstado.setCompetidorCategoria(dettached);
                dettached.getEstados().add(oldEstado);
            }
        }
    }

    public CategoriaLucha recuperarCategoriaLuchaPorSexoEdad(Integer edadInferior, Integer edadSuperior, Sexo sexo) {

        if (edadInferior == null || edadSuperior == null) {
            throw new TDKServerException("Las edades no pueden ser vacias");
        }
        if (edadInferior >= edadSuperior) {
            throw new TDKServerException("Las edad inferior no puede ser mayor a la superior");
        /*throw new TDKServerException("Existe mas de una categoria para los valores: " +
        "<BR> Sexo: " + sexo +"<BR> Edad desde " + edadInferior + " hasta " + edadSuperior);
         * */
        }
        return null;
    }

    private boolean validarCategoriaLucha(CategoriaLucha categoriaLucha) {

        Integer edadInferior = categoriaLucha.getEdadInferior();

        Integer edadSuperior = categoriaLucha.getEdadSuperior();

        Sexo sexo = categoriaLucha.getSexo();

        if (edadInferior == null || edadSuperior == null) {
            throw new TDKServerException("Las edades no pueden ser vacias");
        }
        if (edadInferior >= edadSuperior) {
            throw new TDKServerException("Las edad inferior no puede ser mayor a la superior");
        }

        Query query = getEntityManager().createQuery("Select c From CategoriaLucha c " +
                "where c.sexo = :sexo and c.id <> :idCategoria " +
                "Order by c.edadInferior");
        query.setParameter("sexo", sexo);
        query.setParameter("idCategoria", categoriaLucha.getId() != null ? categoriaLucha.getId() : new Long("-1"));
        List<CategoriaLucha> categorias = query.getResultList();

        if (categorias.size() == 0) {
            return true;
        }

        boolean validar = true;

        for (CategoriaLucha c : categorias) {
            boolean v1 = edadInferior.compareTo(c.getEdadInferior()) <= 0 && edadSuperior.compareTo(c.getEdadInferior()) >= 0;
            boolean v2 = edadInferior.compareTo(c.getEdadInferior()) >= 0 && edadInferior.compareTo(c.getEdadSuperior()) <= 0;
            if (v1 || v2) {
                validar = false;
                break;
            }

        }

        return validar;
    }

    private boolean validarEdadCategoriaForma(CategoriaForma categoria) {
        Integer edadInferior = categoria.getEdadInferior();

        Integer edadSuperior = categoria.getEdadSuperior();

        if (edadInferior == null || edadSuperior == null) {
            throw new TDKServerException("Las edades no pueden ser vacias");
        }

        if (edadInferior >= edadSuperior) {
            throw new TDKServerException("Las edad inferior no puede ser mayor a la superior");
        }

        Query query = getEntityManager().createQuery("Select c From CategoriaForma c where c.id <> :idCategoria Order by c.edadInferior");
        query.setParameter("idCategoria", categoria.getId() != null ? categoria.getId() : new Long("0"));
        List<CategoriaForma> categorias = query.getResultList();

        if (categorias.isEmpty()) {
            return true;
        }

        boolean validar = true;

        for (CategoriaForma c : categorias) {
            boolean v1 = edadInferior.compareTo(c.getEdadInferior()) <= 0 && edadSuperior.compareTo(c.getEdadInferior()) >= 0;
            boolean v2 = edadInferior.compareTo(c.getEdadInferior()) >= 0 && edadInferior.compareTo(c.getEdadSuperior()) <= 0;
            if (v1 || v2) {
                validar = false;
                break;
            }

        }

        return validar;

    }

    public List<Competidor> listarCompetidoresLucha(Long idCinturon, Long idCategoria, Long idTorneo) {
        Query query = getEntityManager().createQuery("select c from Competidor c " +
                "where c.cinturon.id = :idCinturon and c.torneo.id = :idTorneo and " +
                "c.competidorCategoriaLucha.peso.categoriaLucha.id = :idCategoria " +
                "order by c.alumno.institucion");
        query.setParameter("idCinturon", idCinturon);
        query.setParameter("idCategoria", idCategoria);
        query.setParameter("idTorneo", idTorneo);
        return query.getResultList();
    }

    public List<Competidor> listarCompetidoresForma(Long idCinturon, Long idCategoria, Long idTorneo) {
        Query query = getEntityManager().createQuery("select c from Competidor c " +
                "where c.cinturon.id = :idCinturon and c.torneo.id = :idTorneo and " +
                "c.competidorCategoriaForma.categoriaForma.id = :idCategoria " +
                "order by c.alumno.institucion");
        query.setParameter("idCinturon", idCinturon);
        query.setParameter("idCategoria", idCategoria);
        query.setParameter("idTorneo", idTorneo);
        return query.getResultList();
    }

    public List<TorneoInstitucion> listarTorneosPorInstitucion(Long idInstitucion) {
        if (idInstitucion == null) {
            throw new IllegalArgumentException("Seleccione una institución.");
        }

        Query query = getEntityManager().createQuery("select ti from TorneoInstitucion ti " +
                "where ti.institucion.id = :idInstitucion");
        query.setParameter("idInstitucion", idInstitucion);
        return query.getResultList();
    }

    public List<CategoriaLucha> listarCategoriasLuchasPorSexoYEdad(Sexo sexo, Integer edad) {

        if (sexo == null)
            throw new TDKServerException("Seleccione un sexo.");

        if (edad == null)
            throw new TDKServerException("Seleccione una edad.");


        Query query = getEntityManager().createQuery("select c from CategoriaLucha c " +
                "where c.sexo = :sexo and c.edadInferior <= :edad and c.edadSuperior >= :edad ");
        query.setParameter("sexo", sexo);
        query.setParameter("edad", edad);
        return query.getResultList();
    }

    public List<CategoriaForma> listarCategoriasFormaPorEdad(Integer edad) {

        if (edad == null)
            throw new TDKServerException("Seleccione una edad.");

        Query query = getEntityManager().createQuery("select c from CategoriaForma c " +
                "where c.edadInferior <= :edad and c.edadSuperior >= :edad ");
        query.setParameter("edad", edad);
        return query.getResultList();
    }
    
    
}
