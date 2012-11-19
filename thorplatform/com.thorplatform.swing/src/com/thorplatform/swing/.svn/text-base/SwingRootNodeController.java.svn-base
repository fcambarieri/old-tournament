/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author fernando
 */
public abstract class SwingRootNodeController extends SwingController {

    private ExplorerManager explorerManager;
    private RootsNodesForm form;
    private ChoiceRootNode rootNode;
    protected Property<String> filter = new Property<String>("filter");
    
    
    private class ChoiceBeanTreeView extends BeanTreeView {
        public ChoiceBeanTreeView() {
            super();
        }
        
    }

    public SwingRootNodeController() {
        form = new RootsNodesForm();
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    public void setFormTitle(String tile) {
        form.lblTitle.setText(tile);
    }

    public void setFormTitleIcon(Icon icon) {
        form.lblIcon.setIcon(icon);
    }

    public void setFormTitleSearchFor(String stringSearchFor) {
        form.lblTextFilter.setText(stringSearchFor);
    }

    @Override
    public void initController() {
        super.initController();
        
        getSwingBinder().bindTextComponentToString(form.txtFilter, filter);
        
        configureView();
        
        installAction();
    }
    
    private void configureView() {
        rootNode = getChoiceRootNode();
        
        explorerManager = new ExplorerManager();
        explorerManager.setRootContext(rootNode);
        
        form.setExplorerManager(explorerManager);
        form.lblTextFilter.setText(rootNode.getTitleForSearch());
        
        ChoiceBeanTreeView treeView = new ChoiceBeanTreeView();
        treeView.setRootVisible(false);
        form.pnlTreeView.add(treeView, BorderLayout.CENTER);
        
        loadInfo();
        
    }
    
    private void installAction() {
        form.txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        refreshResult();
                 }
            }
                
        });
    }

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    protected void refreshResult() {
        if (form.isShowing())
            rootNode.replaceKeys(filter.get());
    }
    
    protected abstract ChoiceRootNode getChoiceRootNode();
    
    protected abstract void loadInfo();
    
    
    public void setFormTitleIcon(String path) {
        ImageIcon icon = new ImageIcon(Utilities.loadImage(path));
        setFormTitleIcon(icon);
    }   
    
    public JPanel getPanel() {
        return getForm();
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        //Do nothing!
    }
    
    
    
}
