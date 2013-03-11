/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.torneos.competidor.CompetidorController;
import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.InstitucionCompetidorDTO;
import com.tdk.domain.torneo.TipoEstadoTorneo;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.DelegatingTreeTableModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.MapProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.actions.NotifierSwingActionListener;
import com.thorplatform.swing.actions.SwingActionListener;
import com.thorplatform.swing.actions.ValidatePermision;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author fernando
 */
public class RegistrarCompetidorTorneoController extends SwingController {

    private RegistrarCompetidorTorneoForm form = new RegistrarCompetidorTorneoForm();
    private SwingControllerFactory swingFactory;
    private Property<Torneo> torneoSelected = new Property<Torneo>("torneoSelected");
    private ListProperty<Torneo> torneos = new ListProperty<Torneo>("torneos");
    private final MapProperty<Institucion, Competidor> instituciones = new MapProperty<Institucion, Competidor>("intituciones");
    private final Property<Institucion> institucionSelected = new Property<Institucion>("torneoInstitucionSelected");
    private final Property<Competidor> competidorSelected = new Property<Competidor>("competidor");
    private ValidatePermision validarPermiso;
    private boolean permisoAlta = false;
    private boolean permisoEdicion = false;
    private boolean permisoEliminar = false;

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        confgiureView();
        configureBingings();
        initPresentationModel();
        installActions();
    }

    private void confgiureView() {
        swingFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);
        form.xtreetblCompetidores.getTreeSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        form.xtreetblCompetidores.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        HighlighterPipeline ht = new HighlighterPipeline(new Highlighter[]{AlternateRowHighlighter.quickSilver});
        form.xtreetblCompetidores.setHighlighters(ht);
        ImageIcon institucionIcon = new ImageIcon(Utilities.loadImage("com/tdk/client/personas/institucion-16x16.png"));
        final ImageIcon competidorIcon = new ImageIcon(Utilities.loadImage("com/tdk/client/personas/institucion/alumnos-16x16.png"));
        form.xtreetblCompetidores.setOpenIcon(institucionIcon);
        form.xtreetblCompetidores.setClosedIcon(institucionIcon);
        form.xtreetblCompetidores.setLeafIcon(competidorIcon);

        DelegatingTreeTableModel<TorneoInstitucion, Competidor> treeModel = new DelegatingTreeTableModel<TorneoInstitucion, Competidor>();
        treeModel.setColumnNames(new String[]{"Institucion", "Cinturon", "Sexo", "Categoria Lucha", "Categoria Forma"});
        treeModel.setColumnClasses(new Class[]{AbstractTreeTableModel.hierarchicalColumnClass, String.class, String.class, String.class, String.class});
        treeModel.setCellValueProvider(new DelegatingTreeTableModel.CellValueProvider() {
            public Object getValueAt(Object node, int column) {
                Object result = null;

                if (column == 0) {
                    result = node;
                } else {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                    if (treeNode.getUserObject() instanceof Competidor) {
                        Competidor competidor = (Competidor) treeNode.getUserObject();
                        Alumno alumno = competidor.getAlumno();
                        if (column == 1) {
                            result = competidor.getCinturon().getDescripcion();
                        } else if (column == 2) {
                            result = alumno.getPersonaFisica().getSexo().toString();
                        } else if (column == 3) {
                            result = competidor.getCompetidorCategoriaLucha() != null ? competidor.getCompetidorCategoriaLucha().getDisplay() : "No inscripto";
                        } else if (column == 4) {
                            result = competidor.getCompetidorCategoriaForma() != null ? competidor.getCompetidorCategoriaForma().getDisplay() : "No inscripto";
                        }
                    }
                }

                return result;
            }
        });

        form.xtreetblCompetidores.setTreeTableModel(treeModel);

        form.xtreetblCompetidores.setTreeCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public java.awt.Component getTreeCellRendererComponent(
                    javax.swing.JTree tree, Object value, boolean sel,
                    boolean expanded, boolean leaf, int row,
                    boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel,
                        expanded, leaf, row, hasFocus);
                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    if (node.getUserObject() instanceof Competidor) {
                        Competidor person = (Competidor) node
                                .getUserObject();
                        setText(person.getAlumno().getPersonaFisica().getDisplayName());
                        if (node.isLeaf()) {
                            setIcon(competidorIcon);
                        }
                    }

                }

                return this;
            }
        ;
        });
        
       // form.xtreetblCompetidores.setF

        treeModel.setDebug(true);

        form.cboTorneo.setModel(new DelegatingComboBoxModel<Torneo>(torneos.getList()));
    }

    private void configureBingings() {
        getSwingBinder().bindComboBoxToObject(form.cboTorneo, torneoSelected, torneos);

        getSwingBinder().bindTreeTableMap(form.xtreetblCompetidores, instituciones, institucionSelected, competidorSelected, Institucion.class, Competidor.class);
    }

    private void initPresentationModel() {
        validarPermiso = Lookup.getDefault().lookup(ValidatePermision.class);

        //validarPermiso.getAccess("Registrar Comeptidores", Acceso.ALTA)

        NotifierSwingActionListener notifierActionListener = Lookup.getDefault().lookup(NotifierSwingActionListener.class);
        notifierActionListener.addSwingActionListener(new SwingActionListener() {
            public void setIsLogin(boolean isLogin) {
                if (isLogin) {
                    torneos.assignData(getTorneoService().listarTorneos("%"));
                }
            }
        });
    }

    private void installActions() {
        form.btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                agregarCompetidor();
            }
        });

        form.btnExpand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                form.xtreetblCompetidores.expandAll();
            }
        });

        form.btnCollapse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                form.xtreetblCompetidores.collapseAll();
            }
        });

        form.btnQuitar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                quitarAction();
            }
        });

        form.btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                editarAction();
            }
        });


        form.txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && canSearch()) {
                    listarInstitucionCompatidores(form.txtBuscar.getText());
                }
            }
        });


    }

    private void agregarCompetidor() {
        if (getTorneo() != null && getTorneo().getEstadoTorneo().getTipoEstadoTorneo().equals(TipoEstadoTorneo.ACTIVO)) {
            CompetidorController controller = swingFactory.createController(CompetidorController.class);
            controller.setTitle("Agregar competidor al torneo...");
            controller.setTorneo(getTorneo());
            controller.setInstitucion(getInstitucionSeleccionada());
            if (controller.showModal()) {
                agregarCompetidor(controller.getCompetidorCreado());
            }
//            boolean error = true;
//            while (error && controller.showModal()) {
//                try {
//                    Competidor competidor = controller.crearCompetidor();
//                    competidor = getTorneoService().crearCompetidor(competidor);
//                    agregarCompetidor(competidor);
//
//                    error = false;
//                } catch (Throwable ex) {
//                    getGuiUtils().warnnig(ex, TDKServerException.class);
//                }
//            }
        } else {
            getGuiUtils().warnnig("Seleccione un torneo <B>ACTIVO</B>");
        }

    }

    private void agregarCompetidor(Competidor competidor) {
        if (!instituciones.getKeys().contains(competidor.getAlumno().getInstitucion())) {
            List<Competidor> competidores = new ArrayList<Competidor>();
            competidores.add(competidor);
            instituciones.add(competidor.getAlumno().getInstitucion(), competidores);

        } else {
            instituciones.addKeyItem(competidor.getAlumno().getInstitucion(), competidor);
        }
    }

    private Institucion getInstitucionSeleccionada() {
        Institucion institucion = null;
        if (institucionSelected.get() != null) {
            institucion = institucionSelected.get();
        } else if (competidorSelected.get() != null) {
            institucion = competidorSelected.get().getAlumno().getInstitucion();
        }
        return institucion;
    }

    public Torneo getTorneo() {
        return torneoSelected.get();
    }

    public void setTorneo(Torneo torneo) {
        if (torneos.getList().isEmpty()) {
            torneos.assignData(getTorneoService().listarTorneos("%"));
        }
        this.torneoSelected.set(torneo);
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        if (evt.getSource() == torneoSelected && torneoSelected.get() != null) {
            listarInstitucionCompatidores("%");
        } else if (evt.getSource() == instituciones) {
            setEnabledBotonesDeEdicion(instituciones.getMap().size() > 0);
        }
    }

    private void setEnabledBotonesDeEdicion(boolean enabled) {
        form.btnQuitar.setEnabled(enabled);
        form.btnEditar.setEnabled(enabled);
    }

    public void listarInstitucionCompatidores(String patron) {
        try {

            List<InstitucionCompetidorDTO> competidoresDTO = getTorneoService().listarInstitucionCompetidorDTOPorTorneo(torneoSelected.get().getId(), patron);

            Map<Institucion, List<Competidor>> competidoreRegistrados = new HashMap<Institucion, List<Competidor>>();

            if (competidoresDTO != null) {
                for (InstitucionCompetidorDTO dto : competidoresDTO) {
                    competidoreRegistrados.put(dto.getInstitucion(), dto.getCompetidores());
                }
            }


            instituciones.setMap(competidoreRegistrados);

        } catch (Throwable ex) {
            ex.printStackTrace();
            getGuiUtils().warnnig(ex, TDKServerException.class);
        }
    }

    private void editarAction() {

        if (getTorneo() != null && getTorneo().getEstadoTorneo().getTipoEstadoTorneo().equals(TipoEstadoTorneo.ACTIVO)) {

            if (competidorSelected.get() != null) {
                CompetidorController controller = swingFactory.createController(CompetidorController.class);
                controller.setTitle("Modificar competidor al torneo...");
                Competidor competidor = getTorneoService().recuperarCompetidor(competidorSelected.get().getId(), true);
                controller.initCompetidorController(competidor);
                boolean error = true;
                while (error && controller.showModal()) {
                    try {
                        competidor = controller.modificarCompetidor(competidor);
                        getTorneoService().modificarCompetidor(competidor);
                        competidorSelected.set(competidor);
                        form.xtreetblCompetidores.repaint();

                        error = false;
                    } catch (Throwable ex) {
                        getGuiUtils().warnnig(ex, TDKServerException.class);
                    }


                }
            } else {
                getGuiUtils().warnnig("Seleccione un competidor");
            }


        } else {
            getGuiUtils().warnnig("Seleccione un torneo <B>ACTIVO</B>");
        }

    }

    private void aplicarPermisos() {
        form.btnAgregar.setEnabled(permisoAlta);
    }

    private void quitarAction() {
        if (institucionSelected.get() != null) {
            if (getGuiUtils().confirmation("¿Desea eliminar la institución <B>" + institucionSelected.get().getDisplayName() + "</B> y sus competidores del torneo ?")) {
                try {
                    getTorneoService().eliminarInstitucionDelTorneo(torneoSelected.get().getId(), institucionSelected.get().getId());
                    instituciones.remove(institucionSelected.get());
                } catch (Throwable ex) {
                    getGuiUtils().warnnig(ex, TDKServerException.class);
                }
            }
        } else if (competidorSelected.get() != null) {
            if (getGuiUtils().confirmation("¿Desea eliminar el competidor '" + competidorSelected.get().getAlumno().getPersonaFisica().getDisplayName() + "' del torneo ?")) {
                try {
                    getTorneoService().eliminarCompetidor(competidorSelected.get().getId());
                    instituciones.removeKeyItem(competidorSelected.get().getAlumno().getInstitucion(), competidorSelected.get());
                } catch (Throwable ex) {
                    getGuiUtils().warnnig(ex, TDKServerException.class);
                }
            }
        }
    }

    private boolean canSearch() {
        return form.txtBuscar.getText() != null && form.txtBuscar.getText().length() > 0;
    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }
}
