/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import java.util.Map;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public interface JBNodeLocationFactory {

    void createJBNodeLocation(BNode node, Map<BNode, Widget> nodeMapping, int xp, int yp, int top, int level);
    
}
