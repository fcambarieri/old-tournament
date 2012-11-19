/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node.actions;

import com.tdk.client.nodes.AbstractBinaryTreeModel;
import com.tdk.client.nodes.BNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *
 * @author fernando
 */
public abstract class AbstractBinaryAction extends AbstractAction {

    private AbstractBinaryTreeModel treeModel;
    
    public AbstractBinaryAction() {
       this("Sin info");
    }
    
    public AbstractBinaryAction(String title) {
        this(title, null);
    }
    
    public AbstractBinaryAction(String title, Icon icon) {
        this(title, icon, null);
    }
    
    public AbstractBinaryAction(String title, Icon icon, AbstractBinaryTreeModel treeModel) {
        super(title, icon);
        this.treeModel = treeModel;
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (changeActionPerformed(evt) && getTreeModel() != null) {
            getTreeModel().fireNodeChange(getBNode());
        }
    }

    public AbstractBinaryTreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(AbstractBinaryTreeModel treeModel) {
        this.treeModel = treeModel;
    }
    
    public abstract boolean changeActionPerformed(ActionEvent evt) ;
    
    public abstract BNode getBNode();

}
