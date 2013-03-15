/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.competencia.Llave;
import com.tdk.services.CompetenciaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
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
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.Lookup;



/**
 *
 * @author fernando
 */
public class LlaveRootNode extends ChoiceRootNode {

    private StrategySearchPattern<? extends Llave> strategySearchLlave;
    
    public LlaveRootNode() {
        super(new LlaveRootNodeChildren());
        installListeners();
    }
    
    @Override
    protected List loadKeys(String pattern) {
        if (strategySearchLlave != null)
            return strategySearchLlave.strategySearch(pattern);
        
        throw new RuntimeException("El strategy es nulo.");
    }

    public StrategySearchPattern<? extends Llave> getStrategySearchLlave() {
        return strategySearchLlave;
    }

    public void setStrategySearchLlave(StrategySearchPattern<? extends Llave> strategySearchLlave) {
        this.strategySearchLlave = strategySearchLlave;
    }

    private void installListeners() {
        setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String arg0) {
                return arg0 != null;
            }
        });
        
        setTitleForSearch("Cinturon - Categoria");
        
        setStrategySearchLlave(new StrategySearchPattern<Llave>() {

            public List<Llave> strategySearch(String arg0) {
                try {
                    return getCompetenciaService().listarLlave(arg0);
                } catch(Throwable ex) {
                    GuiUtils gui = Lookup.getDefault().lookup(GuiUtils.class);
                    gui.notificationError(ex);
                }
                return new ArrayList<Llave>();
            }
        });
    }
    
    public CompetenciaServiceRemote getCompetenciaService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(CompetenciaServiceRemote.class);
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarLlaveFormaAction(), new AgregarCategoriaLuchaAction()};
    }
    
    
}


class LlaveRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new LlaveNode((Llave) arg0)};
    }

    @Override
    public Class associateLookup() {
        return Llave.class;
    }
    
}

//class AgregarLlaveFormaAction extends AbstractSwingAction {
//
//    public AgregarLlaveFormaAction() {
//        super("Agregar llave forma", "Llave", Acceso.ALTA);
//    }
//    
//    @SuppressWarnings("unchecked")
//    public void actionPerformed(ActionEvent arg0) {
//        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
//        LlaveController<CategoriaForma> controller = scf.createController(LlaveController.class);
//        controller.setTitle("Agregar categoria forma...");
//        controller.categorias.assignData(controller.getTorneoService().listarCategoriasForma("%"));
//        boolean error = true;
//        while(error &&  controller.showModal()) {
//            try {
//                getCompetenciaService().crearLlaveForma(controller.cinturonSelected.get(), 
//                        controller.categoriaSelected.get(), 
//                        controller.torneoSelected.get());
//                error = false;
//            } catch(Throwable ex) {
//                controller.getGuiUtils().warnnig(ex, TDKServerException.class);
//            }
//        }
//    }
//
//    public void setIsLogin(boolean arg0) {
//    }
//    
//    private CompetenciaServiceRemote getCompetenciaService() {
//        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
//        return sf.getService(CompetenciaServiceRemote.class);
//    }
//    
//}

abstract class AgregarLlaveAction<T extends Categoria> extends AbstractSwingAction {

    private String title;
    
    public AgregarLlaveAction(String title) {
        super(title, "Llave", Acceso.ALTA);
        this.title = title;
    }
    
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        LlaveController<T> controller = scf.createController(LlaveController.class);
        controller.setTitle(title);
        controller.categorias.assignData(listarCategorias());
        boolean error = true;
        while(error &&  controller.showModal()) {
            try {
               crearLlave(controller);
               error = false;
            } catch(Throwable ex) {
                controller.getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }

    protected abstract List<T> listarCategorias();
    
    protected abstract void crearLlave(LlaveController<T> controller);
    
    public void setIsLogin(boolean arg0) {
        setEnabled(arg0);
    }
    
    protected CompetenciaServiceRemote getCompetenciaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(CompetenciaServiceRemote.class);
    }
    
    protected TorneoServiceRemote getTorneoServiceRemote() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }
    
}

class AgregarCategoriaLuchaAction extends AgregarLlaveAction<CategoriaLucha> {

    public AgregarCategoriaLuchaAction() {
        super("Agregar llave Lucha");
    }
    
    @Override
    protected List<CategoriaLucha> listarCategorias() {
       return getTorneoServiceRemote().listarCategoriasLucha("%");
    }

    @Override
    protected void crearLlave(LlaveController<CategoriaLucha> controller) {
        getCompetenciaService().crearLlaveLucha((Cinturon) controller.cinturonSelected.get(),
                controller.categoriaSelected.get(), 
                controller.torneoSelected.get());
    }
}

class AgregarLlaveFormaAction extends  AgregarLlaveAction<CategoriaForma> {

    public AgregarLlaveFormaAction() {
        super("Agregar lla Forma");
    }
    
    @Override
    protected List<CategoriaForma> listarCategorias() {
        return getTorneoServiceRemote().listarCategoriasForma("%");
    }

    @Override
    protected void crearLlave(LlaveController<CategoriaForma> controller) {
        getCompetenciaService().crearLlaveForma(controller.cinturonSelected.get(), 
                        controller.categoriaSelected.get(), 
                        controller.torneoSelected.get());
    }
    
}