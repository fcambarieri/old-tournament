/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "com.tdk.client.llaves.TorneoLlaves")
@ActionRegistration(
        iconBase = "com/tdk/client/llaves/vs-16x16.gif",
        displayName = "#CTL_TorneoLlaves")
@ActionReference(path = "Toolbars/File", position = 300)
@Messages("CTL_TorneoLlaves=Llaves del Torneo")
public final class TorneoLlaves implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
       TorneoLlavesTopComponent win =  TorneoLlavesTopComponent.getDefault();
       win.open();
       win.requestActive();
    }
}
