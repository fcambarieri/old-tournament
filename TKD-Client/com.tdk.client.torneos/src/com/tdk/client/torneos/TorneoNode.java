/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor/
 */
package com.tdk.client.torneos;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.torneos.categorias.node.TorneoCategoriaCompetediorNode;
import com.tdk.client.torneos.competidor.CompetidorController;
import com.tdk.client.torneos.competidor.CompetidorNode;
import com.tdk.domain.Institucion;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.TipoEstadoTorneo;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.NodeBuilderFactory;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingBinder;
import com.thorplatform.swing.SwingBinderFactory;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.actions.AbstractSwingAction;
import com.thorplatform.utils.GuiUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JTextField;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class TorneoNode extends SwingNode<Torneo> {

    public TorneoNode(Torneo torneo) {
        super(new TorneoNodeChildren(torneo), torneo);
        setIconBaseWithExtension("com/tdk/client/torneos/torneo-14x16.png");
    }

    @Override
    public String getDisplay() {
        String display = getValue().getNombre();
        display += " - Estado[" + getValue().getEstadoTorneo().getDisplayWithOutDate() + "]";
        return display;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new ModificarTorneoAction(getValue()), new RegistrarCompetidoresEnTorneoAction(getValue())};
    }
}

class ModificarTorneoAction extends AbstractSwingAction {

    private Torneo torneo;

    public ModificarTorneoAction(Torneo torneo) {
        super("Modificar torneo...", "Torneos", Acceso.ALTA);
        this.torneo = torneo;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (torneo != null) {
            if (torneo.getEstadoTorneo().getTipoEstadoTorneo().equals(TipoEstadoTorneo.CANCELADO) ||
                    torneo.getEstadoTorneo().getTipoEstadoTorneo().equals(TipoEstadoTorneo.FINALIZADO)) {
                Lookup.getDefault().lookup(GuiUtils.class).warnnig("No se puede modificar un torneo CANCELADO o FINALIZADO");
            } else {
                torneo = getTorneoService().recuperarTorneo(torneo.getId(), true);
                SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
                TorneoController controller = scf.createController(TorneoController.class);
                controller.setTitle("Modificar torneo...");
                controller.initTorneoController(torneo);
                boolean error = true;
                while (error && controller.showModal()) {
                    try {
                        torneo = controller.modificarTorneo(torneo);
                        getTorneoService().modificarTorneo(torneo);
                        error = false;
                    } catch (Throwable ex) {
                        controller.getGuiUtils().warnnig(ex, TDKServerException.class);
                    }
                }
            }

        }
    }

    public TorneoServiceRemote getTorneoService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(TorneoServiceRemote.class);
    }

    public void setIsLogin(boolean arg0) {
    }
}

class RegistrarCompetidoresEnTorneoAction extends AbstractSwingAction {

    private Torneo torneo;

    public RegistrarCompetidoresEnTorneoAction(Torneo torneo) {
        super("Registrar competidores", "Torneos", Acceso.MODIFICACION);
        this.torneo = torneo;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (torneo != null) {
            RegistrarCompatidoresTorneoTopComponent win = RegistrarCompatidoresTorneoTopComponent.findInstance();
            win.setTorneo(torneo);
            win.open();
            win.requestActive();
        }
    }

    public void setIsLogin(boolean arg0) {
    }
}

class TorneoNodeChildren extends Children.Keys {

    private static final Integer INSTITUCIONES = new Integer(0);
    private static final Integer CATEGORIAS = new Integer(1);
    private List<Integer> categorias = null;
    private Torneo torneo;

    public TorneoNodeChildren(Torneo torneo) {
        this.torneo = torneo;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        if (torneo != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(INSTITUCIONES);
            categorias.add(CATEGORIAS);
            setKeys(categorias);
        }
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        AbstractNode node = null;
        Integer key = (Integer) arg0;
        if (key.equals(INSTITUCIONES)) {
            node = new TorneoInstitucionesNode(torneo);
        } else if (key.equals(CATEGORIAS)) {
            node = new TorneoCategoriaCompetediorNode(torneo);
        }

        return new Node[]{node};
    }
}

class TorneoInstitucionesNode extends AbstractNode {

    private Torneo torneo;

    public TorneoInstitucionesNode(Torneo torneo) {
        super(new TorneoInstitucionesNodeChildren(torneo));
        setIconBaseWithExtension("com/tdk/client/personas/institucion-16x16.png");
        this.torneo = torneo;
    }

    @Override
    public String getDisplayName() {
        return "Instituciones y alumnos inscriptos";
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarInstitucionTorneoAction(torneo)};
    }
}

class TorneoInstitucionesNodeChildren extends Children.Keys {

    private Torneo torneo;

    public TorneoInstitucionesNodeChildren(Torneo torneo) {
        this.torneo = torneo;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        PersonaServiceRemote personaService = sf.getService(PersonaServiceRemote.class);
        setKeys(personaService.listarInstitucionPorTorneo(torneo.getId(), "%"));
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new TorneoInsititucionNode((TorneoInstitucion) arg0)};
    }
}

