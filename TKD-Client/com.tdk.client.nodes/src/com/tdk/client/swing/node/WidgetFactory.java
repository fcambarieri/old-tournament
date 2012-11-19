/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node;

import com.tdk.client.nodes.BNode;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public interface WidgetFactory {
    
    Widget createJBNode(Scene scene, BNode node);

}
