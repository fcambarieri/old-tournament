/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.Funcionalidad;
import com.tdk.services.SecurityServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.InputController;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.actions.AbstractSwingAction;
import java.awt.event.ActionEvent;
import org.openide.nodes.Children;
import org.openide.util.Lookup;





/**
 *
 * @author fernando
 */
public class FuncionalidadNode extends SwingNode<Funcionalidad> {

    
    public FuncionalidadNode(Funcionalidad funcionalidad) {
        super(Children.LEAF, funcionalidad);
        setIconBaseWithExtension("com/tdk/client/security/permisos-16x16.png");
                
    }


    @Override
    public String getDisplay() {
        return getValue().getDescripcion();
    }
    
}
/********** ACTIONS ************/

class ModificarFuncionalidadAction extends AbstractSwingAction {

    private Funcionalidad funcionalidad;

    public ModificarFuncionalidadAction(Funcionalidad funcionalidad) {
        super("Modificar funcionalidad", "Funcionalidades", Acceso.MODIFICACION);
        this.funcionalidad = funcionalidad;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (funcionalidad != null) {
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            InputController controller = scf.createController(InputController.class);
            controller.setTitle("Modificar funcionalidad " + funcionalidad.getDescripcion());
            controller.setLabelDescripcion("Descripcion");
            controller.setMaxLenght(new Integer("64"));
            controller.getTexto().set(funcionalidad.getDescripcion());
            boolean error = true;
            while (error && controller.showModal()) {
                funcionalidad.setDescripcion(controller.getTexto().get());
                try {
                    getSecurityService().modificarFuncionalidad(funcionalidad);
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

    public SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

}

