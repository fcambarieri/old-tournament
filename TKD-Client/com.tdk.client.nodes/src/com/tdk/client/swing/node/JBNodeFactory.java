/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class JBNodeFactory implements WidgetFactory{

    public Widget createJBNode(Scene scene, BNode node) {
        JBNode jn = new JBNode(scene, node);
        jn.getActions().addAction(ActionFactory.createMoveAction());
        return jn;
    }
}
