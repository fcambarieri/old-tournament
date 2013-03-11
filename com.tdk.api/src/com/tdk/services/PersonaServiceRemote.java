/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.services;

import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.Persona;
import com.tdk.domain.PersonaFisica;
import com.tdk.domain.TipoContacto;
import com.tdk.domain.torneo.TorneoInstitucion;
import java.util.List;

/**
 *
 * @author fernando
 */
public interface PersonaServiceRemote {

    public Institucion crearInstitucion(Institucion institucion);
    
    public void modificarInstitucion(Institucion institucion);

    public List<Institucion> listarInstitucion(String patron);
    
    public TorneoInstitucion crearTorneoInstitucion(TorneoInstitucion torneoInstitucion);
    
    public List<TorneoInstitucion> listarInstitucionPorTorneo(Long idTorneo, String patron);

    public Institucion recuperarInstitucion(Long idInstitucion, boolean completa);

    public Institucion recuperarInstitucionPorNombre(String nombre);

    public Persona crearPersona(Persona persona);

    public Persona recuperarPersona(Long id, boolean completa);

    public void modificarPersona(Persona persona);

    public List<Persona> listarPersona(String patron);
    
    public List<PersonaFisica> listarPersonaFisica(String patron);

    public TipoContacto crearTipoContacto(TipoContacto tipoContacto);

    public TipoContacto recuperarTipoContacto(Long idTipoContacto);

    public List<TipoContacto> listarTipoContacto(String patron);

    public void modificarTipoContacto(TipoContacto tipoContacto);

    public TipoContacto recuperarTipoContactoPorDescripcion(String descripcion);
    
    public List<Alumno> listarAlumnosPorInstitucion(Long idInstitucion, String patron);
    
    public Alumno crearAlumno(Alumno a);
    
}
