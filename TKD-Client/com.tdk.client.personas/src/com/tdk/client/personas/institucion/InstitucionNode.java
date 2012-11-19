/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas.institucion;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.personas.PersonaNode;
import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.Profesor;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.NodeBuilderFactory;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.actions.AbstractSwingAction;
import java.awt.event.ActionEvent;
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
public class InstitucionNode extends SwingNode<Institucion> {

    public InstitucionNode(Institucion institucion) {
        super(new InstitucionNodeChildren(institucion), institucion);
        setIconBaseWithExtension("com/tdk/client/personas/institucion-16x16.png");
    }

    @Override
    public String getDisplay() {
        return getValue().getDisplayName();
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new ModificarInstitucionAction(getValue())};
    }
}

class InstitucionNodeChildren extends Children.Keys {

    private static final Integer PROFESORES = new Integer(0);
    private static final Integer ALUMNOS = new Integer(1);
    private static final Integer TORNEOS = new Integer(2);
    private Institucion institucion;
    private List<Integer> categorias = null;

    public InstitucionNodeChildren(Institucion institucion) {
        this.institucion = institucion;
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        AbstractNode node = null;
        Integer key = (Integer) arg0;
        if (key.equals(PROFESORES)) {
            node = new InstitucionProfesorNode(institucion);
        } else if (key.equals(ALUMNOS)) {
            node = new InstitucionAlumnoNode(institucion);
        } else if (key.equals(TORNEOS)) {
            node = new TorneosRegistradoNode(institucion);
        }

        return new Node[]{node};
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        if (institucion != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(PROFESORES);
            categorias.add(ALUMNOS);
            categorias.add(TORNEOS);
            setKeys(categorias);
        }
    }
}

class InstitucionProfesorNode extends AbstractNode {

    public InstitucionProfesorNode(Institucion institucion) {
        super(new InstitucionProfesorNodeChildren(institucion));
        setIconBaseWithExtension("com/tdk/client/personas/institucion/profesores-16x16.png");
    }

    @Override
    public String getDisplayName() {
        return "Profesores";
    }
}

class InstitucionProfesorNodeChildren extends Children.Keys {

    private Institucion institucion;

    public InstitucionProfesorNodeChildren(Institucion institucion) {
        this.institucion = institucion;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        PersonaServiceRemote service = sf.getService(PersonaServiceRemote.class);
        institucion = service.recuperarInstitucion(institucion.getId(), true);
        setKeys(institucion.getProfesores());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Node[] createNodes(Object arg0) {
        Profesor profesor = (Profesor) arg0;
        return new Node[]{new ProfesorNode(profesor)};
    }
}

class ProfesorNode extends AbstractNode {

    private Profesor profesor;

    public ProfesorNode(Profesor profesor) {
        super(Children.LEAF);
        setIconBaseWithExtension("com/tdk/client/personas/institucion/profesores-16x16.png");
        this.profesor = profesor;
    }

    @Override
    public String getDisplayName() {
        return profesor.getPersonaFisica().getDisplayName();
    }
}

class InstitucionAlumnoNode extends AbstractNode {

    public InstitucionAlumnoNode(Institucion institucion) {
        super(new InstitucionAlumnoNodeChildren(institucion));
        setIconBaseWithExtension("com/tdk/client/personas/institucion/alumnos-16x16.png");
    }

    @Override
    public String getDisplayName() {
        return "Alumnos";
    }
}

class InstitucionAlumnoNodeChildren extends Children.Keys {

    private Institucion institucion;

    public InstitucionAlumnoNodeChildren(Institucion institucion) {
        this.institucion = institucion;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        PersonaServiceRemote service = sf.getService(PersonaServiceRemote.class);
        institucion = service.recuperarInstitucion(institucion.getId(), true);
        setKeys(institucion.getCompetidores());
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        Alumno alumno = (Alumno) arg0;
        return new Node[]{new PersonaNode(alumno.getPersonaFisica())};
    }
}

class TorneosRegistradoNode extends AbstractNode {

    public TorneosRegistradoNode(Institucion it) {
        super(new TorneosRegistradoNodeChildren(it));
        setIconBaseWithExtension("com/tdk/client/torneos/torneo-14x16.png");
    }

    @Override
    public String getDisplayName() {
        return "Inscripto en los torneos";
    }
}

class TorneosRegistradoNodeChildren extends Children.Keys<TorneoInstitucion> {

    private Institucion institucion;

    public TorneosRegistradoNodeChildren(Institucion it) {
        this.institucion = it;
    }

    @Override
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        TorneoServiceRemote torneoService = sf.getService(TorneoServiceRemote.class);
        setKeys(torneoService.listarTorneosPorInstitucion(institucion.getId()));
    }

    @Override
    protected Node[] createNodes(TorneoInstitucion ti) {
        return new Node[]{new CompetidoresTorneoInstitucionNode(ti)};
    }
}

class CompetidoresTorneoInstitucionNode extends AbstractNode {

    private TorneoInstitucion torneoInstitucion;

    public CompetidoresTorneoInstitucionNode(TorneoInstitucion ti) {
        super(new CompetidoresTorneoInstitucionNodeChildren(ti));
        this.torneoInstitucion = ti;
        setIconBaseWithExtension("com/tdk/client/torneos/torneo-14x16.png");
    }

    @Override
    public String getDisplayName() {
        return "Torneo " + torneoInstitucion.getTorneo().getNombre();
    }
}

class CompetidoresTorneoInstitucionNodeChildren extends Children.Keys<Competidor> {

    private TorneoInstitucion torneoInstitucion;

    public CompetidoresTorneoInstitucionNodeChildren(TorneoInstitucion it) {
        this.torneoInstitucion = it;
    }

    @Override
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        TorneoServiceRemote torneoService = sf.getService(TorneoServiceRemote.class);
        setKeys(torneoService.listarCompetidoresPorTorneoEInstitucion(
                torneoInstitucion.getTorneo().getId(),
                torneoInstitucion.getInstitucion().getId(),
                "%"));

    }

    @Override
    @SuppressWarnings("unchecked")
    protected Node[] createNodes(Competidor competidor) {
        NodeBuilderFactory builderFactory = Lookup.getDefault().lookup(NodeBuilderFactory.class);
        NodeFactory<Competidor> nodeFactory = builderFactory.createNodeFactory(Competidor.class);
        return new Node[]{nodeFactory.createNode(competidor)};
    }
}

/*****************************************************************************
 *  ACTIONS
 ******************************************************************************/
class ModificarInstitucionAction extends AbstractSwingAction {

    private Institucion institucion;

    public ModificarInstitucionAction(Institucion institucion) {
        super("Modificar institución", "Instituciones", Acceso.MODIFICACION);
        this.institucion = institucion;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (institucion != null) {
            institucion = getPersonaService().recuperarInstitucion(institucion.getId(), true);
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            InstitucionController controller = scf.createController(InstitucionController.class);
            controller.setTitle("Modificar institución...");
            controller.initForUpdate(institucion);
            boolean error = true;
            while (error && controller.showModal()) {
                institucion = controller.modificarInstitucion(institucion);
                try {
                    getPersonaService().modificarInstitucion(institucion);
                    error = false;
                } catch (Throwable ex) {
                    controller.getGuiUtils().warnnig(ex, TDKServerException.class);
                }
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