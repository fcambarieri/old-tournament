/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas.institucion;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.Alumno;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.ValidatorFilter;
import com.thorplatform.utils.GuiUtils;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class AlumnoRootNode extends ChoiceRootNode {

    private StrategySearchPattern<? extends Alumno> strategySearchAlumno;

    public AlumnoRootNode() {
        super(new AlumnoRootNodeChildren());
        installActions();
    }
    
    public AlumnoRootNode(StrategySearchPattern<? extends Alumno> strategySearchAlumno) {
        this();
        setStrategySearchAlumno(strategySearchAlumno);
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
}

class AlumnoRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new AlumnoNode((Alumno) arg0)};
    }
}
