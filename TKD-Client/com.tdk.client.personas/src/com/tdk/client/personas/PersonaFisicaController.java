/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas;

import com.tdk.domain.ContactoPersona;
import com.tdk.domain.PersonaFisica;
import com.tdk.domain.Sexo;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.StringPropertyValidator;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class PersonaFisicaController extends SwingModalController{

    private PersonaFisicaForm form = new PersonaFisicaForm();
    private ContactosPesonalesController contactosController;
    
    
    public final Property<String> apellido = new Property<String>("apellido");
    public final Property<String> nombre = new Property<String>("nombre");
    public final Property<String> nroDocumentacion = new Property<String>("nroDocumentacion");
    public final Property<Sexo> sexo = new Property<Sexo>("sexo");
    public final ListProperty<Sexo> sexoList = new ListProperty<Sexo>("sexoList");
    public final Property<Date> fechaNac = new Property<Date>("fechaNac");
    
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
        configureView();
        configureBinding();
        initPresentationModel();
    }

    private void configureBinding() {
        getSwingBinder().bindTextComponentToString(form.txtApellido, apellido);
        getSwingBinder().bindTextComponentToString(form.txtNombre, nombre);
        getSwingBinder().bindComboBoxToObject(form.cboSexo, sexo, sexoList);
        getSwingBinder().bindDatePickerToDate(form.xdtpFechaNac, fechaNac);
    }

    private void configureValidator() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new StringPropertyValidator(apellido, "Ingrese un apellido", 1, 30));
        getSwingValidator().addSwingValidator(new StringPropertyValidator(nombre, "Ingrese un nombre", 1, 30));
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione el tipo de sexo") {
            @Override
            public boolean validate() {
                return sexo.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Ingrese una fecha de Nacimiento") {
            @Override
            public boolean validate() {
                return fechaNac.get() != null;
            }
        });
    }

    private void configureView() {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        contactosController = scf.createController(ContactosPesonalesController.class);
        form.pnlContactos.setLayout(new BorderLayout());
        form.pnlContactos.add(contactosController.getPanel(), BorderLayout.CENTER);
        sexoList.assignData(Arrays.asList(Sexo.values()));
        form.cboSexo.setModel(new DelegatingComboBoxModel<Sexo>(sexoList.getList()));
        form.xdtpFechaNac.setFormats(new String[]{"dd/MM/yyyy"});
    }

    private void initPresentationModel() {
        apellido.set(null);
        nombre.set(null);
        sexo.set(null);
        fechaNac.set(null);
    }
    
    
    public PersonaFisica crearPersonaFisica() {
        return modificarPersonaFisica(new PersonaFisica());
    }

    public PersonaFisica modificarPersonaFisica(PersonaFisica personaFisica) {
        personaFisica.setApellido(apellido.get());
        personaFisica.setNombre(nombre.get());
        personaFisica.setSexo(sexo.get());
        personaFisica.setContactos(contactosController.getTableList().getList());
        for(ContactoPersona cp : personaFisica.getContactos()) {
            cp.setPersona(personaFisica);
        }
        personaFisica.setFechaNacimiento(fechaNac.get());
        return personaFisica;
    }
    
    public void initControllerForUpdate(PersonaFisica persona) {
        setTitle("Editando a " + persona.getDisplayName());
        apellido.set(persona.getApellido());
        nombre.set(persona.getNombre());
        contactosController.getTableList().assignData(persona.getContactos());
        sexo.set(persona.getSexo());
        fechaNac.set(persona.getFechaNacimiento());
    }

}
