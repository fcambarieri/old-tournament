/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.Persona;
import com.tdk.domain.PersonaFisica;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.ValidatorFilter;
import com.thorplatform.utils.GuiUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.Lookup;


/**
 *
 * @author fernando
 */
public class PersonaRootNode extends ChoiceRootNode  {

    private StrategySearchPattern<? extends Persona> strategySearchPersonas;
    private Lookup.Result<PersonaFisica> result;

    
    public PersonaRootNode() {
        super(new PersonaRootNodeChildren());
        installActions();
    }
    
    public PersonaRootNode(StrategySearchPattern<? extends Persona> strategySearchPersonas) {
        this();
        setStrategySearchPersonas(strategySearchPersonas);
    }

    protected List loadKeys(String arg0) {
        if (strategySearchPersonas != null) {
            return strategySearchPersonas.strategySearch(arg0);
        }
        throw new NullPointerException("No hay un strategy seteado");
    }

    private void installActions() {
        setStrategySearchPersonas(new StrategySearchPattern<Persona>() {

            public List<Persona> strategySearch(String arg0) {
                try {
                    return getPersonaService().listarPersona(arg0);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    GuiUtils guiUtils = Lookup.getDefault().lookup(GuiUtils.class);
                    guiUtils.warnnig(ex, TDKServerException.class);
                }
                return new ArrayList<Persona>();
            }
        });
        
        setValidatorFilter(new ValidatorFilter() {
            public boolean validate(String arg0) {
                return arg0 != null && arg0.length() > 0;
            }
        });
        
        setTitleForSearch("Apellido - Nombre - Contactos");
    }

    public PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

    public StrategySearchPattern<? extends Persona> getStrategySearchPersonas() {
        return strategySearchPersonas;
    }

    public void setStrategySearchPersonas(StrategySearchPattern<? extends Persona> strategySearchPersonas) {
        this.strategySearchPersonas = strategySearchPersonas;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarPersonaFisicaAction()};
    }

}
class PersonaRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    public Class associateLookup() {
        return Persona.class;
    }

    
    
    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new PersonaNode((Persona) arg0)};
    }
}