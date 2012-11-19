/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.services;

import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.Rol;
import com.tdk.domain.security.Funcionalidad;
import com.tdk.domain.security.FuncionalidadRol;
import com.tdk.domain.security.FuncionalidadUsuario;
import com.tdk.domain.security.Usuario;
import java.util.List;

/**
 *
 * @author fernando
 */
public interface SecurityServiceRemote {

    Usuario crearUsuario(Usuario usuario);
    Usuario recuperarUsuario(String name);
    void modificarUsuario(Usuario usuario);
    List<Usuario> listarUsuarios(String name);
    
    Funcionalidad crearFuncionalidad(Funcionalidad permiso);
    void modificarFuncionalidad(Funcionalidad permiso);
    Funcionalidad recuperarFuncionalidad(Long idPermiso);
    List<Funcionalidad> listarFuncionalidad(String descripcion);
    
    Rol crearRol(Rol rol);
    void modificarRol(Rol rol);
    Rol recuperarRolPorDescripcion(String descripcion);
    List<Rol> listarRoles(String descripcion);
    
    List<FuncionalidadRol> listarFuncionesPorRol(Long idPerfil);
    List<FuncionalidadUsuario> listarFuncionalidadesPorUsuario(Long idUsuario);

    FuncionalidadRol crearFuncionalidadRol(FuncionalidadRol funcionRol);
    void modificarFuncionalidadRol(FuncionalidadRol funcionRol);
    void crearFuncionalidadesRol(List<FuncionalidadRol> funcionesRoles);
    
    FuncionalidadUsuario crearRolUsuario(FuncionalidadUsuario rolUsuario);
    void modificarRolUsuario(FuncionalidadUsuario rolUsuario);
    void crearRolUsuario(List<FuncionalidadUsuario> permisos);
    
    
    Usuario login(String usuario, String password);
    
    List<Acceso> recuperarPermiso(Usuario usuario, String funcionalidad);
}
