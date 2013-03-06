/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.DelegatingListModel;
import com.thorplatform.swing.InputController;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingListController;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class CinturonesController extends SwingListController<Cinturon> {

    @Override
    protected DelegatingListModel<Cinturon> configureListModel() {
        return new DelegatingListModel<Cinturon>(getListProperty().getList());
    }

    @Override
    protected Cinturon agregarAction() {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        InputController controller = scf.createController(InputController.class);
        controller.setTitle("Agregar cinturon");
        controller.setMaxLenght(new Integer(64));
        boolean error = true;
        Cinturon cinturon = null;
        while (error && controller.showModal()) {
            try {
                cinturon = new Cinturon();
                cinturon.setDescripcion(controller.getTexto().get());
                cinturon = getTorneoService().crearCinturon(cinturon);
                error = false;
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
        return cinturon;
    }

    @Override
    protected Cinturon editarAction(Cinturon cinturon) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        InputController controller = scf.createController(InputController.class);
        controller.setTitle("Modificar cinturon");
        controller.setMaxLenght(new Integer(64));
        controller.getTexto().set(cinturon.getDescripcion());
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                cinturon.setDescripcion(controller.getTexto().get());
                getTorneoService().modificarCinturon(cinturon);
                error = false;
                return cinturon;
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
        return null;
    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }

    @Override
    protected boolean quitarAction(Cinturon cinturon) {
        try {
            boolean quitar = getGuiUtils().confirmation(String.format("¿Desea eliminar el cinturon %s?", cinturon.getDescripcion()));
            if (quitar) {
                 getTorneoService().eliminarCinturon(cinturon.getId());
            }
           return quitar;
        } catch (Throwable ex) {
            getGuiUtils().warnnig(ex, TDKServerException.class);
            return false;
        }
    }
}
