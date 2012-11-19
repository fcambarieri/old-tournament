/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.institucion;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.Institucion;
import com.tdk.domain.security.Acceso;
import com.tdk.services.PersonaServiceRemote;
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
public class InstitucionRootNode extends ChoiceRootNode{

    private StrategySearchPattern<? extends Institucion> strategySearchInstitucion;
    
    public InstitucionRootNode() {
        super(new InstitucionRootNodeChildren());
        installActions();
    }
    
    public InstitucionRootNode(StrategySearchPattern<? extends Institucion> strategySearchInstitucion) {
        this();
        setStrategySearchInstitucion(strategySearchInstitucion);
    }
    
    @Override
    protected List loadKeys(String arg0) {
        if (strategySearchInstitucion != null)
            return strategySearchInstitucion.strategySearch(arg0);
        
        throw new NullPointerException("El strategy es nulo");
    }

    public StrategySearchPattern<? extends Institucion> getStrategySearchInstitucion() {
        return strategySearchInstitucion;
    }

    public void setStrategySearchInstitucion(StrategySearchPattern<? extends Institucion> strategySearchInstitucion) {
        this.strategySearchInstitucion = strategySearchInstitucion;
    }

    private void installActions() {
        setTitleForSearch("Nombre - Contactos - Alumnos - Profesores");
        setStrategySearchInstitucion(new StrategySearchPattern<Institucion>() {
            public List<Institucion> strategySearch(String arg0) {
                try {
                    return getPersonaService().listarInstitucion(arg0);
                } catch(Throwable ex) {
                    GuiUtils guiUtils = Lookup.getDefault().lookup(GuiUtils.class);
                    guiUtils.warnnig(ex,TDKServerException.class);
                }
                return new ArrayList<Institucion>();
            }
        });
        
        setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String arg0) {
                return arg0 != null && arg0.trim().length() > 0;
            }
        });
    }

    public PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarInstitucionAction()};
    }
    
    
}
class InstitucionRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new InstitucionNode((Institucion) arg0)};
    }
    
}

/*****************************************************************************
 ********************************* ACTIONS ***********************************
 ******************************************************************************/
class AgregarInstitucionAction extends AbstractSwingAction {

    public AgregarInstitucionAction() {
        super("Agregar institución", "Instituciones", Acceso.ALTA);
    }
    
    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        InstitucionController controller = scf.createController(InstitucionController.class);
        controller.setTitle("Agregar institución...");
        boolean error = true;
        while(error && controller.showModal()) {
            Institucion it = controller.crearInstitucion();
            try {
                getPersonaService().crearInstitucion(it);
                error = false;
            } catch(Throwable ex) {
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
