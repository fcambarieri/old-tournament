/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Organimgrama component.
 */
public class OrganimgramaAction extends AbstractAction {

    public OrganimgramaAction() {
        super(NbBundle.getMessage(OrganimgramaAction.class, "CTL_OrganimgramaAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(OrganimgramaTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = OrganimgramaTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
