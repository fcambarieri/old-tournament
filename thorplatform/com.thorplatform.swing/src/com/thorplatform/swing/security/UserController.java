/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing.security;

import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.StringPropertyValidator;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author fernando
 */
public abstract class UserController<T> extends SwingModalController {

    protected UserForm form = new UserForm();
    public final Property<String> usuarioName = new Property<String>("usuario");
    public final Property<String> password = new Property<String>("password");
    public final Property<Date> fechaDesde = new Property<Date>("fechaDesde");
    public final Property<Date> fechaHasta = new Property<Date>("fechaHasta");
    public final Property<Boolean> chkFechaHasta = new Property<Boolean>("chkFechaHasta");
    public final Property<SwingNode> rolNode = new Property<SwingNode>("rolNode");
    
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
        installValidators();
        configureView();
        configureBinding();
        initPresentationModel();
    }

    private void configureBinding() {
        getSwingBinder().bindTextComponentToString(form.txtNombreUsuario, usuarioName);
        getSwingBinder().bindTextComponentToString(form.txtPassword, password);
        getSwingBinder().bindDatePickerToDate(form.xdtpkFechaDesde, fechaDesde);
        getSwingBinder().bindDatePickerToDate(form.xdtpkFechaHasta, fechaHasta);
        getSwingBinder().bindCheckBoxToBoolean(form.chkFechaHasta, chkFechaHasta);
        
        getSwingBinder().bindChoiceToObject(getRolChoiceField(), rolNode);
    }
    
    private void configureView() {
        form.xdtpkFechaDesde.setFormats(new String[]{"dd/MM/yyyy"});
        form.xdtpkFechaHasta.setFormats(new String[]{"dd/MM/yyyy"});

    }

    private void initPresentationModel() {
        chkFechaHasta.set(false);
        usuarioName.set(null);
        password.set(null);
        fechaDesde.set(null);
        fechaHasta.set(null);
        rolNode.set(null);
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        if (evt.getSource() == chkFechaHasta && chkFechaHasta.get() != null) {
            form.xdtpkFechaHasta.setEnabled(chkFechaHasta.get());
        }
    }

    public abstract T crearUsuario() ;

    public abstract T modificarUsuario(T usuario);
    
    public abstract void initControllerForUpdate(T usuario);
    
    protected abstract ChoiceField<SwingNode> getRolChoiceField();

    protected abstract void installValidators();
    /* {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new StringPropertyValidator(usuarioName, "Ingrese un nombre de usuario (mayor de 3 caracteres)", 3, 64));
        getSwingValidator().addSwingValidator(new StringPropertyValidator(password, "Ingrese un password (mayor de 3 caracteres)", 3, 15));
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione una persona") {
            @Override
            public boolean validate() {
                return personaNode.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione un perfil") {
            @Override
            public boolean validate() {
                return rolNode.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Ingrese una fecha desde") {
            @Override
            public boolean validate() {
                return fechaDesde.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Ingrese una fecha hasta") {
            @Override
            public boolean validate() {
                return validarFechaHasta() ;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("La fecha desde tiene que menor a la fecha hasta") {
            @Override
            public boolean validate() {
                return validarFechaHasta() ? true : (fechaDesde.get() != null) && fechaDesde.get().compareTo(fechaHasta.get()) <= 0 ;
            }
        });
        
    }*/
    
    protected boolean validarFechaHasta() {
        return chkFechaHasta.get() == false || fechaHasta.get() != null;
    }
}
