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
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public abstract class AccesosController extends SwingModalController {

    protected AccesosForm form = new AccesosForm();
    public final Property<SwingNode> funcionNode = new Property<SwingNode>("funcionNode");
    public final Property<SwingNode> rolNode = new Property<SwingNode>("rolNode");
    public final Property<SwingNode> usuarioNode = new Property<SwingNode>("usuarioNode");
    public final Property<Boolean> alta = new Property<Boolean>("Alta");
    public final Property<Boolean> baja = new Property<Boolean>("baja");
    public final Property<Boolean> modificacion = new Property<Boolean>("modificacion");
    public final Property<Boolean> consulta = new Property<Boolean>("consulta");

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
        configureValidator();
        configureBindings();
        initPresentationModel();
    }

    @SuppressWarnings("unchecked")
    private void configureBindings() {
        getSwingBinder().bindCheckBoxToBoolean(form.chkAlta, alta);
        getSwingBinder().bindCheckBoxToBoolean(form.chkModificacion, modificacion);
        getSwingBinder().bindCheckBoxToBoolean(form.chkBaja, baja);
        getSwingBinder().bindCheckBoxToBoolean(form.chkConsulta, consulta);

       
        getSwingBinder().bindChoiceToObject(getChoiceFieldFuncion(), funcionNode);
    }

    private void configureValidator() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione una funcion") {

            @Override
            public boolean validate() {
                return funcionNode.get() != null;
            }
        });
    }

    private void initPresentationModel() {
        funcionNode.set(null);
        rolNode.set(null);
        alta.set(false);
        modificacion.set(false);
        baja.set(false);
        consulta.set(false);
    }
    
    public void setEnabledControlls(boolean enabled) {
        form.txtPerfil.setEditable(enabled);
        form.txtPermiso.setEditable(enabled);
        form.chkAlta.setEnabled(enabled);
        form.chkBaja.setEnabled(enabled);
        form.chkModificacion.setEnabled(enabled);
        form.chkConsulta.setEnabled(enabled);
    }
    
    public void setEnabledTextControlls(boolean enabled) {
        form.txtPerfil.setEditable(enabled);
        form.txtPermiso.setEditable(enabled);
    }
    
    
    public void configureForUsuario() {
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione un usuario") {

            @Override
            public boolean validate() {
                return usuarioNode.get() != null;
            }
        });
        
        getSwingBinder().bindChoiceToObject(getChoiceFieldUsuario(), usuarioNode);
        
        form.lblPerfilUsuario.setText("Usuario");
        form.txtPerfil.setEditable(false);
    }
    
    @SuppressWarnings("unchecked")
    public void configureForRol() {
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione un Rol") {

            @Override
            public boolean validate() {
                return rolNode.get() != null;
            }
        });
        
        getSwingBinder().bindChoiceToObject(getChoiceFieldRol(), rolNode);
        form.txtPerfil.setEditable(false);
    }
    
    protected abstract ChoiceField<SwingNode> getChoiceFieldFuncion();
    protected abstract ChoiceField<SwingNode> getChoiceFieldUsuario();
    protected abstract ChoiceField<SwingNode> getChoiceFieldRol();
}
