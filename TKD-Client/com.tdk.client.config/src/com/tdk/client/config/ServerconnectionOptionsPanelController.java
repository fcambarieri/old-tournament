/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.config;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.TopLevelRegistration(
        categoryName = "#OptionsCategory_Name_Serverconnection",
        iconBase = "com/tdk/client/config/panel-config-2-32x32.png",
        keywords = "#OptionsCategory_Keywords_Serverconnection",
        keywordsCategory = "Serverconnection")
@org.openide.util.NbBundle.Messages({"OptionsCategory_Name_Serverconnection=Server connection", "OptionsCategory_Keywords_Serverconnection=p"})
public final class ServerconnectionOptionsPanelController extends OptionsPanelController {

    private ServerconnectionPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    //field.getValue().matches("[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}"))
    
    public void update() {
        getPanel().load();
        changed = false;
    }

    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    public boolean isValid() {
        return getPanel().valid();
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private ServerconnectionPanel getPanel() {
        if (panel == null) {
            panel = new ServerconnectionPanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
