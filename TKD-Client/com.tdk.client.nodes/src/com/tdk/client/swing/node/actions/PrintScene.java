/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.swing.node.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author fernando
 */
public class PrintScene extends AbstractAction {

    private Scene scene;
    
    public PrintScene (Scene scene) {
       this.scene = scene;
    }

    public void actionPerformed(ActionEvent arg0) {
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