class TorneoInsititucionNode extends SwingNode<TorneoInstitucion> {

    private Institucion institucion;
    private TorneoInstitucion ti;

    public TorneoInsititucionNode(TorneoInstitucion torneoInstitucion) {
        super(new CompetidoresNodeChildren(torneoInstitucion), torneoInstitucion);
        setIconBaseWithExtension("com/tdk/client/personas/institucion-16x16.png");
        this.ti = torneoInstitucion;
        institucion = getPersonaService().recuperarInstitucion(torneoInstitucion.getInstitucion().getId(), true);
    }

    @Override
    public String getDisplay() {
        if (institucion.getProfesores() != null && !institucion.getProfesores().isEmpty()) {
            return institucion.getDisplayName() + " (Prof: " + institucion.getProfesores().get(0).getPersonaFisica().getDisplayName() + ")";
        }
        return institucion.getDisplayName();
    }

    private PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarCompetidorTorneoAction(ti)};
    }
}

class CompetidoresNodeChildren extends Children.Keys {

    private TorneoInstitucion torneoInstitucion;

    public CompetidoresNodeChildren(TorneoInstitucion torneoInstitucion) {
        this.torneoInstitucion = torneoInstitucion;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        TorneoServiceRemote torneoService = sf.getService(TorneoServiceRemote.class);
        setKeys(torneoService.listarCompetidoresPorTorneoEInstitucion(torneoInstitucion.getTorneo().getId(), torneoInstitucion.getInstitucion().getId(), "%"));
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new CompetidorNode((Competidor) arg0)};
    }
}

class AgregarInstitucionTorneoAction extends AbstractSwingAction {

    private JTextField txtField = new JTextField();
    private SwingBinderFactory binderFactory = Lookup.getDefault().lookup(SwingBinderFactory.class);
    private NodeBuilderFactory builderFactory = Lookup.getDefault().lookup(NodeBuilderFactory.class);
    private NodeFactory<Institucion> nodeFactory = null;
    private Property<SwingNode<Institucion>> institucionNode = new Property<SwingNode<Institucion>>("ins");

    @SuppressWarnings("unchecked")
    public AgregarInstitucionTorneoAction(final Torneo torneo) {
        super("Agregar institución al torneo", "Torneos", Acceso.ALTA);
        nodeFactory = builderFactory.createNodeFactory(Institucion.class);

        institucionNode.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() != null) {
                    TorneoInstitucion ti = new TorneoInstitucion();
                    ti.setInstitucion(institucionNode.get().getValue());
                    ti.setTorneo(torneo);
                    try {
                        getPersonaService().crearTorneoInstitucion(ti);
                    } catch (Throwable ex) {
                        Lookup.getDefault().lookup(GuiUtils.class).warnnig(ex, TDKServerException.class);
                    }
                }
            }
        });
    }

    public void actionPerformed(ActionEvent arg0) {
        SwingBinder binder = binderFactory.createBinder();
        ChoiceField<SwingNode<Institucion>> cf = new ChoiceField<SwingNode<Institucion>>(txtField, "Seleccione una institucion", nodeFactory.createRootNode(), nodeFactory.getNodeSelectedClass());
        cf.addAnnounceSupportTag(Institucion.class);
        binder.bindChoiceToObject(cf, institucionNode);

        for (ActionListener al : txtField.getActionListeners()) {
            al.actionPerformed(arg0);
        }

    }

    public void setIsLogin(boolean arg0) {
    }

    private PersonaServiceRemote getPersonaService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(PersonaServiceRemote.class);
    }
}

class AgregarCompetidorTorneoAction extends AbstractSwingAction {

    private TorneoInstitucion torneoInstitucion;

    public AgregarCompetidorTorneoAction(TorneoInstitucion torneoInstitucion) {
        super("Registrar competidor...", "Torneos", Acceso.MODIFICACION);
        this.torneoInstitucion = torneoInstitucion;
    }

    public void actionPerformed(ActionEvent arg0) {

        if (torneoInstitucion != null && torneoInstitucion.getTorneo().getEstadoTorneo().getTipoEstadoTorneo().equals(TipoEstadoTorneo.ACTIVO)) {
            SwingControllerFactory swingFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);
            CompetidorController controller = swingFactory.createController(CompetidorController.class);
            controller.setTitle("Agregar competidor al torneo...");
            controller.setTorneo(torneoInstitucion.getTorneo());
            controller.setInstitucion(torneoInstitucion.getInstitucion());
            boolean error = true;
            while (error && controller.showModal()) {
                try {
                    Competidor competidor = controller.crearCompetidor();
                    competidor = getTorneoService().crearCompetidor(competidor);
                    error = false;
                } catch (Throwable ex) {
                    controller.getGuiUtils().warnnig(ex, TDKServerException.class);
                }


            }
        } else {
            GuiUtils gu = Lookup.getDefault().lookup(GuiUtils.class);
            gu.warnnig("Seleccione un torneo <B>ACTIVO</B>");
        }
    }

    public void setIsLogin(boolean arg0) {
    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }
}


