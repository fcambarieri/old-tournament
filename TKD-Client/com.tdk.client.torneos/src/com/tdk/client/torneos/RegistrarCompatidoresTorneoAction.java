/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos;

import com.tdk.domain.security.Acceso;
import com.thorplatform.swing.actions.AbstractMenuSwingAction;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows RegistrarCompatidoresTorneo component.
 */
public class RegistrarCompatidoresTorneoAction extends AbstractMenuSwingAction {

    public RegistrarCompatidoresTorneoAction() {
        super(NbBundle.getMessage(RegistrarCompatidoresTorneoAction.class, "CTL_RegistrarCompatidoresTorneoAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(RegistrarCompatidoresTorneoTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = RegistrarCompatidoresTorneoTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    protected String getFuncionality() {
        return "Torneos";
    }

    @Override
    protected Object getAccess() {
        return Acceso.ALTA;
    }

    public void setIsLogin(boolean arg0) {
        setEnabled(arg0);
    }
}
