/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.security;

import com.tdk.domain.security.Acceso;
import com.thorplatform.swing.actions.AbstractMenuSwingAction;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Usuarios component.
 */
public class UsuariosAction extends AbstractMenuSwingAction {

    public UsuariosAction() {
        super(NbBundle.getMessage(UsuariosAction.class, "CTL_UsuariosAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(UsuariosTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = UsuariosTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    protected String getFuncionality() {
        return "Usuarios";
    }

    @Override
    protected Object getAccess() {
        return Acceso.CONSULTA;
    }

    public void setIsLogin(boolean login) {
        setEnabled(login);
    }
}
