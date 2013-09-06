/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.Peso;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.CompetenciaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.DelegatingTreeTableModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.MapProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingController;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.actions.NotifierSwingActionListener;
import com.thorplatform.swing.actions.SwingActionListener;
import com.thorplatform.swing.table.SwingTableController;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

/**
 *
 * @author sabon
 */
public class TorneoLlavesController extends SwingController {

    private TorneoLlavesForm form = new TorneoLlavesForm();
    public final Property<Torneo> torneoSelected = new Property<Torneo>("torneoSelected");
    public final ListProperty<Torneo> torneos = new ListProperty<Torneo>("torneos");
    public final Property<Cinturon> cinturonSelected = new Property<Cinturon>("cinturonSelected");
    public final ListProperty<Cinturon> cinturones = new ListProperty<Cinturon>("cinturones");
    public final Property<CategoriaLucha> categoriaLuchaSelected = new Property<CategoriaLucha>("categoriaLuchaSelected");
    public final ListProperty<CategoriaLucha> categoriaLuchas = new ListProperty<CategoriaLucha>("categoriaLuchas");
    public final Property<CategoriaForma> categoriaFormaSelected = new Property<CategoriaForma>("categoriaFormaSelected");
    public final ListProperty<CategoriaForma> categoriaFormas = new ListProperty<CategoriaForma>("categoriaFormas");
    //
    public final MapProperty<TreeItem, TreeItemChildren> categoriasXCompetidores = new MapProperty<TreeItem, TreeItemChildren>("intituciones");
    private final Property<TreeItem> itemSelected = new Property<TreeItem>("torneoInstitucionSelected");
    private final Property<TreeItemChildren> competidorSelected = new Property<TreeItemChildren>("competidor");
    //services
    private TorneoServiceRemote torneoService;
    private CompetenciaServiceRemote competenciaService;

    @Override
    public void initController() {
        super.initController();
        configureValidators();
        configureView();
        configureBindings();
        initPresentationModel();
    }

