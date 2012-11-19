/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas;

import com.tdk.domain.security.Acceso;
import com.thorplatform.swing.actions.AbstractMenuSwingAction;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Personas component.
 */
public class PersonasAction extends AbstractMenuSwingAction {

    public PersonasAction() {
        super(NbBundle.getMessage(PersonasAction.class, "CTL_PersonasAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(PersonasTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = PersonasTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    protected String getFuncionality() {
        return "Personas";
    }

    @Override
    protected Object getAccess() {
        return Acceso.CONSULTA;
    }

    public void setIsLogin(boolean arg0) {
        super.setIslogin(arg0);
    }

    
    
    
}
