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
public class FuncionalidadRootNode extends ChoiceRootNode {

    private StrategySearchPattern<? extends Funcionalidad> strategySearchFuncionalidad;
    private GuiUtils guiUtils = Lookup.getDefault().lookup(GuiUtils.class);

    public FuncionalidadRootNode() {
        super(new FuncionalidadRootNodeChildren());
        installActions();
    }
    
    public FuncionalidadRootNode(StrategySearchPattern<? extends Funcionalidad> strategySearach) {
        this();
        setStrategySearchFuncionalidad(strategySearchFuncionalidad);
    }

    private void installActions() {
        setStrategySearchFuncionalidad(new StrategySearchPattern<Funcionalidad>() {

            public List<Funcionalidad> strategySearch(String arg0) {
                try {
                    return getSecurityService().listarFuncionalidad(arg0);
                } catch (Throwable ex) {
                    guiUtils.warnnig(ex, TDKServerException.class);
                }
                return new ArrayList<Funcionalidad>();
            }
        });

        setTitleForSearch("Descripcion");

        setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String arg0) {
                return true;
            }
        });
    }

    

    public SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

    @Override
    protected List loadKeys(String arg0) {
        if (strategySearchFuncionalidad != null) {
            return strategySearchFuncionalidad.strategySearch(arg0);
        }
        throw new TDKServerException("El strategy de rol es null");
    }

    public StrategySearchPattern<? extends Funcionalidad> getStrategySearchRol() {
        return strategySearchFuncionalidad;
    }

    public void setStrategySearchFuncionalidad(StrategySearchPattern<? extends Funcionalidad> strategySearchFuncionalidad) {
        this.strategySearchFuncionalidad = strategySearchFuncionalidad;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarFuncionalidadAction()};
    }
}

class FuncionalidadRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new FuncionalidadNode((Funcionalidad) arg0)};
    }
}

class AgregarFuncionalidadAction extends AbstractSwingAction {

    public AgregarFuncionalidadAction() {
        super("Agregar funcionalidad...", "Funcionalidades", Acceso.ALTA);
    }

    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        InputController controller = scf.createController(InputController.class);
        controller.setTitle("Agregando funcionalidad...");
        controller.setLabelDescripcion("Descripcion");
        controller.setMaxLenght(new Integer("64"));
        boolean error = true;
        while (error && controller.showModal()) {
            Funcionalidad funcionalidad = new Funcionalidad(controller.getTexto().get());
            try {
                getSecurityService().crearFuncionalidad(funcionalidad);
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