    private void configureView() {
        form.cboTorneo.setModel(new DelegatingComboBoxModel<Torneo>(torneos.getList()));
        form.cboCinturon.setModel(new DelegatingComboBoxModel<Cinturon>(cinturones.getList()));
        form.cboCategoriaLucha.setModel(new DelegatingComboBoxModel<CategoriaLucha>(categoriaLuchas.getList()));
        form.cboCategoriaForma.setModel(new DelegatingComboBoxModel<CategoriaForma>(categoriaFormas.getList()));

        form.btnPreVisualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bracketPreview();
            }
        });

        form.btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });


        form.btnCrearLlaves.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearLlave();
            }
        });
        form.btnLonelyCompetidors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLonelyCompetidores();
            }
        });

        form.xtbCompetencias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Imprimir");
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                form.xtbCompetencias.print();
                            } catch (PrinterException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    });
                    menu.add(item);
                    menu.setVisible(true);
                }
            }
        });

        ///tree

        //final ImageIcon competidorIcon = new ImageIcon(Utilities.loadImage("com/tdk/client/personas/institucion/alumnos-16x16.png"));
        //combate-16x16
        final ImageIcon categoriaLuchaIcon = new ImageIcon(ImageUtilities.loadImage("com/tdk/client/torneos/combate-16x16.png"));
        final ImageIcon categoriaFormaIcon = new ImageIcon(ImageUtilities.loadImage("com/tdk/client/torneos/formas-16x16.png"));
        final ImageIcon competidorIcon = new ImageIcon(ImageUtilities.loadImage("com/tdk/client/personas/institucion/alumnos-16x16.png"));

        form.xtbCompetencias.getTreeSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        form.xtbCompetencias.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        HighlighterPipeline ht = new HighlighterPipeline(new Highlighter[]{AlternateRowHighlighter.quickSilver});
        form.xtbCompetencias.setHighlighters(ht);

        DelegatingTreeTableModel treeModel = new DelegatingTreeTableModel<TorneoInstitucion, Competidor>() {
            @Override
            public boolean isCellEditable(Object node, int column) {
                return column == 3;
            }

            @Override
            public void setValueAt(Object value, Object node, int column) {
                //super.setValueAt(value, node, column); //To change body of generated methods, choose Tools | Templates.

                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                if (treeNode.getUserObject() instanceof TreeItem) {
                    TreeItem item = (TreeItem) treeNode.getUserObject();
                    item.setImprimir(Boolean.valueOf(value.toString()));
                    if (item.isImprimir()) {
                        for (TreeItemChildren child : item.getChildren()) {
                            child.setImprimir(true);
                        }
                        this.nodeStructureChanged(treeNode);
                    }
                } else if (treeNode.getUserObject() instanceof TreeItemChildren) {
                    TreeItemChildren item = (TreeItemChildren) treeNode.getUserObject();
                    item.setImprimir(Boolean.valueOf(value.toString()));
                }
            }
        };
        treeModel.setColumnNames(new String[]{"Categoria", "Cinturon", "Competidores", "Imprimir"});
        treeModel.setColumnClasses(new Class[]{AbstractTreeTableModel.hierarchicalColumnClass, String.class, String.class, String.class, Integer.class, Boolean.class});
        treeModel.setCellValueProvider(new DelegatingTreeTableModel.CellValueProvider() {
            public Object getValueAt(Object node, int column) {
                Object result = null;

                if (column == 0) {
                    result = node;
                } else {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                    if (treeNode.getUserObject() instanceof TreeItem) {
                        TreeItem item = (TreeItem) treeNode.getUserObject();
                        switch (column) {
                            case 1:
                                result = item.getCinturon().getDescripcion();
                                break;
                            case 2:
                                result = item.size();
                                break;
                            case 3:
                                result = item.isImprimir();
                                break;
                        }
                    } else if (treeNode.getUserObject() instanceof TreeItemChildren) {
                        TreeItemChildren item = (TreeItemChildren) treeNode.getUserObject();
                        switch (column) {
                            case 2:
                                result = item.size();
                                break;
                            case 3:
                                result = item.isImprimir();
                                break;
                        }
                    }
                }

                return result;
            }
        });

        form.xtbCompetencias.setTreeCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public java.awt.Component getTreeCellRendererComponent(
                    javax.swing.JTree tree, Object value, boolean sel,
                    boolean expanded, boolean leaf, int row,
                    boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    if (node.getUserObject() instanceof TreeItemChildren) {
                        TreeItemChildren child = (TreeItemChildren) node
                                .getUserObject();
                        setText(child.getDisplay());
                        if (node.isLeaf()) {
                            setIcon(competidorIcon);
                        }
                    }
                    if (node.getUserObject() instanceof TreeItem) {
                        TreeItem item = (TreeItem) node
                                .getUserObject();
                        setText(item.getCategoria().getDisplay());
                        setIcon(categoriaFormaIcon);
                        if (!node.isLeaf()) {
                            if (item.getCategoria() instanceof CategoriaForma) {
                                setIcon(categoriaFormaIcon);
                            } else {
                                setIcon(categoriaLuchaIcon);
                            }
                        }
                    }
                }
                return this;
            }
        ;
        });

        form.xtbCompetencias.setTreeTableModel(treeModel);
        form.xtbCompetencias.getColumnModel().getColumn(3).setCellRenderer(new BooleanRenderer());
        form.xtbCompetencias.getColumnModel().getColumn(3).setCellEditor(new BooleanEditor());
    }

    private void configureBindings() {
        getSwingBinder().bindComboBoxToObject(form.cboCategoriaLucha, categoriaLuchaSelected, categoriaLuchas);
        getSwingBinder().bindComboBoxToObject(form.cboCategoriaForma, categoriaFormaSelected, categoriaFormas);
        getSwingBinder().bindComboBoxToObject(form.cboCinturon, cinturonSelected, cinturones);
        getSwingBinder().bindComboBoxToObject(form.cboTorneo, torneoSelected, torneos);


        getSwingBinder().bindTreeTableMap(form.xtbCompetencias, categoriasXCompetidores, itemSelected, competidorSelected, TreeItem.class, TreeItemChildren.class);
    }

    private void crearLlave() {
        Cinturon cinturon = null;
        Categoria categoria = null;
        Torneo torneo = null;
        if (competidorSelected.get() != null) {
            TreeItemChildren child = competidorSelected.get();
            if (child.getCompetidores().size() > 1) {
                Competidor c = child.getCompetidores().get(0);
                torneo = c.getTorneo();
                categoria = c.getCompetidorCategoriaLucha().getPeso().getCategoriaLucha();
                cinturon = c.getCinturon();
            }
        } else {
            if (itemSelected.get() instanceof FormaTreeItem) {
                FormaTreeItem child = (FormaTreeItem) itemSelected.get();
                if (!child.getCompetidores().isEmpty()) {
                    Competidor c = child.getCompetidores().get(0);
                    torneo = c.getTorneo();
                    categoria = c.getCompetidorCategoriaForma().getCategoriaForma();
                    cinturon = c.getCinturon();
                }

            }
        }
        if (cinturon != null && categoria != null && torneo != null) {
            if (categoria instanceof CategoriaLucha) {
                competenciaService.crearLlaveLucha(cinturon, (CategoriaLucha) categoria, torneo);
            } else {
                competenciaService.crearLlaveForma(cinturon, (CategoriaForma) categoria, torneo);
            }

        }

    }

    private void configureValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
