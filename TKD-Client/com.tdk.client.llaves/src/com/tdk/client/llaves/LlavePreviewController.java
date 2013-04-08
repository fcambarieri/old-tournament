/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import com.tdk.client.nodes.BNode;
import com.tdk.client.nodes.BinaryNodeFactory;
import com.tdk.client.nodes.BinaryTree;
import com.tdk.client.swing.node.JBTree;
import com.tdk.domain.torneo.Competidor;
import com.thorplatform.swing.SwingController;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.JPanel;
import org.openide.windows.TopComponent;

/**
 *
 * @author sabon
 */
public class LlavePreviewController extends SwingController {

    private JBTree treePanel = new JBTree();
    private BinaryTree<Competidor> competidores = new BinaryTree<Competidor>();

    @Override
    protected JPanel getForm() {
        return treePanel;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent pce) {
    }

    @Override
    public void initController() {
        super.initController(); //To change body of generated methods, choose Tools | Templates.
        configureView();
    }

    private void configureView() {
        treePanel.setModel(competidores);
        competidores.setBinaryNodeFactory(new BinaryNodeFactory<Competidor>() {
            public BNode createBNode(Competidor value) {
                return new CompetidorBNode(value);
            }
        });
    }

    public void setCompetidores(List<Competidor> competidores) {
        this.competidores.convertListToBinaryTree(competidores);
    }
    
    public void showOnTopComponent(String title) {
        treePanel.setTitle(title);
        TopComponent win = new TopComponent();
        win.setDisplayName(title);
        win.setLayout(new BorderLayout());
        win.add(treePanel, BorderLayout.CENTER);
        win.requestFocus();
        win.open();
    }
}
