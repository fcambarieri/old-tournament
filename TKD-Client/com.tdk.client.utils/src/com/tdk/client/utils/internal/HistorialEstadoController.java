/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.utils.internal;

import com.tdk.domain.Estado;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingModalController;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class HistorialEstadoController<T extends Estado> extends SwingModalController {

    private final ListProperty<T> historial = new ListProperty<T>("historial");
    private final Property<Integer> index = new Property<Integer>("index");

    private JButton btnAceptar = new JButton();
    private HistorialEstadosForm form = new HistorialEstadosForm();
    private SwingControllerChangeEvent controllerChangeEvent;
    
    @Override
    protected JButton getAcceptButton() {
        return btnAceptar;
    }

    @Override
    protected JButton getCancelButton() {
        return form.btnCerrar;
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
    }

    private void configureBindings() {
        getSwingBinder().bindSingleSelectionTable(form.xtblHistorial,getHistorial(), index);
        
        setTitle("Historial");
    }

    private void configureView() {
        ListTableModel<T> model = new ListTableModel<T>();
        model.setColumnTitles(new String[]{"Tipo de estado", "Fecha desde"});
        model.setColumnClasses(new Class[]{String.class, String.class});
        model.setColumnMethodNames(new String[]{"getDisplayWithOutDate", "getDisplayDate"});
        form.xtblHistorial.setModel(model);
    }
    
    public void initHistorialEstadoController(List<T> historial) {
        this.getHistorial().assignData(historial);
    }

    public ListProperty<T> getHistorial() {
        return historial;
    }

    public SwingControllerChangeEvent getControllerChangeEvent() {
        return controllerChangeEvent;
    }

    public void setControllerChangeEvent(SwingControllerChangeEvent controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        
        if (getControllerChangeEvent() != null)
            getControllerChangeEvent().notifyEvent(evt);
    }

    
}
