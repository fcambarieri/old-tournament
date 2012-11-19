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
 * Action which shows Torneos component.
 */
public class TorneosAction extends AbstractMenuSwingAction {

    public TorneosAction() {
        super(NbBundle.getMessage(TorneosAction.class, "CTL_TorneosAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(TorneosTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = TorneosTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    protected String getFuncionality() {
        return "Torneos";
    }

    @Override
    protected Object getAccess() {
        return Acceso.CONSULTA;
    }

    public void setIsLogin(boolean isLogin) {
        super.setIslogin(isLogin);
    }
}
