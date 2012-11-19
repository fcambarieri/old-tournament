/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class JTreeScene extends GraphScene<BNode, String>{

    private Map<BNode, Widget> mapNode = new HashMap<BNode, Widget>();
    private WidgetFactory widgetFactory;

    @Override
    protected Widget attachNodeWidget(BNode node) {
        Widget w = widgetFactory.createJBNode(this, node);
        mapNode.put(node, w);

        return w;
    }

    @Override
    protected Widget attachEdgeWidget(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeSourceAnchor(String arg0, BNode arg1, BNode arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(String arg0, BNode arg1, BNode arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
