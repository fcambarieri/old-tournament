/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.config;

import com.tdk.client.config.toolbar.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
             //Get the main window of the NetBeans Platform:
//        JFrame frame = (JFrame) WindowManager.getDefault().getMainWindow();
//        //Get our custom main toolbar:  
//        JComponent toolbar = ToolbarComponentProvider.getDefault().createToolbar();
//        //Set the new layout of our root pane:
//        frame.getRootPane().setLayout(new MyRootPaneLayout(toolbar));
//        //Install a new toolbar component into the layered pane 
//        //of the main frame on layer 0: 
//        toolbar.putClientProperty(JLayeredPane.LAYER_PROPERTY, 0);
//        frame.getRootPane().getLayeredPane().add(toolbar, 0);
    }
}
