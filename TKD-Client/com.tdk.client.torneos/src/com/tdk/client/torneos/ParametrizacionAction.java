/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos;

import com.tdk.domain.security.Acceso;
import com.thorplatform.swing.actions.AbstractMenuSwingAction;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Parametrizacion component.
 */
public class ParametrizacionAction extends AbstractMenuSwingAction {

    public ParametrizacionAction() {
        super(NbBundle.getMessage(ParametrizacionAction.class, "CTL_ParametrizacionAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ParametrizacionTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ParametrizacionTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    protected String getFuncionality() {
        return "Parametrizacion";
    }

    @Override
    protected Object getAccess() {
        return Acceso.CONSULTA;
    }

    public void setIsLogin(boolean isLogin) {
        super.setIslogin(isLogin);
    }
}
