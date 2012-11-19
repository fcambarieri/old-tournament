/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.SwingRootNodeController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class LlavesRootNodeController extends SwingRootNodeController{

    @Override
    protected ChoiceRootNode getChoiceRootNode() {
        return new LlaveRootNode();
    }

    @Override
    protected void loadInfo() {
        setFormTitle("Llaves");
        setFormTitleIcon("com/tdk/client/llaves/llaves-48x48.png");
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }


}
