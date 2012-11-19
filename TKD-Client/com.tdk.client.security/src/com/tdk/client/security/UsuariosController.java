/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.SwingRootNodeController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class UsuariosController extends SwingRootNodeController{

    @Override
    protected ChoiceRootNode getChoiceRootNode() {
        return new UsuarioRootNode();
    }

    @Override
    protected void loadInfo() {
        setFormTitle("Usuarios");
        setFormTitleIcon("com/tdk/client/security/usuarios-48x48.png");
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }

    @Override
    protected boolean canAcceptDialog() {
        return true;
    }

}
