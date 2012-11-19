/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.institucion;

import com.tdk.client.personas.ContactosPesonalesController;
import com.tdk.domain.Alumno;
import com.tdk.domain.ContactoPersona;
import com.tdk.domain.Institucion;
import com.tdk.domain.Profesor;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.StringPropertyValidator;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class InstitucionController extends SwingModalController implements SwingControllerChangeEvent {

    private InstitucionForm form = new InstitucionForm();
    private final Property<String> nombre = new Property<String>("nombres");
    private AlumnosController alumnosController;
    private ProfesoresListController profesoresController;
    private ContactosPesonalesController contactosController;
    
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
        installActions();
        configureView();
        configureBindings();
        initPresentationModel();
    }

    private void configureBindings() {
        getSwingBinder().bindTextComponentToString(form.txtNombre, nombre);
    }

    private void configureView() {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        alumnosController = scf.createController(AlumnosController.class);
        form.pnlAlumnos.add(alumnosController.getPanel(), BorderLayout.CENTER);
        alumnosController.setControllerNotifier(this);
        
        profesoresController = scf.createController(ProfesoresListController.class);
        form.pnlProfesores.add(profesoresController.getPanel(), BorderLayout.CENTER);
        profesoresController.setControllerNotifier(this);
        
        contactosController = scf.createController(ContactosPesonalesController.class);
        form.pnlContactos.add(contactosController.getPanel(), BorderLayout.CENTER);
        contactosController.setControllerNotifier(this);
    }

    private void initPresentationModel() {
        nombre.set(null);
    }

    private void installActions() {
        getSwingValidator().addSwingValidator(new StringPropertyValidator(nombre, "Ingrese el nombre de la institución", new Integer(1), new Integer(64)));
    }

    public void initForUpdate(Institucion institucion) {
        nombre.set(institucion.getNombre());
        alumnosController.setInstitucion(institucion);
        alumnosController.getListProperty().assignData(institucion.getCompetidores());
        profesoresController.getListProperty().assignData(institucion.getProfesores());
        contactosController.getTableList().assignData(institucion.getContactos());
    }
    
    public Institucion crearInstitucion() {
        return modificarInstitucion(new Institucion());
    }
    
    public Institucion modificarInstitucion(Institucion institucion) {
        institucion.setNombre(nombre.get());
        
        institucion.setCompetidores(alumnosController.getListProperty().getList());
        for(Alumno a : institucion.getCompetidores()) {
            a.setInstitucion(institucion);
        }
        
        institucion.setProfesores(profesoresController.getListProperty().getList());
        for(Profesor p : institucion.getProfesores()) {
            p.setInstitucion(institucion);
        }
        
        institucion.setContactos(contactosController.getTableList().getList());
        for(ContactoPersona cp : institucion.getContactos()) {
            cp.setPersona(institucion);
        }
        
        return institucion;
    }

    public void notifyEvent(PropertyChangeEvent evt) {
        propertyChange(evt);
    }

}
