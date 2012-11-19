package com.thorplatform.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;

/**
 *
 * @author Fernando
 */
public class ExplorerManagerTreePanel extends JPanel implements ExplorerManager.Provider {
    
    private ExplorerManager explorerManager;
    private BeanTreeView treeView;
    
    public ExplorerManagerTreePanel() {
        explorerManager = new ExplorerManager();
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 100));
        
        treeView = new BeanTreeView();
        treeView.setRootVisible(false);
        add(treeView, BorderLayout.CENTER);
    }
    
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }
    
    public BeanTreeView getBeanTreeView() {
        return treeView;
    }
    
}
