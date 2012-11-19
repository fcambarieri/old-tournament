/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.utils;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.utils.internal.*;
import com.tdk.domain.Estado;
import com.tdk.services.UtilServiceRemote;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 * 
 * Esta clase ademite 2 tipos de objetos cuando se lo crea. El valor T
 * esta asociado con el estado y el valor E con el tipo de estado.
 * El valor E tiene que ser si o si un enum.
 */
public class EstadoController<T extends Estado, E> extends SwingController implements SwingControllerChangeEvent{

    private Property<E> tipoEstadoSelected = new Property<E>("estadoSelected");
    private ListProperty<E> tipoEstados = new ListProperty<E>("estados");
    private Property<String> fechaDesde = new Property<String>("fechaDesde");
    private EstadoForm form = new EstadoForm();
    private SwingControllerChangeEvent controllerChangeEvent = null;
    private HistorialEstadoController<T> historialEstadoController = null;

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        if (evt.getSource() == historialEstadoController.getHistorial())
            form.btnHistorial.setEnabled(historialEstadoController.getHistorial().getList().size() > 0);

        if (controllerChangeEvent != null) {
            controllerChangeEvent.notifyEvent(evt);
        }
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
        installActions();
        initPresentationModel();
    }

    private void configureBindings() {
        getSwingBinder().bindComboBoxToObject(form.cboTipoEstado, tipoEstadoSelected, tipoEstados);
        getSwingBinder().bindTextComponentToString(form.txtFechaDesde, fechaDesde);
    }

    @SuppressWarnings("unchecked")
    private void configureView() {
        DelegatingComboBoxModel<E> model = new DelegatingComboBoxModel<E>(tipoEstados.getList());
        form.cboTipoEstado.setModel(model);

        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        historialEstadoController = scf.createController(HistorialEstadoController.class);
        historialEstadoController.setControllerChangeEvent(this);
        
        form.txtFechaDesde.setEnabled(false);
    }

    public void setControllerChangeEvent(SwingControllerChangeEvent controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }

    private void initPresentationModel() {
        tipoEstadoSelected.set(null);
    }

    private void installActions() {
        form.btnHistorial.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                showHistorial();
            }
        });
    }

    private void showHistorial() {
        historialEstadoController.showModal();
    }
    
    public void initEstadoController(List<E> tiposEstados) {
        tipoEstados.assignData(tiposEstados);
    }
    
    @SuppressWarnings("unchecked")
    public void initEstadoController(T estado, List<T> estados) {
     initEstadoController((E) estado.getTipoEstado(), estados, estado.getDisplayDate());   
    }
    
    public void initEstadoController(E tipoEstado, List<T> estados, String strFechaDesde) {
        tipoEstadoSelected.set(tipoEstado);
        historialEstadoController.initHistorialEstadoController(estados);
        this.fechaDesde.set(strFechaDesde);
    }
    
    public JPanel getPanel() {
        return getForm();
    }
    
    public E getTipoEstado() {
        return tipoEstadoSelected.get();
    }
    
    public Date getFechaDesde() {
        return getUtilService().getDiaHora();
    }
    
    private UtilServiceRemote getUtilService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(UtilServiceRemote.class);
    }

    public void notifyEvent(PropertyChangeEvent arg0) {
        propertyChange(arg0);
    }
}
