/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import java.awt.Dimension;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author fernando
 */
public class JBNode extends LabelWidget {

    protected BNode bnode;

    public JBNode(Scene scene, BNode node) {
        super(scene, "Sin info");
        setBnode(node);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder ());
    }

    public BNode getBnode() {
        return bnode;
    }

    public void setBnode(BNode bnode) {
        this.bnode = bnode;
        String msg = "";
        if (bnode != null) {
            if (bnode.getUserObject() != null) {
                msg = bnode.getDisplay();
            } else {
                msg = bnode.toString();
            }
        }
        this.setPreferredSize(new Dimension(150, 20));
        this.setLabel(msg);
    }
}
