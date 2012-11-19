/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.domain.security.Funcionalidad;
import com.tdk.domain.security.FuncionalidadRol;
import com.tdk.domain.security.FuncionalidadUsuario;
import com.tdk.domain.security.Rol;
import com.tdk.domain.security.Usuario;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.security.AccesosController;

/**
 *
 * @author fernando
 */
public class TKDAccesosController extends AccesosController{


    @Override
    protected ChoiceField<SwingNode> getChoiceFieldFuncion() {
       ChoiceField<SwingNode> funcion = new ChoiceField<SwingNode>(form.txtPermiso, "Seleccione una funcionalidad", new FuncionalidadRootNode(), FuncionalidadNode.class);
       funcion.addAnnounceSupportTag(Funcionalidad.class);
       return funcion;
    }

    @Override
    protected ChoiceField<SwingNode> getChoiceFieldUsuario() {
       ChoiceField<SwingNode> usuarioCF = new ChoiceField<SwingNode>(form.txtPerfil, "Seleccione una usuario", new UsuarioRootNode(), UsuarioNode.class);
       usuarioCF.addAnnounceSupportTag(Funcionalidad.class);
       return usuarioCF;
    }

    @Override
    protected ChoiceField<SwingNode> getChoiceFieldRol() {
       ChoiceField<SwingNode> rolCF = new ChoiceField<SwingNode>(form.txtPerfil, "Seleccione un Rol", new RolRootNode(), RolNode.class);
       rolCF.addAnnounceSupportTag(Funcionalidad.class);
       return rolCF;
    }

    public void initControllerForRol(Rol rol) {
        configureForRol();
        rolNode.set(new RolNode(rol));
    }
    
    public void initControllerForUpdateFuncionalidadRol(FuncionalidadRol funcionalidadRol) {
        initControllerForRol(funcionalidadRol.getRol());
        alta.set(funcionalidadRol.getAlta());
        baja.set(funcionalidadRol.getBaja());
        modificacion.set(funcionalidadRol.getModificacion());
        consulta.set(funcionalidadRol.getConsulta());
        funcionNode.set(new FuncionalidadNode(funcionalidadRol.getFuncionalidad()));
    }
    
    public FuncionalidadRol crearFuncionalidadRol() {
        FuncionalidadRol fr = new FuncionalidadRol();
        fr.setFuncionalidad((Funcionalidad) funcionNode.get().getValue());
        fr.setRol((Rol) rolNode.get().getValue());
        return modificacionFuncionalidadRol(fr);
    }
    
    public FuncionalidadRol modificacionFuncionalidadRol(FuncionalidadRol funcionalidadRol) {
        funcionalidadRol.setAlta(alta.get());
        funcionalidadRol.setBaja(baja.get());
        funcionalidadRol.setModificacion(modificacion.get());
        funcionalidadRol.setConsulta(consulta.get());
        return funcionalidadRol;
    }
    
    /***********************************************************************/
    
    public void initControllerForUsuario(Usuario usuario) {
        configureForUsuario();
        usuarioNode.set(new UsuarioNode(usuario));
    }
    
    public void initControllerForUpdateFuncionalidadUsuario(FuncionalidadUsuario funcionalidadUsuario) {
        initControllerForUsuario(funcionalidadUsuario.getUsuario());
        alta.set(funcionalidadUsuario.getAlta());
        baja.set(funcionalidadUsuario.getBaja());
        modificacion.set(funcionalidadUsuario.getModificacion());
        consulta.set(funcionalidadUsuario.getConsulta());
        funcionNode.set(new FuncionalidadNode(funcionalidadUsuario.getFuncionalidad()));
    }
    
    public FuncionalidadUsuario crearFuncionalidadUsuario() {
        FuncionalidadUsuario fr = new FuncionalidadUsuario();
        fr.setFuncionalidad((Funcionalidad) funcionNode.get().getValue());
        fr.setUsuario((Usuario) usuarioNode.get().getValue());
        return modificacionFuncionalidadUsuario(fr);
    }
    
    public FuncionalidadUsuario modificacionFuncionalidadUsuario(FuncionalidadUsuario funcionalidadUsuario) {
        funcionalidadUsuario.setAlta(alta.get());
        funcionalidadUsuario.setBaja(baja.get());
        funcionalidadUsuario.setModificacion(modificacion.get());
        funcionalidadUsuario.setConsulta(consulta.get());
        return funcionalidadUsuario;
    }
}
