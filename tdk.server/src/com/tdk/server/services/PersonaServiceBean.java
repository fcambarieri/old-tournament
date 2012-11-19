/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.server.services;

import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.KeywordPersona;
import com.tdk.domain.Persona;
import com.tdk.domain.PersonaFisica;
import com.tdk.domain.TipoContacto;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.jpa.IsTransactional;
import com.thorplatform.jpa.JPAService;
import com.thorplatform.jpa.JPATransactional;
import com.thorplatform.notifier.NotifierEvent;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author fernando
 */
public class PersonaServiceBean extends JPAService implements PersonaServiceRemote {

    private static final int MAX_RESULT = 100;

    private void registerKeywordPersona(Persona p) {
        List<String> keys = p.getKeywords();
        for (String key : keys) {
            KeywordPersona kp = new KeywordPersona();
            kp.setKeyword(key);
            kp.setPersona(p);
            getEntityManager().persist(kp);
        }
    }

    public void anRegisterKeywordPersona(Long idPersona) {
        Query query = getEntityManager().createQuery("delete from KeywordPersona kp " +
                "where kp.persona.id = :idPersona");
        query.setParameter("idPersona", idPersona);
        query.executeUpdate();
    }

    public Persona crearPersona(Persona persona) {
        getEntityManager().persist(persona);
        registerKeywordPersona(persona);
        getNotifier().mark(Persona.class, NotifierEvent.ADD, persona.getId());
        return persona;
    }

    public Persona recuperarPersona(Long id, boolean completa) {
        Persona persona = getEntityManager().find(Persona.class, id);
        if (completa) {
            persona.getContactos().size();
        }
        return persona;
    }

    public void modificarPersona(Persona persona) {
        Persona attached = getEntityManager().find(Persona.class, persona.getId());

        updateList(attached.getContactos(), persona.getContactos());

        getEntityManager().merge(persona);
        anRegisterKeywordPersona(persona.getId());
        registerKeywordPersona(persona);
        getNotifier().mark(Persona.class, NotifierEvent.UDPDATE, persona);
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<Persona> listarPersona(String patron) {
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(distinct kp.persona) from KeywordPersona kp " +
                "where lower(kp.keyword) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Demacioados resultados, filtre con más datos</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>No hay resultados</HTML>");
        }

        query = getEntityManager().createQuery("Select distinct kp.persona From KeywordPersona kp " +
                "where lower(kp.keyword) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    /* public TipoDocumentacion crearTipoDocumento(TipoDocumentacion tipoDocumento) {
    TipoDocumentacion other = recuperarTipoDocumentacionPorDescripcion(tipoDocumento.getDescripcion());
    if (other != null)
    throw new TDKServerException("Ya existe un tipo de documentación con esa descripción");
    getEntityManager().persist(tipoDocumento);
    return tipoDocumento;
    }
    
    public TipoDocumentacion recuperarTipoDocumentacion(Long idTipoDocumento) {
    return getEntityManager().find(TipoDocumentacion.class, idTipoDocumento);
    }
    
    public void modificarTipoDocumentacion(TipoDocumentacion tipoDocumentacion) {
    TipoDocumentacion other = recuperarTipoDocumentacionPorDescripcion(tipoDocumentacion.getDescripcion());
    if (other != null && other.equals(tipoDocumentacion))
    throw new TDKServerException("Ya existe un tipo de documentación con esa descripción");
    getEntityManager().merge(tipoDocumentacion);
    }
    
    public List<TipoDocumentacion> listarTipoDocumentacion(String patron) {
    if (patron != null && patron.trim().length() == 0)             
    throw new TDKServerException("Ingrese un filtro");
    
    Query query = getEntityManager().createQuery("select t from TipoDocumentacion t " +
    "where lower(t.descripcion) like :patron");
    query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
    return query.getResultList();    
    }
    
