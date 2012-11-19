/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.institucion;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.SwingRootNodeController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class InstitucionesController extends SwingRootNodeController{

    @Override
    protected ChoiceRootNode getChoiceRootNode() {
        return new InstitucionRootNode();
    }

    @Override
    protected void loadInfo() {
        setFormTitle("Instituciones");
        setFormTitleIcon("com/tdk/client/personas/institucion-48x48.png");
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }

    @Override
    protected boolean canAcceptDialog() {
        return true;
    }

}
