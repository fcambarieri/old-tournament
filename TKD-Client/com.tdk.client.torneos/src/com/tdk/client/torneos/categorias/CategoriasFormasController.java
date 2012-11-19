/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.utils.IntegerCellRenderer;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.DelegatingListModel;
import com.thorplatform.swing.ListTableModel;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingListController;
import com.thorplatform.swing.table.SwingTableController;
import com.thorplatform.swing.table.SwingTableForm;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.openide.util.Lookup;


/**
 *
 * @author fernando
 */
public class CategoriasFormasController extends SwingTableController<CategoriaForma> {

    @Override
    protected CategoriaForma agregarAction() {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        CategoriaFormaController controller = scf.createController(CategoriaFormaController.class);
        controller.setTitle("Agregar categoria forma");
        CategoriaForma categoria = null;
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                categoria = controller.crearCategoriaForma();
                categoria = getTorneoService().crearCategoriaForma(categoria);
                error = false;
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
                categoria = null;
            }
        }

        return categoria;
    }

    @Override
    protected void quitarAction(CategoriaForma categoriaForma) {
        try {
            getTorneoService().eliminarCategoria(categoriaForma.getId());
        } catch(Throwable ex) {
            getGuiUtils().notificationError(ex);
        }
    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }

    @Override
    protected ListTableModel<CategoriaForma> configureTable() {
        ListTableModel<CategoriaForma> model = new ListTableModel<CategoriaForma>();
        model.setColumnTitles(new String[]{"Descripción", "Edad Inferior", "Edad Superior"});
        model.setColumnClasses(new Class[]{String.class, Integer.class, Integer.class});
        model.setColumnMethodNames(new String[]{"getDescripcion" ,"getEdadInferior", "getEdadSuperior"});
        return model;
    }

    @Override
    protected CategoriaForma editarAction(int row) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        CategoriaFormaController controller = scf.createController(CategoriaFormaController.class);
        controller.setTitle("Modificar categoria forma");
        CategoriaForma categoriaForma = getTableList().get(row);
        controller.initControllerForUpdate(categoriaForma);
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                categoriaForma = controller.modificarCategoriaForma(categoriaForma);
                getTorneoService().modificarCategoriaForma(categoriaForma);
                error = false;
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
        return categoriaForma;
    }

    @Override
    public void initController() {
        super.initController();
        ((SwingTableForm)getForm()).xTable.getColumnExt(1).setCellRenderer(new IntegerCellRenderer());
        ((SwingTableForm)getForm()).xTable.getColumnExt(2).setCellRenderer(new IntegerCellRenderer());
    }

}

