/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public abstract class SwingListController<T> extends SwingController {

    private SwingListForm form = new SwingListForm();
    
    private final ListProperty<T> listProperty = new ListProperty<T>("listProperty");
    private final Property<T> propertySelected = new Property<T>("propertySelected");
    
    private SwingControllerChangeEvent swingControllerChangeEvent;

    @Override
    protected JPanel getForm() {
        return form;
    }

    public ListProperty<T> getListProperty() {
        return listProperty;
    }

    public Property<T> getPropertySelected() {
        return propertySelected;
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
        installActions();
    }
    
    private void configureView() {
        form.jxList.setModel(configureListModel());
    }
    
    private void configureBindings() {
        getSwingBinder().bindSingleSelectionList(form.jxList, listProperty, propertySelected);
    }
    
    private void installActions() {
        form.btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                T newValue = agregarAction();
                if (newValue != null) {
                    getListProperty().add(newValue);
                }
                    
            }
        });
        
        form.btnQuitar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                quitarAction();
            }
        });
        
        form.btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (propertySelected.get() != null) {
                    T value = editarAction(propertySelected.get());
                    if (value != null) {
                        form.jxList.repaint();
                    }
                }
                    
            }
        });
    }
    
    private void quitarAction() {
        if (propertySelected.get() != null) {
            T objectSelected = propertySelected.get();
            if (quitarAction(objectSelected)) {
                listProperty.remove(objectSelected);    
            }
        }
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        if (evt.getSource() == listProperty) {
            boolean enabled = !listProperty.getList().isEmpty();
            form.btnQuitar.setEnabled(enabled);
            form.btnEditar.setEnabled(enabled);
        }
       
        if (getSwingControllerChangeEvent() != null) {
            getSwingControllerChangeEvent().notifyEvent(evt);
        }
    }
    
    protected JButton getAgregarButton() {
        return form.btnAgregar;
    }
    
    protected JButton getEditarButton() {
        return form.btnEditar;
    }
    
    public JPanel getPanel() {
        return getForm();
    }
    
    protected abstract DelegatingListModel<T> configureListModel();

    protected abstract T agregarAction();
    
    protected abstract T editarAction(T objectSelected);
    
    /**
     *@return true if it has to be deleted from the list
     */
    protected abstract boolean quitarAction(T objectSelected);

    public SwingControllerChangeEvent getSwingControllerChangeEvent() {
        return swingControllerChangeEvent;
    }

    public void setSwingControllerChangeEvent(SwingControllerChangeEvent swingControllerChangeEvent) {
        this.swingControllerChangeEvent = swingControllerChangeEvent;
    }
   
}
