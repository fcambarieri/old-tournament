/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas.institucion;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.personas.PersonaFisicaController;
import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.security.Acceso;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.ValidatorFilter;
import com.thorplatform.swing.actions.AbstractSwingAction;
import com.thorplatform.utils.GuiUtils;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;


/**
 *
 * @author fernando
 */
public class AlumnoRootNode extends ChoiceRootNode {

    private StrategySearchPattern<? extends Alumno> strategySearchAlumno;

    private Institucion institucion;
    
    private final InstanceContent lookupContents;
    
    public AlumnoRootNode() {
        this(new InstanceContent());
    }
    public AlumnoRootNode(InstanceContent ic) {
        super(new AlumnoRootNodeChildren(), new AbstractLookup(ic));
        installActions();
        this.lookupContents = ic;
    }
    
    public AlumnoRootNode(StrategySearchPattern<? extends Alumno> strategySearchAlumno) {
        this();
        setStrategySearchAlumno(strategySearchAlumno);
    }
    
    public AlumnoRootNode(Institucion institucion, StrategySearchPattern<? extends Alumno> strategySearchAlumno) {
        this(strategySearchAlumno);
        this.institucion = institucion;
    }
            

    @Override
    protected List loadKeys(String arg0) {
        if (strategySearchAlumno != null) {
            return strategySearchAlumno.strategySearch(arg0);
        }
        throw new NullPointerException("El strategy alumno es nulo");
    }

    public StrategySearchPattern<? extends Alumno> getStrategySearchAlumno() {
        return strategySearchAlumno;
    }

    public void setStrategySearchAlumno(StrategySearchPattern<? extends Alumno> strategySearchAlumno) {
        this.strategySearchAlumno = strategySearchAlumno;
    }

    private void installActions() {
        setTitleForSearch("Nombre - Apellido - Institución");
        setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String arg0) {
                return arg0 != null && arg0.trim().length() > 0;
            }
        });
        setStrategySearchAlumno(new StrategySearchPattern<Alumno>() {

            public List<Alumno> strategySearch(String arg0) {
                try {
                    
                } catch (Throwable ex) {
                    GuiUtils gui = Lookup.getDefault().lookup(GuiUtils.class);
                    gui.warnnig(ex, TDKServerException.class);
                }
                return new ArrayList<Alumno>();
            }
        });
    }

    public TorneoServiceRemote getTorneoService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(TorneoServiceRemote.class);
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    @Override
    public Action[] getActions(boolean context) {
        if (institucion != null) {
            return new Action[]{new AgregarAlumno(institucion)};
        } else {
            return super.getActions(context); 
        }
            
    }
    
    
}
class AlumnoRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new AlumnoNode((Alumno) arg0)};
    }
}


class AgregarAlumno extends AbstractSwingAction {

    private Institucion institucion;
    public AgregarAlumno(Institucion institucion) {
        super("Agregar alumno", "Personas", Acceso.ALTA);
        setIsLogin(true);
        this.institucion = institucion;
    }

    
    public void actionPerformed(ActionEvent e) {
        //AlumnoController
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        AlumnoController controller = scf.createController(AlumnoController.class);
        controller.initForAgregarAlumno(institucion);
        controller.setTitle("Agregar alumno...");
        boolean error = true;
        
        while (error && controller.showModal()) {
            try {
                Alumno a = getPersonaService().crearAlumno(controller.crearAlumno());
                error = false;
            } catch (Throwable ex) {
               controller.getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }
    
    private PersonaServiceRemote getPersonaService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(PersonaServiceRemote.class);
    }

    public void setIsLogin(boolean bln) {
         setEnabled(bln);
    }
    
}