    public TipoDocumentacion recuperarTipoDocumentacionPorDescripcion(String descripcion) {
    Query query = getEntityManager().createQuery("select t from TipoDocumentacion t where " +
    "lower(t.descripcion) = :patron");
    query.setParameter("patron", descripcion.trim().toLowerCase());
    TipoDocumentacion tipoDocumentacion = null;
    try {
    tipoDocumentacion = (TipoDocumentacion) query.getSingleResult();
    } catch (NoResultException ex) {
    return null;
    }
    return tipoDocumentacion;   
    }
    
    public PuntoVenta crearPuntoVenta(PuntoVenta puntoVento) {
    throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public PuntoVenta recuperarPuntoVenta(Long idPuntoVenta) {
    throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void modificarPuntoVenta(PuntoVenta puntoVenta) {
    throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<PuntoVenta> listarPuntoVenta(String patron) {
    throw new UnsupportedOperationException("Not supported yet.");
    }
     */
    public TipoContacto crearTipoContacto(TipoContacto tipoContacto) {
        TipoContacto other = recuperarTipoContactoPorDescripcion(tipoContacto.getDescripcion());
        if (other != null) {
            throw new TDKServerException("Ya existe un tipo de contacto con la descripcion " + tipoContacto.getDescripcion());
        }
        getEntityManager().persist(tipoContacto);
        return tipoContacto;
    }

    public TipoContacto recuperarTipoContacto(Long idTipoContacto) {
        return getEntityManager().find(TipoContacto.class, idTipoContacto);
    }

    public List<TipoContacto> listarTipoContacto(String patron) {
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select t from TipoContacto t " +
                "where lower(t.descripcion) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public void modificarTipoContacto(TipoContacto tipoContacto) {
        TipoContacto other = recuperarTipoContactoPorDescripcion(tipoContacto.getDescripcion());
        if (other != null && !other.equals(tipoContacto)) {
            throw new TDKServerException("Ya existe un tipo de contacto con la descripcion " + tipoContacto.getDescripcion());
        }
        getEntityManager().merge(tipoContacto);
    }

    public TipoContacto recuperarTipoContactoPorDescripcion(String descripcion) {
        Query query = getEntityManager().createQuery("select t from TipoContacto t where " +
                "lower(t.descripcion) = :patron");
        query.setParameter("patron", descripcion.trim().toLowerCase());
        TipoContacto tipoContacto = null;
        try {
            tipoContacto = (TipoContacto) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
        return tipoContacto;
    }

    public Institucion crearInstitucion(Institucion institucion) {
        Institucion other = recuperarInstitucionPorNombre(institucion.getNombre());
        if (other != null) {
            throw new TDKServerException("Existe una institucion con el nombre " + institucion.getNombre());
        }
        getEntityManager().persist(institucion);
        registerKeywordPersona(institucion);
        return institucion;
    }

    public List<Institucion> listarInstitucion(String patron) {
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(distinct kp.persona) from KeywordPersona kp , Institucion it " +
                "where lower(kp.keyword) like :patron AND it.id = kp.persona.id");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Demacioados resultados, filtre con más datos</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>No hay resultados</HTML>");
        }

        query = getEntityManager().createQuery("Select distinct kp.persona From KeywordPersona kp, Institucion it " +
                "where lower(kp.keyword) like :patron and it.id = kp.persona.id");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public Institucion recuperarInstitucion(Long idInstitucion, boolean completa) {
        Institucion institucion = getEntityManager().find(Institucion.class, idInstitucion);
        if (completa) {
            institucion.getCompetidores().size();
            institucion.getProfesores().size();
            institucion.getContactos().size();
        }
        return institucion;
    }

    public Institucion recuperarInstitucionPorNombre(String nombre) {
        Query query = getEntityManager().createQuery("select i from Institucion i where " +
                "lower(i.nombre) = :patron");
        query.setParameter("patron", nombre.trim().toLowerCase());
        Institucion institucion = null;
        try {
            institucion = (Institucion) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
        return institucion;
    }

    public void modificarInstitucion(Institucion institucion) {
        Institucion attached = recuperarInstitucionPorNombre(institucion.getNombre());
        if (attached != null && !attached.equals(institucion)) {
            throw new TDKServerException("Existe una institucion con el nombre " + institucion.getNombre());
        }

        attached = getEntityManager().find(Institucion.class, institucion.getId());
        
        updateList(attached.getCompetidores(), institucion.getCompetidores());
        updateList(attached.getContactos(), institucion.getContactos());
        updateList(attached.getProfesores(), institucion.getProfesores());

        getEntityManager().merge(institucion);

        anRegisterKeywordPersona(institucion.getId());
        registerKeywordPersona(institucion);
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<PersonaFisica> listarPersonaFisica(String patron) {
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(distinct kp.persona) from KeywordPersona kp , PersonaFisica pf " +
                "where lower(kp.keyword) like :patron AND pf.id = kp.persona.id");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Demacioados resultados, filtre con más datos</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>No hay resultados</HTML>");
        }

        query = getEntityManager().createQuery("Select distinct kp.persona From KeywordPersona kp, PersonaFisica pf " +
                "where lower(kp.keyword) like :patron and pf.id = kp.persona.id");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public List<Alumno> listarAlumnosPorInstitucion(Long idInstitucion, String patron) {
        if (patron != null && patron.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(distinct kp.persona) from KeywordPersona kp , PersonaFisica pf , Alumno al " +
                "where lower(kp.keyword) like :patron AND pf.id = kp.persona.id AND al.personaFisica.id = pf.id AND al.institucion.id = :idInstitucion");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        query.setParameter("idInstitucion", idInstitucion);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Demacioados resultados, filtre con más datos</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>No hay resultados</HTML>");
        }

        query = getEntityManager().createQuery("Select distinct al From KeywordPersona kp, PersonaFisica pf , Alumno al " +
                "where lower(kp.keyword) like :patron and pf.id = kp.persona.id " +
                "AND al.personaFisica.id = pf.id AND al.institucion.id = :idInstitucion");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        query.setParameter("idInstitucion", idInstitucion);
        return query.getResultList();
    }

    public List<TorneoInstitucion> listarInstitucionPorTorneo(Long idTorneo, String patron) {
        Query query = getEntityManager().createQuery("Select distinct ti From TorneoInstitucion ti, KeywordPersona kp, PersonaFisica pf , Alumno al " +
                "where lower(kp.keyword) like :patron and pf.id = kp.persona.id " +
                "AND al.personaFisica.id = pf.id AND al.institucion.id = ti.institucion.id " +
                "AND ti.torneo.id = :idTorneo");
        query.setParameter("patron", STARTING_WITH_WILDCARD + patron.toLowerCase() + STARTING_WITH_WILDCARD);
        query.setParameter("idTorneo", idTorneo);
        return query.getResultList();
    }

    public TorneoInstitucion crearTorneoInstitucion(TorneoInstitucion torneoInstitucion) {
        //if (existeInstitucionEnTorneo(torneoInstitucion))
        //    throw new TDKServerException("La institución <B>"+torneoInstitucion.getInstitucion().getDisplayName()+"</B> ya está inscripcta en el torneo");
        getEntityManager().persist(torneoInstitucion);
        return torneoInstitucion;
    }

    public boolean existeInstitucionEnTorneo(TorneoInstitucion torneoInstitucion) {

        Query query = getEntityManager().createQuery("Select ti From TorneoInstitucion ti " +
                "where ti.torneo.id = :idTorneo and ti.institucion = :idInstitucion");
        query.setParameter("idTorneo", torneoInstitucion.getTorneo().getId());
        query.setParameter("idInstitucion", torneoInstitucion.getInstitucion().getId());

        try {
            Object object = query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            return true;
        } catch (NoResultException ex) {
            return false;
        }


        return true;
    }

   
}

