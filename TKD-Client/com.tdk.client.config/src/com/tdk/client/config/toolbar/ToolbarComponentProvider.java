/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.config.toolbar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openide.util.Lookup;

/**
 *
 * @author sabon
 */
public abstract class ToolbarComponentProvider {
    
     public abstract JComponent createToolbar();

    public static ToolbarComponentProvider getDefault() {
        ToolbarComponentProvider provider = Lookup.getDefault().lookup(ToolbarComponentProvider.class);
        if (provider == null) {
            provider = new DefaultToolbarComponentProvider();
        }
        return provider;
    }

    private static class DefaultToolbarComponentProvider extends ToolbarComponentProvider {
        @Override
        public JComponent createToolbar() {
            JTabbedPane pane = new JTabbedPane();
            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
            panel1.add(new JButton("Button 1"));
            panel1.add(new JButton("Button 2"));
            pane.add("Tab 1", panel1);
            pane.add("Tab 2", new JPanel());
            pane.setPreferredSize(new Dimension(100, 70));
            return pane;
        }
    }
}