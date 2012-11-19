/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.swing.node.actions;

import java.awt.Point;
import javax.swing.JLabel;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetMouseEvent;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class PositionAction extends WidgetAction.Adapter {

    private JLabel position;
    
    public PositionAction(JLabel label) {
        this.position = label;
    }

    @Override
    public State mouseMoved(Widget widget, WidgetMouseEvent event) {
        Point point = widget.convertLocalToScene(event.getPoint());
        position.setText("Position: x = " + point.x + ", y = " + point.y);
        return State.REJECTED;
    }
    
}