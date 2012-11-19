/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class JDefaultBNodeLocationFactory implements JBNodeLocationFactory {

    public void createJBNodeLocation(BNode node, Map<BNode, Widget> nodeMapping, int xp, int yp, int top, int level) {
        if (node != null) {
            Widget jn = nodeMapping.get(node);
            jn.setPreferredLocation(new Point(xp, yp));
            Rectangle bounds = jn.getBounds();
            xp = xp - 200 - (bounds != null ? bounds.width : 0);
            top = top / 2;

            createJBNodeLocation(node.getLeftNode(), nodeMapping, xp, yp + top, top, --level);
            createJBNodeLocation(node.getRightNode(), nodeMapping, xp, yp - top, top, --level);
        }
    }
}
