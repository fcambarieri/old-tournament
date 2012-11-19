/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.client.llaves.competencias.CompetenciaTopComponent;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.WeakListeners;
import org.openide.windows.TopComponent;

/**
 *
 * @author fernando
 */
public class StatusGraph implements StatusLineElementProvider , PropertyChangeListener {

    public static final JLabel status = new JLabel();

    public StatusGraph() {
        TopComponent.getRegistry().addPropertyChangeListener(
                WeakListeners.propertyChange(this, TopComponent.getRegistry()));
    }

    public Component getStatusLineElement() {
        return status;
    }

    public static void setMessage(String text) {
        status.setText(text);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (TopComponent.Registry.PROP_ACTIVATED.equals(evt.getPropertyName())) {
            activatedComponent();
        }
    }

    private void activatedComponent() {
        status.setVisible(enabledThisComponent());
    }

    protected boolean enabledThisComponent() {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc == null) return false;
        return tc.getClass().isAssignableFrom(CompetenciaTopComponent.class);
    }
}
