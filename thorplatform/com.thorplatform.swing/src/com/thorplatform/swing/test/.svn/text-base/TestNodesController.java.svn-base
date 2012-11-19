/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.test;

import com.thorplatform.swing.DelegatingListModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.MultiChoiceField;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Fernando
 */
public class TestNodesController extends SwingModalController {

    private TestNodesForm form = new TestNodesForm();
    private final ListProperty<TestNode> nodes = new ListProperty<TestNode>("nodes");
    private final JTextField txtField = new JTextField("%");
    
    private final ListProperty<String> textos = new ListProperty<String>("textos");
    private final Property<String> textSelected = new Property<String>("text");
    
    @Override
    protected JButton getAcceptButton() {
        return form.btnAceptar;
    }

    @Override
    protected JButton getCancelButton() {
        return form.btnCancelar;
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        
        DelegatingListModel<String> model = new DelegatingListModel<String>(textos.getList());
        
        form.xlist.setModel(model);
        
        getSwingBinder().bindSingleSelectionList(form.xlist, textos, textSelected);
        
        
        MultiChoiceField<TestNode> cf = new MultiChoiceField<TestNode>(txtField, "Seleccionar texto", new TestRootNode(), TestNode.class);
        getSwingBinder().bindMultiChoiceToObjects(cf, nodes);
                
        initListeners();
    }

    private void initListeners() {
        form.btnAgregar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                agregar(e);
            }
        });
        
        form.btnQuitar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quitar();
            }
        });
    }

    private void agregar(ActionEvent e) {
        for(ActionListener al : txtField.getActionListeners())
            al.actionPerformed(e);
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        
        if (evt.getSource() == textSelected)
            form.btnQuitar.setEnabled(textSelected.get() != null);
        
        if (evt.getSource() == nodes) {
            printNodes();
        }
    }

    private void printNodes() {
        for(TestNode tn : nodes.getList()) {
            textos.add(tn.getValue());
        }
    }
    
    private void quitar() {
        if (textSelected.get() != null) {
            textos.remove(textSelected.get());
        }
    }
    
}
