/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.torneos.categorias.CategoriasFormasController;
import com.tdk.client.torneos.categorias.CategoriasLuchasController;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.services.TorneoServiceRemote;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.actions.NotifierSwingActionListener;
import com.thorplatform.swing.actions.SwingActionListener;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class ParametrizacionController extends SwingController implements SwingControllerChangeEvent {

    private ParametrizacionForm form = new ParametrizacionForm();
    private SwingControllerFactory swingFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);
    //Controllers
    private CinturonesController cinturonController = null;
    private CategoriasFormasController categoriasFormasController = null;
    private CategoriasLuchasController categoriasLuchasController = null;
    private NotifierSwingActionListener notifierActionListener;

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent arg0) {
    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        installActions();
    }

    private void configureView() {
        cinturonController = swingFactory.createController(CinturonesController.class);
        form.pnlCinturones.add(cinturonController.getPanel(), BorderLayout.CENTER);

        categoriasFormasController = swingFactory.createController(CategoriasFormasController.class);
        form.pnlCategoriaFormas.add(categoriasFormasController.getPanel(), BorderLayout.CENTER);

        categoriasLuchasController = swingFactory.createController(CategoriasLuchasController.class);
        form.pnlCategoriaLuchas.add(categoriasLuchasController.getPanel(), BorderLayout.CENTER);

        notifierActionListener = Lookup.getDefault().lookup(NotifierSwingActionListener.class);
        notifierActionListener.addSwingActionListener(new SwingActionListener() {
            public void setIsLogin(boolean isLogin) {
                if (isLogin) {
                    initialice();
                }
            }
        });

    }

    private void installActions() {
    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }

    public void initialice() {
        try {
            cinturonController.getListProperty().assignData(getTorneoService().listarCinturones("%"));
            categoriasFormasController.getTableList().assignData(getTorneoService().listarCategoriasForma("%"));
            categoriasLuchasController.categoriasLuchas.clear();
            List<CategoriaLucha> categorias = getTorneoService().listarCategoriasLucha("%");
            if (categorias != null && !categorias.isEmpty()) {
                for (CategoriaLucha cl : categorias) {
                    categoriasLuchasController.categoriasLuchas.add(cl, cl.getPesos());
                }
            }
        } catch (Exception e) {
        }

    }

    public void notifyEvent(PropertyChangeEvent evt) {
        this.propertyChange(evt);
    }
}
