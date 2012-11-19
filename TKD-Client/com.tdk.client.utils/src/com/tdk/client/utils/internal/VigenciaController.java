/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.utils.internal;

import com.tdk.client.utils.VigenciaInterface;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.RequiredPropertyValidator;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class VigenciaController extends SwingController implements VigenciaInterface{
    
    private VigenciaForm form = new VigenciaForm();
    
    private final Property<Date> fechaDesde = new Property<Date>("fechaDesde");
    private final Property<Date> fechaHasta = new Property<Date>("fechaHasta");
    private final Property<Boolean> chkFechaHasta = new Property<Boolean>("chkFechaHasta");
    
    private SwingControllerChangeEvent controllerChangeEvent;

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        if (controllerChangeEvent != null)
            controllerChangeEvent.notifyEvent(evt);
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        confgiureBindings();
        initPresentationModel();
    }

    private void confgiureBindings() {
        getSwingBinder().bindDatePickerToDate(form.xdtpFechaDesde, fechaDesde);
        getSwingBinder().bindDatePickerToDate(form.xdtpFechaHasta, fechaHasta);
        getSwingBinder().bindCheckBoxToBoolean(form.chkFechaHasta, chkFechaHasta);
    }

    private void configureView() {
        form.xdtpFechaDesde.setFormats(new String[]{"dd/MM/yyyy"});
        form.xdtpFechaHasta.setFormats(new String[]{"dd/MM/yyyy"});
    }

    private void initPresentationModel() {
        fechaDesde.set(null);
        fechaHasta.set(null);
        chkFechaHasta.set(false);
    }
    
    public void initForUpdate(Date fechaDesde, Date fechaHasta) {
        this.fechaDesde.set(fechaDesde);
        this.fechaHasta.set(fechaHasta);
    }
    
    public void initForConsulta(Date fechaDesde, Date fechaHasta) {
        initForUpdate(fechaDesde, fechaHasta);
        form.xdtpFechaDesde.setEnabled(false);
        form.xdtpFechaHasta.setEnabled(false);
        form.chkFechaHasta.setEnabled(false);
    }
    
    public Date getFechaDesde() {
        return fechaDesde.get();
    }
    
    public Date getFechaHasta() {
        return chkFechaHasta.get() ? fechaHasta.get() : null;
    }
    
    
    public AbstractSwingValidator[] getValidators() {
        return new AbstractSwingValidator[]{new RequiredPropertyValidator(fechaDesde, "Ingrese una fecha desde")
        , getFechaHastaValidator(), getFechasValidator()};
    }
    
    private AbstractSwingValidator getFechaHastaValidator() {
        return new AbstractSwingValidator("Ingrese una fecha hasta") {
            @Override
            public boolean validate() {
                return chkFechaHasta.get() == false || fechaHasta.get() != null;
            }
        };
    }
    
    private AbstractSwingValidator getFechasValidator() {
        return new AbstractSwingValidator("La fecha desde tiene que ser menor o igual a la fecha hasta") {
            @Override
            public boolean validate() {
                return chkFechaHasta.get() == false || 
                        (fechaHasta.get() != null && fechaDesde.get() != null && fechaDesde.get().compareTo(fechaHasta.get()) < 0);
            }
        };
    }

    public void setControllerChangeEvent(SwingControllerChangeEvent controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }

    public JPanel getPanel() {
        return getForm();
    }
}
