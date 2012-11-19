/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.domain.security.FuncionalidadUsuario;
import com.tdk.domain.security.Usuario;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.security.ConfigurarPermisosController;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author fernando
 */
public class ConfigurarPermisosPorUsuarioController extends ConfigurarPermisosController{

    private Usuario usuario;
    
    public final ListProperty<FuncionalidadUsuario> permisos = new ListProperty<FuncionalidadUsuario>("permisos");
    
    @Override
    protected void configureTableView() {
         ListTableModel<FuncionalidadUsuario> model = new ListTableModel<FuncionalidadUsuario>();
        model.setColumnClasses(new Class[]{String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class});
        model.setColumnTitles(new String[]{"Descripción", "Alta", "Baja", "Modificación", "Consulta"});
        model.setCellValueProvider(new ListTableModel.CellValueProvider() {

            public Object getCellValue(int row, int col) {
                FuncionalidadUsuario funcionalidadUsuario = permisos.get(row);
                Object result = null;
                switch (col) {
                    case 0:
                        result = funcionalidadUsuario.getFuncionalidad().getDescripcion();
                        break;
                    case 1:
                        result = funcionalidadUsuario.getAlta();
                        break;
                    case 2:
                        result = funcionalidadUsuario.getBaja();
                        break;
                    case 3:
                        result = funcionalidadUsuario.getModificacion();
                        break;
                    case 4:
                        result = funcionalidadUsuario.getConsulta();
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
        controller.initControllerForUsuario(getUsuario());
        controller.setTitle("Permisos por usuario...");
        boolean error = true;
        while (error && controller.showModal()) {
            FuncionalidadUsuario funcionalidadRol = controller.crearFuncionalidadUsuario();
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
        FuncionalidadUsuario funcionalidadUsuario = permisos.get(index);
        TKDAccesosController controller = getSwingControllerFactory().createController(TKDAccesosController.class);
        controller.initControllerForUpdateFuncionalidadUsuario(funcionalidadUsuario);
        controller.setTitle("Permisos por usuario...");
        controller.setEnabledTextControlls(false);
        boolean error = true;
        while (error && controller.showModal()) {
            funcionalidadUsuario = controller.modificacionFuncionalidadUsuario(funcionalidadUsuario);
            form.xtblPermisos.repaint();
            error = false;
        }
    }

    private boolean validarPermiso(FuncionalidadUsuario funcionalidadUsuario) {
        boolean existe = false;
        int index = 0;
        FuncionalidadUsuario aux = null;
        while (!existe && index < permisos.getList().size()) {
            aux = permisos.get(index);
            existe = aux.getUsuario().equals(funcionalidadUsuario.getUsuario()) &&
                    aux.getFuncionalidad().equals(funcionalidadUsuario.getFuncionalidad());
            index++;
        }

        return !existe;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        if (evt.getSource() == permisos) {
            form.btnQuitar.setEnabled(!permisos.getList().isEmpty());
            form.btnEditar.setEnabled(!permisos.getList().isEmpty());
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
