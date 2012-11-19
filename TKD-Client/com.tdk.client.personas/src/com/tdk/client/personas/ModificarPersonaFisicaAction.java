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
public class ModificarPersonaFisicaAction extends AbstractSwingAction {

    private PersonaFisica personaFisica;
    
    public ModificarPersonaFisicaAction(PersonaFisica personaFisica) {
        super("Modificar persona fisica...", "Personas", Acceso.MODIFICACION);
        this.personaFisica = personaFisica;
    }
    
    
    public void actionPerformed(ActionEvent arg0) {
        if (personaFisica != null) {
            personaFisica = (PersonaFisica) getPersonaService().recuperarPersona(personaFisica.getId(), true);
            
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            PersonaFisicaController controller = scf.createController(PersonaFisicaController.class);
            controller.initControllerForUpdate(personaFisica);
            
            boolean error = true;
            while(error && controller.showModal()) {
                try {
                    personaFisica = controller.modificarPersonaFisica(personaFisica);
                    getPersonaService().modificarPersona(personaFisica);
                    error = false;
                } catch (Throwable ex) {
                    controller.getGuiUtils().warnnig(ex, TDKServerException.class);
                }
            } 
            
        }
    }

    public void setIsLogin(boolean isLogin) {
        setEnabled(isLogin);
    }
    
    public PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }
}