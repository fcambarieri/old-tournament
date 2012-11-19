/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias.node;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.SwingRootNodeController;

/**
 *
 * @author sabon
 */
public class CategoriasRootNodeController extends SwingRootNodeController {

    @Override
    protected ChoiceRootNode getChoiceRootNode() {
        return new CategoriasRootNode();
    }

    @Override
    protected void loadInfo() {
        setFormTitle("Categorias");
        setFormTitleIcon("com/tdk/client/llaves/llaves-48x48.png");
    }
    
}
