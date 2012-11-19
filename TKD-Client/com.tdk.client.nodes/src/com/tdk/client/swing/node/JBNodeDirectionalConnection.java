/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class JBNodeDirectionalConnection implements JBNodeConnectionFactory {

    public ConnectionWidget createJBNodeConnection(Widget sourceNode, Widget targetNode, BNode node, LayerWidget layer) {
        ConnectionWidget edge = new ConnectionWidget(layer.getScene());
        edge.setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceNode, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
        edge.setTargetAnchor(AnchorFactory.createDirectionalAnchor(targetNode, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
        edge.setRouter(RouterFactory.createOrthogonalSearchRouter(layer));
        return edge;
    }

}
