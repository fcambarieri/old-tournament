/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.Sexo;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Peso;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.DelegatingTreeTableModel;
import com.thorplatform.swing.MapProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class CategoriasLuchasController extends SwingController {

    private CategoriasLuchasForm form = new CategoriasLuchasForm();
    public final MapProperty<CategoriaLucha, Peso> categoriasLuchas = new MapProperty<CategoriaLucha, Peso>("categorias");
    private final Property<CategoriaLucha> categoriaSelected = new Property<CategoriaLucha>("torneoInstitucionSelected");
    private final Property<Peso> pesoSelected = new Property<Peso>("competidor");
    private SwingControllerFactory swingFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {

        if (evt.getSource() == categoriasLuchas) {
            form.btnEditar.setEnabled(!categoriasLuchas.getKeys().isEmpty());
            form.btnQuitar.setEnabled(!categoriasLuchas.getKeys().isEmpty());
        }

    }

    @Override
    public void initController() {
        super.initController();
        configureView();
        configureBindings();
        initPresentationModel();
        installActions();
    }

    private void configureBindings() {
        getSwingBinder().bindTreeTableMap(form.xttCategoriaLucha, categoriasLuchas, categoriaSelected, pesoSelected, CategoriaLucha.class, Peso.class);
    }

    private void configureView() {
        form.xttCategoriaLucha.getTreeSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        form.xttCategoriaLucha.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        HighlighterPipeline ht = new HighlighterPipeline(new Highlighter[]{AlternateRowHighlighter.quickSilver});
        form.xttCategoriaLucha.setHighlighters(ht);

        DelegatingTreeTableModel<CategoriaLucha, Peso> treeModel = new DelegatingTreeTableModel<CategoriaLucha, Peso>();
        treeModel.setColumnNames(new String[]{"Categoria", "Sexo", "Edad inferior", "Edad Superior"});
        treeModel.setColumnClasses(new Class[]{AbstractTreeTableModel.hierarchicalColumnClass, String.class, Integer.class, Integer.class});
        treeModel.setCellValueProvider(new DelegatingTreeTableModel.CellValueProvider() {

            public Object getValueAt(Object node, int column) {
                Object result = null;

                if (column == 0) {
                    return node;
                } else {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                    if (treeNode.getUserObject() instanceof CategoriaLucha) {
                        CategoriaLucha c = (CategoriaLucha) treeNode.getUserObject();
                        if (column == 1) {
                            return c.getSexo().toString();
                        } else if (column == 2) {
                            result = c.getEdadInferior();
                        } else if (column == 3) {
                            result = c.getEdadSuperior();
                        }
                    }
                }
                return result;
            }
        });
        treeModel.setRoot(new DefaultMutableTreeNode());
        treeModel.setDebug(true);

        form.xttCategoriaLucha.setTreeTableModel(treeModel);
        //form.xttCategoriaLucha.getColumnExt(1).setCellRenderer(new SexoCellRendere());
    }

    public JPanel getPanel() {
        return getForm();
    }

    private void initPresentationModel() {
        categoriaSelected.set(null);
        pesoSelected.set(null);
    }

    private void installActions() {
        form.btnAgregar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                agregarCategoria();
            }
        });

        form.btnEditar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (categoriaSelected.get() != null) {
                    editarCategoria(categoriaSelected.get());
                }
            }
        });

        form.btnQuitar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (categoriaSelected.get() != null) {
                    quitarCategoria(categoriaSelected.get());
                }
            }
        });
    }

    private void agregarCategoria() {
        CategoriaLuchaController controller = swingFactory.createController(CategoriaLuchaController.class);
        controller.setTitle("Agregar categoria");
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                CategoriaLucha categoria = controller.crearCategoriaLucha();
                categoria = getTorneoService().crearCategoriaLuchas(categoria);
                categoriasLuchas.add(categoria, new ArrayList<Peso>());
                error = false;
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }

    private void editarCategoria(CategoriaLucha categoriaLucha) {
        CategoriaLuchaController controller = swingFactory.createController(CategoriaLuchaController.class);
        controller.setTitle("Editar categoria ");
        controller.initControllerForUpdate(categoriaLucha);
        boolean error = true;
        while (error && controller.showModal()) {
            try {
                categoriaLucha = controller.modificarCategoriaLucha(categoriaLucha);
                getTorneoService().modificarCategoriaLucha(categoriaLucha);
                form.repaint();
                error = false;
            } catch (Throwable ex) {
                getGuiUtils().warnnig(ex, TDKServerException.class);
            }
        }
    }

    private void quitarCategoria(CategoriaLucha categoriaLucha) {
        if (getGuiUtils().confirmation("¿Desea eliminar la categoria " + categoriaLucha.getDescripcion() + "?")) {
            try {
                getTorneoService().eliminarCategoria(categoriaLucha.getId());
                categoriasLuchas.remove(categoriaLucha);
            } catch (Throwable ex) {
                getGuiUtils().notificationError(ex, TDKServerException.class);
            }
        }

    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }
}

class SexoCellRendere extends JLabel implements TableCellRenderer {


    public SexoCellRendere() {
        setHorizontalAlignment(CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isFocus, boolean isSelected, int row, int column) {

        if (value instanceof Sexo) {
            setText(value.toString());
            boolean esMujer = ((Sexo)value).equals(Sexo.FEMENINO);
            setForeground(esMujer ? Color.BLUE : Color.RED);
        }

        return this;
    }

}