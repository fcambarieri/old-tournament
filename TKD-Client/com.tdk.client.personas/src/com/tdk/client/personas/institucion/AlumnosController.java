/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas.institucion;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.personas.AgregarPersonaFisicaAction;
import com.tdk.client.personas.ModificarPersonaFisicaAction;
import com.tdk.client.personas.PersonaFisicaController;
import com.tdk.client.personas.PersonaNode;
import com.tdk.client.personas.PersonaRootNode;
import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.PersonaFisica;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.DelegatingListModel;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingListController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class AlumnosController extends SwingListController<Alumno> {

    private Institucion institucion;
    private SwingControllerChangeEvent controllerNotifier;
    private Property<PersonaNode> personaNode = new Property<PersonaNode>("persona");
    private JTextField txtPersona = new JTextField();
    private PersonaRootNode personaRootNode = new PersonaRootNode();

    @Override
    protected DelegatingListModel<Alumno> configureListModel() {
        DelegatingListModel<Alumno> model = new DelegatingListModel<Alumno>(getListProperty().getList());
        return model;
    }

    @Override
    protected Alumno agregarAction() {
        for (ActionListener al : txtPersona.getActionListeners()) {
            al.actionPerformed(new ActionEvent(txtPersona, ActionEvent.ACTION_PERFORMED, "personas"));
        }
        return agregarAlumnno();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Alumno editarAction(Alumno alumno) {
        ModificarPersonaFisicaAction action = new ModificarPersonaFisicaAction((PersonaFisica) alumno.getPersonaFisica());
        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "modificarPersona"));
        alumno.setPersonaFisica((PersonaFisica) getPersonaService().recuperarPersona(alumno.getPersonaFisica().getId(), false));
        return alumno;
    }

    @Override
    protected boolean canAcceptDialog() {
        return true;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public SwingControllerChangeEvent getControllerNotifier() {
        return controllerNotifier;
    }

    public void setControllerNotifier(SwingControllerChangeEvent controllerNotifier) {
        this.controllerNotifier = controllerNotifier;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);

        if (controllerNotifier != null) {
            controllerNotifier.notifyEvent(evt);
        }
    }

    @Override
    public void initController() {
        super.initController();
        installActions();


        ChoiceField<PersonaNode> personaChoiceField = new ChoiceField<PersonaNode>(txtPersona, "Seleccione una persona", personaRootNode, PersonaNode.class);
        personaChoiceField.addAnnounceSupportTag(PersonaFisica.class);
        getSwingBinder().bindChoiceToObject(personaChoiceField, personaNode);
    }

    private void installActions() {
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

    public Alumno agregarAlumnno() {
        Alumno alumno = null;
        if (personaNode.get() != null) {
            if (!existeAlumno((PersonaFisica) personaNode.get().getValue())) {
                alumno = new Alumno();
                alumno.setPersonaFisica((PersonaFisica) personaNode.get().getValue());
                personaNode.set(null);
                return alumno;
            } else {
                getGuiUtils().warnnig("El alumno ya existe en la institución");
            }
        }
        return alumno;
    }

    private boolean existeAlumno(PersonaFisica pf) {
        int i = 0;
        boolean existe = false;
        while (!existe && i < getListProperty().getList().size()) {
            existe = getListProperty().getList().get(i).getPersonaFisica().equals(pf);
            i++;
        }

        return existe;
    }

    private PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

    @Override
    protected void quitarAction(Alumno alumno) {
    }
}
