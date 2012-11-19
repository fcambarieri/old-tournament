/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public interface JBNodeConnectionFactory {

    ConnectionWidget createJBNodeConnection(Widget sourceNode, Widget targetNode, BNode node, LayerWidget layer);
}
