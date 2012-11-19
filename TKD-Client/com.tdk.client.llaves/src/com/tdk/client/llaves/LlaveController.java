/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.nodes.BNode;
import com.tdk.client.nodes.BinaryNodeFactory;
import com.tdk.client.nodes.BinaryTree;
import com.tdk.client.swing.node.JBTree;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.competencia.Llave;
import com.tdk.services.CompetenciaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.utils.CalcBitInverso;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.RequiredPropertyValidator;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class LlaveController<T extends Categoria> extends SwingModalController {

    private LlavesForm form = new LlavesForm();
    private JBTree treePanel = new JBTree();
    private BinaryTree<Competidor> competidores = new BinaryTree<Competidor>();
    
    public final Property<Torneo> torneoSelected = new Property<Torneo>("torneoSelected");
    public final ListProperty<Torneo> torneos = new ListProperty<Torneo>("torneos");
    
    public final Property<Cinturon> cinturonSelected = new Property<Cinturon>("cinturonSelected");
    public final ListProperty<Cinturon> cinturones = new ListProperty<Cinturon>("cinturones");
    
    public final Property<T> categoriaSelected = new Property<T>("categoriaSelected");
    public final ListProperty<T> categorias = new ListProperty<T>("categorias");
    
    private TorneoServiceRemote torneoService;
    private CompetenciaServiceRemote competenciaService;
    
    @Override
    protected JButton getAcceptButton() {
        return form.btnAceptar;
    }

    @Override
    protected JButton getCancelButton() {
        return form.btnCancelar;
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        configureValidators();
        configureView();
        configureBindings();
        initPresentationModel();
    }

    private void configureBindings() {
        getSwingBinder().bindComboBoxToObject(form.cboCategoria, categoriaSelected, categorias);
        getSwingBinder().bindComboBoxToObject(form.cboCinturon, cinturonSelected, cinturones);
        getSwingBinder().bindComboBoxToObject(form.cboTorneo, torneoSelected, torneos);
    }

    private void configureValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(torneoSelected, "Seleccione un torneo"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(cinturonSelected, "Seleccione un cinturon"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(categoriaSelected, "Seleccione una categoria"));
    }

    private void configureView() {
        form.cboTorneo.setModel(new DelegatingComboBoxModel<Torneo>(torneos.getList()));
        form.cboCinturon.setModel(new DelegatingComboBoxModel<Cinturon>(cinturones.getList()));
        form.cboCategoria.setModel(new DelegatingComboBoxModel<T>(categorias.getList()));
        
        
        treePanel.setModel(competidores);
        competidores.setBinaryNodeFactory(new BinaryNodeFactory<Competidor>() {
            public BNode createBNode(Competidor value) {
                return new CompetidorBNode(value);
            }
        });
        
        form.pnlDiagrama.add(treePanel, BorderLayout.CENTER);
    }

    private void initPresentationModel() {
        torneoSelected.set(null);
        cinturonSelected.set(null);
        categoriaSelected.set(null);
        
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        torneoService = sf.getService(TorneoServiceRemote.class);
        competenciaService = sf.getService(CompetenciaServiceRemote.class);
        
        torneos.assignData(torneoService.listarTorneos("%"));
        cinturones.assignData(torneoService.listarCinturones("%"));
    }

    @SuppressWarnings("unchecked")
    public void initLlaveController(Llave llave) {
        torneoSelected.set(llave.getTorneo());
        cinturonSelected.set(llave.getCinturon());
        
        categoriaSelected.set((T) llave.getCategoria());
    }
    
    public TorneoServiceRemote getTorneoService() {
        return torneoService;
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        
        rebuilTree();
    }

    @SuppressWarnings("unchecked")
    private void rebuilTree() {
        if (torneoSelected.get() != null && cinturonSelected.get() != null && categoriaSelected.get() != null) {
            List<Competidor> list = null;
            if (categoriaSelected.get() instanceof CategoriaForma) {
                list = torneoService.listarCompetidoresForma(cinturonSelected.get().getId(), 
                        categoriaSelected.get().getId(), 
                        torneoSelected.get().getId());
            } else if (categoriaSelected.get() instanceof CategoriaLucha) {
                list = torneoService.listarCompetidoresLucha(cinturonSelected.get().getId(), 
                        categoriaSelected.get().getId(), 
                        torneoSelected.get().getId());
            } 
            
            if (list != null) {
                list = CalcBitInverso.orderList(list);
                competidores.convertListToBinaryTree(list);
            }
        }
    }
    
}
