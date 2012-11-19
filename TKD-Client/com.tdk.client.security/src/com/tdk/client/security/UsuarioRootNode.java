/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.Usuario;
import com.tdk.services.SecurityServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
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
public class UsuarioRootNode extends ChoiceRootNode{

    private StrategySearchPattern<? extends Usuario> strategySearch;
    
    public UsuarioRootNode() {
        super(new UsuarioRootNodeChildren());
        installActions();
        setTitleForSearch("Nombre de usuario");
    }
    
    private void installActions() {
        setStrategySearch(new StrategySearchPattern<Usuario>() {
            public List<Usuario> strategySearch(String arg0) {
                try {
                    return getSecurityService().listarUsuarios(arg0);
                } catch(Throwable ex) {
                    GuiUtils guiUtils = Lookup.getDefault().lookup(GuiUtils.class);
                    guiUtils.notificationError(ex, TDKServerException.class);
                }
                return new ArrayList<Usuario>();
            }
        });
        
        setValidatorFilter(new ValidatorFilter() {
            public boolean validate(String arg0) {
                return true;
            }
        });
    }
    
    @Override
    protected List loadKeys(String arg0) {
        if (getStrategySearch() != null)
            return getStrategySearch().strategySearch(arg0);
        throw new NullPointerException("El strategy es null");
    }

    public StrategySearchPattern getStrategySearch() {
        return strategySearch;
    }

    public void setStrategySearch(StrategySearchPattern<? extends Usuario> strategySearch) {
        this.strategySearch = strategySearch;
    }
    
    public SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarUsuarioAction()};
    }

}

class UsuarioRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[] {new UsuarioNode((Usuario) arg0)};
    }
    
}

class AgregarUsuarioAction extends AbstractSwingAction {

    public AgregarUsuarioAction() {
        super("Agregar usuario...","Usuarios", Acceso.ALTA);
    }
    
    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        UsuarioController controller = scf.createController(UsuarioController.class);
        controller.setTitle("Agregar usuario...");
        boolean error = true;
        while (error && controller.showModal()) {
            Usuario u = controller.crearUsuario();
            try {
                getSecurityService().crearUsuario(u);
                error = false;
            } catch (Throwable ex) {
                controller.getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }
    
    private SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

    public void setIsLogin(boolean isLogin) {
        setEnabled(isLogin);
    }
    
}