//        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(torneoSelected, "Seleccione un torneo"));
//        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(cinturonSelected, "Seleccione un cinturon"));
//        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(categoriaSelected, "Seleccione una categoria"));
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        if (evt.getSource() == torneoSelected) {
            if (torneoSelected.get() != null) {
                fillTreeItem();
            }
            form.btnLonelyCompetidors.setEnabled(torneoSelected.get() != null);

        } else if (evt.getSource() == competidorSelected && competidorSelected.get() != null) {
            form.btnPreVisualizar.setEnabled(competidorSelected.get().getCompetidores().size() > 1);
            form.btnCrearLlaves.setEnabled(competidorSelected.get().getCompetidores().size() > 1);
        } else if (evt.getSource() == itemSelected && itemSelected.get() != null) {
            form.btnPreVisualizar.setEnabled(itemSelected.get().getCompetidores().size() > 1);
            form.btnCrearLlaves.setEnabled(itemSelected.get().getCompetidores().size() > 1);
        }
    }

    private void initPresentationModel() {
        torneoSelected.set(null);
        cinturonSelected.set(null);
        categoriaFormaSelected.set(null);
        categoriaLuchaSelected.set(null);


        NotifierSwingActionListener notifierActionListener = Lookup.getDefault().lookup(NotifierSwingActionListener.class);
        notifierActionListener.addSwingActionListener(new SwingActionListener() {
            public void setIsLogin(boolean isLogin) {
                if (isLogin) {
                    ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
                    torneoService = sf.getService(TorneoServiceRemote.class);
                    competenciaService = sf.getService(CompetenciaServiceRemote.class);

                    torneos.assignData(torneoService.listarTorneos("%"));
                    cinturones.assignData(torneoService.listarCinturones("%"));
                    categoriaLuchas.assignData(torneoService.listarCategoriasLucha("%"));
                    categoriaFormas.assignData(torneoService.listarCategoriasForma("%"));
                }
            }
        });
    }

    private void showLonelyCompetidores() {

        if (torneoSelected.get() != null) {
            List<Competidor> competidores = filterLonelyCompetidores();
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            LonelyCompetidorsController controller = scf.createController(LonelyCompetidorsController.class);
            controller.setTitle("Competidores sin rivales");
            controller.setCompetidores(competidores);
            controller.showModal();
        }

    }

    private void refresh() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        if (sf.authenticated()) {
            torneoSelected.set(null);
            cinturonSelected.set(null);
            categoriaFormaSelected.set(null);
            categoriaLuchaSelected.set(null);



            torneoService = sf.getService(TorneoServiceRemote.class);
            competenciaService = sf.getService(CompetenciaServiceRemote.class);

            torneos.assignData(torneoService.listarTorneos("%"));
            cinturones.assignData(torneoService.listarCinturones("%"));
            categoriaLuchas.assignData(torneoService.listarCategoriasLucha("%"));
            categoriaFormas.assignData(torneoService.listarCategoriasForma("%"));
        }
    }

    private void fillTreeItem() {
        if (torneoSelected.get() != null) {
            Map<TreeItem, List<TreeItemChildren>> competidoreRegistrados = new HashMap<TreeItem, List<TreeItemChildren>>();
            List<TreeItem> treeItems = new LinkedList<TreeItem>();
            List<Competidor> competidores = torneoService.listarCompetidoresPorTorneo(torneoSelected.get().getId(), "%");
            if (competidores != null && !competidores.isEmpty()) {
                Iterator<Competidor> it = competidores.iterator();
                while (it.hasNext()) {
                    Competidor c = it.next();
                    if (c.getCompetidorCategoriaForma() != null) {
                        addTreeItem(treeItems, c, c.getCompetidorCategoriaForma().getCategoriaForma(), null);
                    }
                    if (c.getCompetidorCategoriaLucha() != null) {
                        addTreeItem(treeItems, c, c.getCompetidorCategoriaLucha().getPeso().getCategoriaLucha(), c.getCompetidorCategoriaLucha().getPeso());
                    }
                }
            }

            for (TreeItem ti : treeItems) {
                competidoreRegistrados.put(ti, ti.getChildren());
            }
            categoriasXCompetidores.setMap(competidoreRegistrados);
        }
    }

    private List<Competidor> filterLonelyCompetidores() {
        if (torneoSelected.get() != null) {
            Set<TreeItem> treeItems = categoriasXCompetidores.getKeys();
            //List<TreeItem> treeItems = new LinkedList<TreeItem>();
//            List<Competidor> competidores = torneoService.listarCompetidoresPorTorneo(torneoSelected.get().getId(), "%");
//            if (competidores != null && !competidores.isEmpty()) {
//                Iterator<Competidor> it = competidores.iterator();
//                while (it.hasNext()) {
//                    Competidor c = it.next();
//                    if (c.getCompetidorCategoriaForma() != null) {
//                        addTreeItem(treeItems, c, c.getCompetidorCategoriaForma().getCategoriaForma(), null);
//                    }
//                    if (c.getCompetidorCategoriaLucha() != null) {
//                        addTreeItem(treeItems, c, c.getCompetidorCategoriaLucha().getPeso().getCategoriaLucha(), c.getCompetidorCategoriaLucha().getPeso());
//                    }
//                }
//            }
            Set<Competidor> filtered = new HashSet<Competidor>();
            for (TreeItem ti : treeItems) {
                if (ti.isLeaf() && ti.getCompetidores().size() == 1) {
                    filtered.addAll(ti.getCompetidores());
                } else if (!ti.isLeaf()) {
                    for (TreeItemChildren child : ti.getChildren()) {
                        if (child.getCompetidores().size() == 1) {
                            filtered.addAll(child.getCompetidores());
                        }
                    }
                }
            }
            List<Competidor> competidores = new LinkedList<Competidor>();
            for(Competidor c : filtered) {
                competidores.add(c);
            }
                    

            return competidores;
        }
        return null;
    }

    private void bracketPreview() {
        if (competidorSelected.get() != null) {
            TreeItemChildren child = competidorSelected.get();
            if (child.getCompetidores().size() > 1) {
                SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
                LlavePreviewController controller = scf.createController(LlavePreviewController.class);
                controller.setCompetidores(child.getCompetidores());
                controller.showOnTopComponent(String.format("Lucha %s %s - %s", child.getRoot().toString(), child.getDisplay(), child.getRoot().getCinturon().getDescripcion()));

            }
        } else {
            if (itemSelected.get() instanceof FormaTreeItem) {
                FormaTreeItem child = (FormaTreeItem) itemSelected.get();

                String cinturon = child.getCinturon().getDescripcion();
                String categoria = child.getCategoria().getDisplay();

                SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
                LlavePreviewController controller = scf.createController(LlavePreviewController.class);
                controller.setCompetidores(child.getCompetidores());
                controller.showOnTopComponent(String.format("Forma %s %s ", categoria, cinturon));

            }
        }
    }

    private void addTreeItem(List<TreeItem> treeItems, Competidor c, Categoria categoria, Peso peso) {
        TreeItem item = null;
        if (peso == null) {
            item = new FormaTreeItem();
        } else {
            item = new LuchaTreeItem();
        }

        item.setCinturon(c.getCinturon());
        item.setTorneo(c.getTorneo());
        item.setCategoria(categoria);
        int i = treeItems.indexOf(item);

        if (i >= 0) {
            treeItems.get(i).add(c);
        } else {
            item.add(c);
            treeItems.add(item);
        }
    }
}

