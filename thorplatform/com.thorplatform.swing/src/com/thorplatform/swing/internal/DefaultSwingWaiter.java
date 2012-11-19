/*
 * DefaultSwingWaiter.java
 *
 * Created on 31 de enero de 2008, 08:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.thorplatform.swing.internal;

import com.thorplatform.swing.SwingWaiterStrategy;
import com.thorplatform.swing.SwingWaiter;

import java.awt.Component;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author jrunge
 */
public class DefaultSwingWaiter implements SwingWaiter {
    
    private DefaultSwingGlassPane dsGlassPane = new DefaultSwingGlassPane();
    private SwingWaiterStrategy swStrategy;
    /** Creates a new instance of DefaultSwingWaiter */
    public DefaultSwingWaiter() {
    }
    
    public void setComponent(Component c) {
        JRootPane rootPane = SwingUtilities.getRootPane(c);
        dsGlassPane.setSize(rootPane.getSize());
        rootPane.setGlassPane(dsGlassPane);
    }
    
    private void perform() {
        try {
            swStrategy.execute();
            dsGlassPane.stop();
        } catch (Throwable t) {
            dsGlassPane.stop();
        }
    }
    
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dsGlassPane.start();
                Thread performer = new Thread(new Runnable() {
                    public void run() {
                        perform();
                    }
                }, "Performer");
                performer.start();
            }
        });
    }
    
    public void stop() {
        dsGlassPane.setVisible(false);
        dsGlassPane.interrupt();
    }
    
    public void setStrategy(SwingWaiterStrategy s) {
        swStrategy = s;
    }
}
