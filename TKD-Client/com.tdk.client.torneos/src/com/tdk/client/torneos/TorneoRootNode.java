/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.torneo.Torneo;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.ValidatorFilter;
import com.thorplatform.swing.actions.AbstractSwingAction;
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
public class TorneoRootNode extends ChoiceRootNode {

    private StrategySearchPattern<? extends Torneo> strategySearchTorneo;
    
    public TorneoRootNode() {
        super(new TorneoRootNodeChildren());
        installActions();
    }
    
    @Override
    protected List loadKeys(String arg0) {
        if (strategySearchTorneo != null)
            return strategySearchTorneo.strategySearch(arg0);
        
        throw new NullPointerException("El strategy es nulo");
    }

    public StrategySearchPattern<? extends Torneo> getStrategySearchTorneo() {
        return strategySearchTorneo;
    }

    public void setStrategySearchTorneo(StrategySearchPattern<? extends Torneo> strategySearchTorneo) {
        this.strategySearchTorneo = strategySearchTorneo;
    }

    private void installActions() {
        
        setTitleForSearch("Nombre");
        
        setValidatorFilter(new ValidatorFilter() {
            public boolean validate(String arg0) {
                return arg0 != null && arg0.trim().length() > 0;
            }
        });
        
        setStrategySearchTorneo(new StrategySearchPattern<Torneo>() {
            public List<Torneo> strategySearch(String arg0) {
                try {
                    return getTorneoService().listarTorneos(arg0);
                } catch(Throwable ex) {
                    
                }
                return new ArrayList<Torneo>();
            }
        });
    }
    
    public TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarTorneoAction()};
    }

    
}
class TorneoRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new TorneoNode((Torneo) arg0)};
    }
    
}

class AgregarTorneoAction extends AbstractSwingAction {

    public AgregarTorneoAction() {
        super("Agregar torneo...","Torneos", Acceso.ALTA);
    }
    
    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        TorneoController controller = scf.createController(TorneoController.class);
        controller.setTitle("Agregar torneo...");
        boolean error = true;
        while(error && controller.showModal()) {
            try {
                Torneo torneo =  controller.crearTorneo();
                torneo = getTorneoService().crearTorneo(torneo);
                error = false;
            } catch(Throwable ex) {
                controller.getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }
    
    public TorneoServiceRemote getTorneoService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(TorneoServiceRemote.class);
    }

    public void setIsLogin(boolean arg0) {
    }
    
}