abstract class TreeItem {

    private Torneo torneo;
    private Categoria categoria;
    private Cinturon cinturon;
    private boolean imprimir = false;

    public TreeItem() {
    }

    public abstract int size();

    public abstract void add(Competidor c);

    public abstract List<Competidor> getCompetidores();

    public abstract List<TreeItemChildren> getChildren();

    public abstract boolean isLeaf();

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Cinturon getCinturon() {
        return cinturon;
    }

    public void setCinturon(Cinturon cinturon) {
        this.cinturon = cinturon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TreeItem)) {
            return false;
        }
        TreeItem other = (TreeItem) obj;

        return torneo.equals(other.getTorneo())
                && categoria.equals(other.getCategoria())
                && cinturon.equals(other.getCinturon());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.torneo != null ? this.torneo.hashCode() : 0);
        hash = 59 * hash + (this.categoria != null ? this.categoria.hashCode() : 0);
        hash = 59 * hash + (this.cinturon != null ? this.cinturon.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return categoria.getDisplay();
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }
}

class FormaTreeItem extends TreeItem {

    private FormaTreeItemChildren children = new FormaTreeItemChildren(this);

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public void add(Competidor c) {
        if (children.getCompetidores() == null) {
            children.setCompetidores(new ArrayList<Competidor>());
        }
        children.getCompetidores().add(c);
    }

