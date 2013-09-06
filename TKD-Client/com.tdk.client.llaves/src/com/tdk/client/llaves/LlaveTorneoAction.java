/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import com.tdk.domain.security.Acceso;
import com.thorplatform.swing.actions.AbstractMenuSwingAction;
import java.awt.event.ActionEvent;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author sabon
 */
public class LlaveTorneoAction extends AbstractMenuSwingAction {

    public LlaveTorneoAction() {
        super(NbBundle.getMessage(LlaveTorneoAction.class, "CTL_TorneosAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage("com/tdk/client/llaves/vs-16x16.gif", true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = TorneoLlavesTopComponent.getDefault();
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
