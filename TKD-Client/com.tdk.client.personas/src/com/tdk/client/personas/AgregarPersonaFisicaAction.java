/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.PersonaFisica;
import com.tdk.domain.security.Acceso;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.actions.AbstractSwingAction;
import java.awt.event.ActionEvent;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class AgregarPersonaFisicaAction extends AbstractSwingAction {

    public AgregarPersonaFisicaAction() {
        super("Agregar persona fisica...", "Personas", Acceso.ALTA);
    }

    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        PersonaFisicaController controller = scf.createController(PersonaFisicaController.class);
        controller.setTitle("Agregar persona fisica...");
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                PersonaFisica personaFisica = controller.crearPersonaFisica();
                getPersonaService().crearPersona(personaFisica);
                error = false;
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
}