    @Override
    public List<TreeItemChildren> getChildren() {
        return Collections.emptyList();// children;
    }

    @Override
    public List<Competidor> getCompetidores() {
        return children.getCompetidores();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}

class LuchaTreeItem extends TreeItem {

    private List<LuchaTreeItemChildren> children = new ArrayList<LuchaTreeItemChildren>();

    @Override
    public int size() {
        int i = 0;
        for (LuchaTreeItemChildren child : children) {
            i += child.size();
        }
        return i;
    }

    @Override
    public void add(Competidor c) {
        boolean newList = true;
        for (LuchaTreeItemChildren child : children) {
            if (child.getPeso().equals(c.getCompetidorCategoriaLucha().getPeso())) {
                child.getCompetidores().add(c);
                newList = false;
            }
        }
        if (newList) {
            LuchaTreeItemChildren child = new LuchaTreeItemChildren(this, c.getCompetidorCategoriaLucha().getPeso(), new ArrayList<Competidor>());
            child.getCompetidores().add(c);
            children.add(child);
        }
    }

    @Override
    public List<TreeItemChildren> getChildren() {
        return new ArrayList<TreeItemChildren>(children);//Collections. children;
    }

    @Override
    public List<Competidor> getCompetidores() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}

interface TreeItemChildren {

    String getDisplay();

    int size();

    boolean isImprimir();

    void setImprimir(boolean b);

    List<Competidor> getCompetidores();

    TreeItem getRoot();
}

class FormaTreeItemChildren implements TreeItemChildren {

    private CategoriaForma categoria;
    private List<Competidor> competidores;
    private boolean imprimir = false;
    private TreeItem root;

    public FormaTreeItemChildren(TreeItem root) {
        this.root = root;
    }

    public FormaTreeItemChildren(CategoriaForma categoria, List<Competidor> competidores) {
        this.categoria = categoria;
        this.competidores = competidores;
    }

    public List<Competidor> getCompetidores() {
        return competidores;
    }

    public String getDisplay() {
        return String.format("Forma - ", categoria.getDisplay());
    }

    public int size() {
        if (competidores != null) {
            return competidores.size();
        }
        return 0;
    }

    public void add(Object o) {
        competidores.add((Competidor) o);
    }

    public void setCompetidores(List<Competidor> competidores) {
        this.competidores = competidores;
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public TreeItem getRoot() {
        return root;
    }
}

class LuchaTreeItemChildren implements TreeItemChildren {

    private Peso peso;
    private List<Competidor> competidores;
    private boolean imprimir = false;
    private TreeItem root;

    public LuchaTreeItemChildren(TreeItem root, Peso peso, List<Competidor> competidores) {
        this.root = root;
        this.peso = peso;
        this.competidores = competidores;
    }

    public LuchaTreeItemChildren(TreeItem root) {
        this.root = root;
    }

    public void setCompetidores(List<Competidor> competidores) {
        this.competidores = competidores;
    }

    public void setPeso(Peso peso) {
        this.peso = peso;
    }

    public List<Competidor> getCompetidores() {
        return competidores;
    }

    public Peso getPeso() {
        return peso;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Peso)) {
            return false;
        }
        Peso other = (Peso) obj;
        return peso.equals(other);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.peso != null ? this.peso.hashCode() : 0);
        return hash;
    }

    public String getDisplay() {
        return String.format("[%d, %d] Kg", peso.getPesoInferior(), peso.getPesoSuperior());
    }

    public int size() {
        if (competidores != null) {
            return competidores.size();
        }
        return 0;
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public TreeItem getRoot() {
        return root;
    }
}

class BooleanEditor extends AbstractCellEditor implements TableCellEditor {

    protected JCheckBox checkBox;

    public BooleanEditor() {
        checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.setBackground(Color.white);
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    public Object getCellEditorValue() {
        return Boolean.valueOf(checkBox.isSelected());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Boolean) {
            checkBox.setSelected(((Boolean) value).booleanValue());
        }
        return checkBox;
    }
}

class BooleanRenderer extends JCheckBox implements TreeCellRenderer, TableCellRenderer {

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public BooleanRenderer() {
        super();
        setHorizontalAlignment(JLabel.CENTER);
        setBorderPainted(true);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setForeground(tree.getForeground());
        setBackground(tree.getBackground());
        setSelected((value != null && ((Boolean) value).booleanValue())); // <--- does not support strings
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(noFocusBorder);
        }
        return this;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setForeground(table.getForeground());
        setBackground(table.getBackground());
        setSelected((value != null && ((Boolean) value).booleanValue())); // <--- does not support strings

        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(noFocusBorder);
        }

        return this;
    }
}