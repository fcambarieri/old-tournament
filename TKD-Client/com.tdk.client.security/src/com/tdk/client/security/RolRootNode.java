/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.security;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.Rol;
import com.tdk.services.SecurityServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import com.thorplatform.swing.InputController;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.ValidatorFilter;
import com.thorplatform.swing.actions.AbstractSwingAction;
import com.thorplatform.utils.GuiUtils;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class RolRootNode extends ChoiceRootNode {

    private StrategySearchPattern<? extends Rol> strategySearchRol;
    private GuiUtils guiUtils = Lookup.getDefault().lookup(GuiUtils.class);

    public RolRootNode() {
        super(new RolRootNodeChildren());
        installActions();
    }

    private void installActions() {
        setStrategySearchRol(new StrategySearchPattern<Rol>() {

            public List<Rol> strategySearch(String arg0) {
                try {
                    return getSecurityService().listarRoles(arg0);
                } catch (Throwable ex) {
                    guiUtils.warnnig(ex, TDKServerException.class);
                }
                return new ArrayList<Rol>();
            }
        });

        setTitleForSearch("Descripcion");

        setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String arg0) {
                return true;
            }
        });
    }

    public RolRootNode(StrategySearchPattern<? extends Rol> strategySearach) {
        this();
        setStrategySearchRol(strategySearchRol);
    }

    public SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

    @Override
    protected List loadKeys(String arg0) {
        if (strategySearchRol != null) {
            return strategySearchRol.strategySearch(arg0);
        }
        throw new TDKServerException("El strategy de rol es null");
    }

    public StrategySearchPattern<? extends Rol> getStrategySearchRol() {
        return strategySearchRol;
    }

    public void setStrategySearchRol(StrategySearchPattern<? extends Rol> strategySearchRol) {
        this.strategySearchRol = strategySearchRol;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarRolAction()};
    }
}

class RolRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new RolNode((Rol) arg0)};
    }
}

class AgregarRolAction extends AbstractSwingAction {

    public AgregarRolAction() {
        super("Agregar rol...", "Roles", Acceso.ALTA);
    }

    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        InputController controller = scf.createController(InputController.class);
        controller.setTitle("Agregando rol...");
        controller.setLabelDescripcion("Descripcion");
        controller.setMaxLenght(new Integer("64"));
        boolean error = true;
        while (error && controller.showModal()) {
            Rol rol = new Rol(controller.getTexto().get());
            try {
                getSecurityService().crearRol(rol);
                error = false;
            } catch (Throwable ex) {
                controller.getGuiUtils().warnnig(ex, TDKServerException.class);
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
