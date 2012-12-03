/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.Persona;
import com.tdk.domain.security.Acceso;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.NodeNotifier;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.actions.AbstractSwingAction;
import java.awt.event.ActionEvent;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;

/**
 *
 * @author fernando
 */
public class AgregarPersonaFisicaAction extends AbstractSwingAction implements org.openide.util.LookupListener {

    private NodeNotifier notifier;

    public AgregarPersonaFisicaAction() {
        super("Agregar persona fisica...", "Personas", Acceso.ALTA);
        notifier = Lookup.getDefault().lookup(NodeNotifier.class);
    }

    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        PersonaFisicaController controller = scf.createController(PersonaFisicaController.class);
        controller.setTitle("Agregar persona fisica...");
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                Persona personaFisica = controller.crearPersonaFisica();
                personaFisica = getPersonaService().crearPersona(personaFisica);
                error = false;
               // notifier.notify(personaFisica);
            } catch (Throwable ex) {
                controller.getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }

    public void setIsLogin(boolean arg0) {
        setEnabled(arg0);
    }

    public PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

//    private void associateLookup(AbstractLookup abstractLookup) {
//        res = abstractLookup.lookup(new Lookup.Template(Persona.class));
//        res.addLookupListener(this);
//    }

    public void resultChanged(LookupEvent le) {
        System.out.print("resutl: " + le.getSource());
    }
}