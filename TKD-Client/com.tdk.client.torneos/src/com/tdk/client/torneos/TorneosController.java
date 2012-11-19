/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.SwingRootNodeController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class TorneosController extends SwingRootNodeController {

    @Override
    protected ChoiceRootNode getChoiceRootNode() {
        return new TorneoRootNode();
    }

    @Override
    protected void loadInfo() {
        setFormTitle("Torneos");
        setFormTitleIcon("com/tdk/client/torneos/torneo-41x48.png");
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }

    @Override
    protected boolean canAcceptDialog() {
        return true;
    }

}
