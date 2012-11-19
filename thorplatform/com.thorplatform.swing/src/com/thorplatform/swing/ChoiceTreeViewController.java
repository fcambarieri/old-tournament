package com.thorplatform.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;

/**
 *
 * @author Fernando
 */
public class ChoiceTreeViewController extends SwingModalController {
    
    private ChoiceTreeViewForm form;
    private Class selectableClass;
    private ExplorerManager explorerManager;
    
    public final ListProperty<Object> selectedNodes = new ListProperty<Object>("selectedNode");
    public final Property<String> patternText = new Property<String>("patternText");
    
    private class ChoiceBeanTreeView extends BeanTreeView {
        public ChoiceBeanTreeView() {
            super();
            
            tree.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) { }
                
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && canSelect()) {
                        form.btnAceptar.doClick();
                    }
                }
                
                public void keyReleased(KeyEvent e) { }
            });
            
            tree.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && canSelect()) {
                        form.btnAceptar.doClick();
                    }
                }
            });
        }
        
    }
    
    public ChoiceTreeViewController() {
        form = new ChoiceTreeViewForm();
    }
    
    /**
     * Implemento acá binding unidireccional para ExplorerManager, sin usar SwingBinder.
     * El binding del TextField sí es convencional.
     */
    @Override
    public void initController() {
        super.initController();
        
        explorerManager = new ExplorerManager();
        form.setExplorerManager(getExplorerManager());
        
        getExplorerManager().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES))
                    viewSelectedNodesChange();
            }
        });
        
        /*selectedNodes.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                modelSelectedNodeChange(evt);
            }
        });*/
        
        selectedNodes.addListener(new ListProperty.Listener() {
            public void onDataChange() {
                modelSelectedNodeChange(new PropertyChangeEvent(selectedNodes, selectedNodes.getName(), null, selectedNodes.getList()));
            }

            public void onAddItem(int index) {
                modelSelectedNodeChange(new PropertyChangeEvent(selectedNodes, selectedNodes.getName(), null, selectedNodes.getList()));
            }

            public void onRemoveItem(int index) {
                modelSelectedNodeChange(new PropertyChangeEvent(selectedNodes, selectedNodes.getName(), null, selectedNodes.getList()));
            }
        });
        
        // la inicializo antes de bindear para no tener el evento
        patternText.set("");
        getSwingBinder().bindTextComponentToString(form.txtPattern, patternText);
        
        form.txtPattern.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                patternTextAction();
            }
        });
        
        ChoiceBeanTreeView treeView = new ChoiceBeanTreeView();
        treeView.setRootVisible(false);
        form.pnlTreeView.add(treeView, BorderLayout.CENTER);
    }
    
    private void patternTextAction() {
        if (form.isShowing()) {
            refreshResults();
        }
    }
    
    private void refreshResults() {
        ChoiceRootNode choiceRootNode = (ChoiceRootNode) getExplorerManager().getRootContext();
        choiceRootNode.replaceKeys(patternText.get());
    }
    
    @SuppressWarnings("unchecked")
    private boolean canSelect() {
        boolean result = false;
        
        Node[] nodes = getExplorerManager().getSelectedNodes();
        if (nodes.length > 0) {
            int index = 0;
            boolean results = true;
            while (results && index < nodes.length) {
                results = selectableClass.isAssignableFrom(nodes[index].getClass());
                index++;
            }
            result = results;
        }
            
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private void updateSelectedNodeProperty() {
        if (canSelect()){
            List list = createListNodes(getExplorerManager().getSelectedNodes());
            selectedNodes.assignData(list);
            patternText.set(selectedNodes.getList().toString());
        }
        else
            selectedNodes.assignData(new ArrayList<Object>());
    }
    
    private List createListNodes(Node[] nodes) {
        List list = new ArrayList();
        for(Node n : nodes) {
            list.add(n);
        }
        return list;
    }
    
    private void viewSelectedNodesChange() {
        updateSelectedNodeProperty();
    }
    
    private void modelSelectedNodeChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
    }
    
    private ExplorerManager getExplorerManager() {
        return explorerManager;
    }
    
    public void installAnnounceSupport(Class tag) {
        
    }
    
    public void setSelectableClass(Class selectableClass) {
        this.selectableClass = selectableClass;
    }
    
    public void setChoiceRootNode(ChoiceRootNode node) {
        getExplorerManager().setRootContext(node);
    }
    
    protected JButton getAcceptButton() {
        return form.btnAceptar;
    }
    
    protected JButton getCancelButton() {
        return form.btnCancelar;
    }
    
    protected JPanel getForm() {
        return form;
    }
    
    @Override
    protected boolean canAcceptDialog() {
        return selectedNodes.getList().size() > 0;
    }
    
    public void setTitleForSearch(String title) {
        form.lblSearch.setText(title);
    }
    
}

