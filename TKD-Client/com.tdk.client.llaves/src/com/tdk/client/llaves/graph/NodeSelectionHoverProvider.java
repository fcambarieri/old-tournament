/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import javax.swing.JLabel;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class NodeSelectionHoverProvider implements TwoStateHoverProvider {

    private Scene scene;

    public NodeSelectionHoverProvider(Scene scene) {
        this.scene = scene;
    }

    public void unsetHovering(Widget widget) {
        if (widget != null) {
            widget.setBackground(scene.getLookFeel().getBackground(ObjectState.createNormal()));
            widget.setForeground(scene.getLookFeel().getForeground(ObjectState.createNormal()));
            print("Node selected: nonde");
        }
    }

    public void setHovering(Widget widget) {
        if (widget != null) {
            ObjectState state = ObjectState.createNormal().deriveSelected(true);
            widget.setBackground(scene.getLookFeel().getBackground(state));
            widget.setForeground(scene.getLookFeel().getForeground(state));
            if (widget.getPreferredLocation() != null) {
                print("Node selected: " + widget.toString() + "( " + widget.getPreferredLocation().x + ", " + widget.getPreferredLocation().y + " )");
            }

        }
    }

    private void print(String str) {
        System.out.println(str);
    }
}
