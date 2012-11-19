/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.ContactoPersona;
import com.tdk.domain.TipoContacto;
import com.tdk.services.PersonaServiceRemote;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.StringPropertyValidator;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class ContactoPersonaController extends SwingModalController {

    private ContactoPersonaForm form = new ContactoPersonaForm();
    private final ListProperty<TipoContacto> tiposContactosList = new ListProperty<TipoContacto>("tiposContactos");
    private final Property<TipoContacto> tipoContactoSelected = new Property<TipoContacto>("tipoContactoSelected");
    private final Property<String> value = new Property<String>("value");
    private final Property<String> descripcion = new Property<String>("descripcion");
    
    
    
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
        configureBindings();
        initPresentationModel();
    }

    private void configureBindings() {
        getSwingBinder().bindComboBoxToObject(form.cboTipoContacto, tipoContactoSelected, tiposContactosList);
        getSwingBinder().bindTextComponentToString(form.txtDescripcion, descripcion);
        getSwingBinder().bindTextComponentToString(form.txtValor, value);
    }

    private void configureView() {
        DelegatingComboBoxModel<TipoContacto> model = new DelegatingComboBoxModel<TipoContacto>(tiposContactosList.getList());
        form.cboTipoContacto.setModel(model);
    }

    private void initPresentationModel() {
        setTitle("Agregar contacto personal");
        tiposContactosList.assignData(getPersonaService().listarTipoContacto("%"));
        value.set(null);
        descripcion.set(null);
    }
    
    private PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

    private void installValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione un tipo de contacto") {
            @Override
            public boolean validate() {
                return tipoContactoSelected.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new StringPropertyValidator(value, "Ingrese un valor", new Integer(3), new Integer(64)));
    }
    
    public void initControllerForUpdate(ContactoPersona contactoPersona) {
        setTitle("Editar contacto de " + contactoPersona.getPersona().getDisplayName());
        value.set(contactoPersona.getValor());
        descripcion.set(contactoPersona.getDescripcion());
        tipoContactoSelected.set(contactoPersona.getTipoContacto());
    }
    
    public ContactoPersona crearContactoPersona() {
        return modificarContactoPersona(new ContactoPersona());
    }

    public ContactoPersona modificarContactoPersona(ContactoPersona contactoPersona) {
        contactoPersona.setDescripcion(descripcion.get());
        contactoPersona.setTipoContacto(tipoContactoSelected.get());
        contactoPersona.setValor(value.get());
        return contactoPersona;
    }
    
}
