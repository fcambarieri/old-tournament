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
 * Action which shows Roles component.
 */
public class RolesAction extends AbstractMenuSwingAction {

    public RolesAction() {
        super(NbBundle.getMessage(RolesAction.class, "CTL_RolesAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(RolesTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = RolesTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    protected String getFuncionality() {
        return "Roles";
    }

    @Override
    protected Object getAccess() {
        return Acceso.CONSULTA;
    }

    public void setIsLogin(boolean isLogin) {
        setEnabled(isLogin);
    }
}
