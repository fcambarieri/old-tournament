/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.ContactoPersona;
import com.tdk.domain.Persona;
import com.tdk.domain.PersonaFisica;
import com.tdk.services.PersonaServiceRemote;
import com.thorplatform.swing.SwingNode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;



/**
 *
 * @author fernando
 */
public class PersonaNode extends SwingNode<Persona>{

    public PersonaNode(Persona persona) {
        super(new PersonaNodeChildren(persona), persona);
        setIconBaseWithExtension("com/tdk/client/personas/persona-16x16.png");
    }
    
    public String getDisplay() {
        return getValue().getDisplayName();
    }

    @Override
    public Action[] getActions(boolean arg0) {
        if (getValue() instanceof PersonaFisica) {
            return  new Action[]{new ModificarPersonaFisicaAction((PersonaFisica) getValue())};
        }
        return super.getActions(arg0);
    }
}





class PersonaNodeChildren extends Children.Keys<Integer> {

    private static final Integer CONTACATOS = new Integer(0);
    private static final Integer INSTITUCIONES = new Integer(0);
    private static final Integer TORNEOS = new Integer(0);

    private Persona persona;

    public PersonaNodeChildren(Persona persona) {
        this.persona = persona;
    }

    @Override
    protected void addNotify() {
        if (persona != null) {
            ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
            PersonaServiceRemote personaService = sf.getService(PersonaServiceRemote.class);
            persona = personaService.recuperarPersona(persona.getId(), true);

            List<Integer> categorias = new ArrayList<Integer>();
            categorias.add(CONTACATOS);
            setKeys(categorias);
        }
    }

    @Override
    protected Node[] createNodes(Integer key) {
        AbstractNode node = null;
        if (key.equals(CONTACATOS)) {
           node = new ContactosNode(persona);
        }
        return new Node[]{node};
    }

}
class ContactosNode extends AbstractNode {

    public ContactosNode(Persona prs) {
        super(new ContactosNodeChildren(prs));
        setIconBaseWithExtension("com/tdk/client/personas/contacto-16x16.png");
    }

    @Override
    public String getDisplayName() {
        return "Contactos";
    }


}
class ContactosNodeChildren extends Children.Keys<ContactoPersona> {

    private Persona persona;

    public ContactosNodeChildren(Persona prs) {
        this.persona = prs;
    }

    @Override
    protected void addNotify() {
        setKeys(persona.getContactos());
    }

    @Override
    protected Node[] createNodes(ContactoPersona contacto) {
        return new Node[]{new ContactoNode(contacto)};
    }

}

class ContactoNode extends SwingNode<ContactoPersona> {

    public ContactoNode(ContactoPersona cp) {
        super(cp);
        setIconBaseWithExtension("com/tdk/client/personas/tipocontacto-16x16.png");
    }

    @Override
    public String getDisplay() {
        return getValue().getTipoContacto().getDescripcion() + " [" + getValue().getDescripcion() + "]: " + getValue().getValor();
    }

}
