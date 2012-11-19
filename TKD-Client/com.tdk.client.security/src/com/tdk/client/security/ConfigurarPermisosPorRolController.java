/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.domain.security.Funcionalidad;
import com.tdk.domain.security.FuncionalidadRol;
import com.tdk.domain.security.Rol;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.security.ConfigurarPermisosController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class ConfigurarPermisosPorRolController extends ConfigurarPermisosController {

    private Funcionalidad funcionalidad;
    private Rol rol;
    
    public final ListProperty<FuncionalidadRol> permisos = new ListProperty<FuncionalidadRol>("permisos");
    
    @Override
    protected void configureTableView() {
         ListTableModel<FuncionalidadRol> model = new ListTableModel<FuncionalidadRol>();
        model.setColumnClasses(new Class[]{String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class});
        model.setColumnTitles(new String[]{"Descripción", "Alta", "Baja", "Modificación", "Consulta"});
        model.setCellValueProvider(new ListTableModel.CellValueProvider() {

            public Object getCellValue(int row, int col) {
                FuncionalidadRol funcionalidadRol = permisos.get(row);
                Object result = null;
                switch (col) {
                    case 0:
                        result = funcionalidadRol.getFuncionalidad().getDescripcion();
                        break;
                    case 1:
                        result = funcionalidadRol.getAlta();
                        break;
                    case 2:
                        result = funcionalidadRol.getBaja();
                        break;
                    case 3:
                        result = funcionalidadRol.getModificacion();
                        break;
                    case 4:
                        result = funcionalidadRol.getConsulta();
                        break;

                }
                return result;
            }
        });

        form.xtblPermisos.setModel(model);
        createHighlighterPipeline(form.xtblPermisos);
    }

    @Override
    protected void configureTableBindings() {
        getSwingBinder().bindSingleSelectionTable(form.xtblPermisos, permisos, indexTable);
    }

    @Override
    protected void agregarPermisoAction() {
        TKDAccesosController controller = getSwingControllerFactory().createController(TKDAccesosController.class);
        controller.initControllerForRol(getRol());
        controller.setTitle("Permisos por rol...");
        boolean error = true;
        while (error && controller.showModal()) {
            FuncionalidadRol funcionalidadRol = controller.crearFuncionalidadRol();
            if (validarPermiso(funcionalidadRol)) {
                permisos.add(funcionalidadRol);
                error = false;
            } else {
                getGuiUtils().warnnig("El permiso ya existe pare ese rol");
            }
        }
    }

    @Override
    protected void quitarPermisoAction() {
        if (indexTable.get() != null && indexTable.get().intValue() >= 0) {
            permisos.remove(indexTable.get().intValue());
        }
    }

    @Override
    protected void editarPermiso(int index) {
        FuncionalidadRol funcionalidadRol = permisos.get(index);
        TKDAccesosController controller = getSwingControllerFactory().createController(TKDAccesosController.class);
        controller.initControllerForUpdateFuncionalidadRol(funcionalidadRol);
        controller.setTitle("Permisos por rol...");
        controller.setEnabledTextControlls(false);
        boolean error = true;
        while (error && controller.showModal()) {
            funcionalidadRol = controller.modificacionFuncionalidadRol(funcionalidadRol);
            form.xtblPermisos.repaint();
            error = false;
        }
    }

    private boolean validarPermiso(FuncionalidadRol funcionalidadRol) {
        boolean existe = false;
        int index = 0;
        FuncionalidadRol aux = null;
        while (!existe && index < permisos.getList().size()) {
            aux = permisos.get(index);
            existe = aux.getRol().equals(funcionalidadRol.getRol()) &&
                    aux.getFuncionalidad().equals(funcionalidadRol.getFuncionalidad());
            index++;
        }

        return !existe;
    }

    public Funcionalidad getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        if (evt.getSource() == permisos) {
            form.btnQuitar.setEnabled(!permisos.getList().isEmpty());
            form.btnEditar.setEnabled(!permisos.getList().isEmpty());
        }
    }
    
    
    
}
