/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.institucion;

import com.tdk.client.personas.PersonaNode;
import com.tdk.client.personas.PersonaRootNode;
import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.Persona;
import com.tdk.domain.PersonaFisica;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class AlumnoController extends SwingModalController {

    private AlumnoForm form = new AlumnoForm();
    
    private final Property<PersonaNode> personaNode = new Property<PersonaNode>("persona");
    private final Property<InstitucionNode> institucionNode = new Property<InstitucionNode>("institucionNode");
    
    private PersonaRootNode personaRootNode = new PersonaRootNode();
    
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
        configureBindings();
    }

    private void configureBindings() {
        ChoiceField<PersonaNode> personaChoiceField = new ChoiceField<PersonaNode>(form.txtPersona, "Seleccione una persona", personaRootNode, PersonaNode.class);
        personaChoiceField.addAnnounceSupportTag(PersonaFisica.class);
        getSwingBinder().bindChoiceToObject(personaChoiceField, personaNode);
        
        ChoiceField<InstitucionNode> institucionChoiceField = new ChoiceField<InstitucionNode>(form.txtInstitucion, "Seleccione una institución", new InstitucionRootNode(), InstitucionNode.class);
        institucionChoiceField.addAnnounceSupportTag(Institucion.class);
        getSwingBinder().bindChoiceToObject(institucionChoiceField, institucionNode);
    }

    private void installActions() {
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Selecione una persona") {
            @Override
            public boolean validate() {
                return personaNode.get() != null;
            }
        });
        
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Selecione una institución") {
            @Override
            public boolean validate() {
                return institucionNode.get() != null;
            }
        });
        
        personaRootNode.setStrategySearchPersonas(new StrategySearchPattern<PersonaFisica>() {
            public List<PersonaFisica> strategySearch(String arg0) {
                try {
                    return personaRootNode.getPersonaService().listarPersonaFisica(arg0);
                } catch (Throwable ex) {
                    getGuiUtils().warnnig(ex, TDKServerException.class);
                }
                return new ArrayList<PersonaFisica>();
            }
        });
    }
    
    public Alumno crearAlumno() {
        return modificarAlumno(new Alumno());
    }
    
    public Alumno modificarAlumno(Alumno alumno) {
        alumno.setInstitucion(institucionNode.get().getValue());
        alumno.setPersonaFisica((PersonaFisica) personaNode.get().getValue());
        return alumno;
    }

    public void initForUpdate(Alumno alumno) {
        institucionNode.set(new InstitucionNode(alumno.getInstitucion()));
        personaNode.set(new PersonaNode(alumno.getPersonaFisica()));
    }
    
    public void initForAgregarAlumno(Institucion institucion) {
        institucionNode.set(new InstitucionNode(institucion));
        form.txtInstitucion.setEnabled(false);
    }
}
