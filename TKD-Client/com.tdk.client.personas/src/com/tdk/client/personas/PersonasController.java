/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.SwingRootNodeController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class PersonasController extends SwingRootNodeController{

    @Override
    protected ChoiceRootNode getChoiceRootNode() {
        return new PersonaRootNode();
    }

    @Override
    protected void loadInfo() {
        setFormTitle("Personas");
        setFormTitleIcon("com/tdk/client/personas/persona-48x48.png");
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }

    @Override
    protected boolean canAcceptDialog() {
        return true;
    }

}
