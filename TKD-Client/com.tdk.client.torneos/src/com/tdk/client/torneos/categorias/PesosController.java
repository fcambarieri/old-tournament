/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias;

import com.tdk.domain.torneo.Peso;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.table.SwingTableController;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Comparator;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class PesosController extends SwingTableController<Peso> {

    private SwingControllerChangeEvent controllerChangeEvent;

    @Override
    protected ListTableModel<Peso> configureTable() {
        ListTableModel<Peso> model = new ListTableModel<Peso>();
        model.setColumnTitles(new String[]{"Peso desde", "Peso hasta"});
        model.setColumnClasses(new Class[]{Integer.class, Integer.class});
        model.setColumnMethodNames(new String[]{"getPesoInferior", "getPesoSuperior"});
        return model;
    }

    @Override
    protected Peso agregarAction() {
        Peso peso = null;
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        PesoController controller = scf.createController(PesoController.class);
        controller.setTitle("Agregar peso");
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                peso = controller.crearPeso();
                if (!validarPeso(peso)) {
                    getGuiUtils().warnnig("Ya existe un peso con los rangos " + peso.getPesoInferior() + " a " + peso.getPesoSuperior());
                } else {
                    error = false;
                    return peso;
                }
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }

        return null;
    }

    @Override
    protected Peso editarAction(int index) {
        Peso peso = getTableList().get(index);
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        PesoController controller = scf.createController(PesoController.class);
        controller.setTitle("Modificar peso");
        boolean error = true;
        while (error && controller.showModal()) {
            try {

                peso = controller.modificarPeso(peso);
                if (!validarPeso(peso)) {
                    getGuiUtils().warnnig("Ya existe un peso con los rangos " + peso.getPesoInferior() + " a " + peso.getPesoSuperior());
                } else {
                    error = false;
                    return peso;
                }
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }

        return null;
    }

    @Override
    protected void quitarAction(Peso arg0) {
    }

    public SwingControllerChangeEvent getControllerChangeEvent() {
        return controllerChangeEvent;
    }

    public void setControllerChangeEvent(SwingControllerChangeEvent controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);

        if (getControllerChangeEvent() != null) {
            getControllerChangeEvent().notifyEvent(evt);
        }
    }

    private boolean validarPeso(Peso peso) {
        boolean validar = true;
        Integer edadInferior = peso.getPesoInferior();
        Integer edadSuperior = peso.getPesoSuperior();

        Collections.sort(getTableList().getList(), new Comparator<Peso>() {

            public int compare(Peso peso1, Peso peso2) {
                return peso1.getPesoInferior().compareTo(peso2.getPesoInferior());
            }
        });

        for (Peso p : getTableList().getList()) {
            if (!p.equals(peso)) {
                boolean v1 = edadInferior.compareTo(p.getPesoInferior()) <= 0 && edadSuperior.compareTo(p.getPesoInferior()) >= 0;
                boolean v2 = edadInferior.compareTo(p.getPesoInferior()) >= 0 && edadInferior.compareTo(p.getPesoSuperior()) <= 0;
                if (v1 || v2) {
                    validar = false;
                    break;
                }
            }
        }

        return validar;
    }
}
