/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.domain.security.Rol;
import com.tdk.domain.security.Usuario;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.security.UserController;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.StringPropertyValidator;

/**
 *
 * @author fernando
 */
public class UsuarioController extends UserController<Usuario>{

    @Override
    public Usuario crearUsuario() {
        Usuario u = new Usuario();
        return modificarUsuario(u);
    }

    @Override
    public Usuario modificarUsuario(Usuario usuario) {
        usuario.setPassword(password.get());
        usuario.setRol((Rol) rolNode.get().getValue());
        usuario.setUserName(usuarioName.get());
        usuario.setFechaDesde(fechaDesde.get());
        return usuario;
    }

    @Override
    public void initControllerForUpdate(Usuario usuario) {
        rolNode.set(new RolNode(usuario.getRol()));
        fechaDesde.set(usuario.getFechaDesde());
        usuarioName.set(usuario.getUserName());
        password.set(usuario.getPassword());
    }

    @Override
    protected ChoiceField<SwingNode> getRolChoiceField() {
        ChoiceField<SwingNode> rolCF = new ChoiceField<SwingNode>(form.txtPerfil,"Seleccione un Rol", new RolRootNode(), RolNode.class);
        rolCF.addAnnounceSupportTag(Rol.class);
        return rolCF;
    }

    @Override
    protected void installValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new StringPropertyValidator(usuarioName, "Ingrese un nombre de usuario (mayor de 3 caracteres)", 3, 64));
        getSwingValidator().addSwingValidator(new StringPropertyValidator(password, "Ingrese un password (mayor de 3 caracteres)", 3, 15));
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione un perfil") {
            @Override
            public boolean validate() {
                return rolNode.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Ingrese una fecha desde") {
            @Override
            public boolean validate() {
                return fechaDesde.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Ingrese una fecha hasta") {
            @Override
            public boolean validate() {
                return validarFechaHasta() ;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("La fecha desde tiene que menor a la fecha hasta") {
            @Override
            public boolean validate() {
                return validarFechaHasta() ? true : (fechaDesde.get() != null) && fechaDesde.get().compareTo(fechaHasta.get()) <= 0 ;
            }
        });
    }

}
