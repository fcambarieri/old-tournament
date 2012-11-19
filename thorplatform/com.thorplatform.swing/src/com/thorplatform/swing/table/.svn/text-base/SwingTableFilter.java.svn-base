/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.table;

import java.awt.BorderLayout;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXSearchPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.decorator.PatternFilter;

/**
 *
 * @author fernando
 */
public class SwingTableFilter extends JXPanel {

    private JXCollapsiblePane jxCollapsiblePane;
    private JXSearchPanel jxSearchPanel;
    private JXTaskPane jxTaskPane;
    
    public SwingTableFilter() {
        this("Filtrar...");
    }
    
    public SwingTableFilter(String filterTitle) {
        initForm();
        
        getJxTaskPane().setTitle(filterTitle);
    }
    
    public void initForm() {
        
        jxCollapsiblePane = new JXCollapsiblePane(new BorderLayout());
        jxTaskPane = new JXTaskPane();
        jxSearchPanel = new JXSearchPanel();
        
        setLayout(new BorderLayout());
        add(jxCollapsiblePane, BorderLayout.CENTER);
        getJxCollapsiblePane().add(getJxTaskPane(), BorderLayout.CENTER);
        
        getJxTaskPane().setLayout(new BorderLayout());
        getJxTaskPane().add(getJxSearchPanel(), BorderLayout.CENTER);
        getJxCollapsiblePane().setAnimated(true);
    }

    public void setPatternFilter(PatternFilter patternFilter) {
        getJxSearchPanel().setPatternFilter(patternFilter);
    }

    public JXCollapsiblePane getJxCollapsiblePane() {
        return jxCollapsiblePane;
    }

    public void setJxCollapsiblePane(JXCollapsiblePane jxCollapsiblePane) {
        this.jxCollapsiblePane = jxCollapsiblePane;
    }

    public JXSearchPanel getJxSearchPanel() {
        return jxSearchPanel;
    }

    public void setJxSearchPanel(JXSearchPanel jxSearchPanel) {
        this.jxSearchPanel = jxSearchPanel;
    }

    public JXTaskPane getJxTaskPane() {
        return jxTaskPane;
    }

    public void setJxTaskPane(JXTaskPane jxTaskPane) {
        this.jxTaskPane = jxTaskPane;
    }
}
