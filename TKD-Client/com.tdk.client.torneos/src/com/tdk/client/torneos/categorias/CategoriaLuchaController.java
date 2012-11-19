/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias;

import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Peso;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingModalController;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class CategoriaLuchaController extends SwingModalController implements SwingControllerChangeEvent {

    private CategoriaLuchaForm form = new CategoriaLuchaForm();
    private CategoriaController categoriaController = null;
    private PesosController pesosController = null;

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
        
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        categoriaController = scf.createController(CategoriaController.class);
        pesosController = scf.createController(PesosController.class);
        
        installValidators();
        configureView();
        initPresentationModel();
    }

    private void configureView() {

        form.pnlCategoria.setLayout(new BorderLayout());
        form.pnlPesos.setLayout(new BorderLayout());

        form.pnlCategoria.add(categoriaController.getForm(), BorderLayout.CENTER);
        form.pnlPesos.add(pesosController.getPanel(), BorderLayout.CENTER);

        pesosController.setControllerChangeEvent(this);
        categoriaController.setControllerNotifier(this);

        getSwingValidator().setJLabel(form.lblMessage);
        
        pesosController.setSwingValidator(getSwingValidator());
        categoriaController.setSwingValidator(getSwingValidator());

        categoriaController.clearFields();
        categoriaController.setSexoVisible(true);
        
    }

    public void notifyEvent(PropertyChangeEvent evt) {
        this.propertyChange(evt);
    }

    public CategoriaLucha crearCategoriaLucha() {
        return modificarCategoriaLucha(new CategoriaLucha());
    }

    public CategoriaLucha modificarCategoriaLucha(CategoriaLucha categoriaLucha) {
        categoriaLucha = (CategoriaLucha) categoriaController.modificarCategoria(categoriaLucha);
        categoriaLucha.setSexo(categoriaController.getSexo());
        categoriaLucha.setPesos(pesosController.getTableList().getList());
        for (Peso peso : categoriaLucha.getPesos()) {
            peso.setCategoriaLucha(categoriaLucha);
        }
        return categoriaLucha;
    }

    private void initPresentationModel() {
        categoriaController.clearFields();
    }

    public void initControllerForUpdate(CategoriaLucha categoriaLucha) {
        categoriaController.initControllerForUpdate(categoriaLucha);
        categoriaController.setSexo(categoriaLucha.getSexo());
        pesosController.getTableList().assignData(categoriaLucha.getPesos());
    }

    private void installValidators() {
        getSwingValidator().addSwingValidator(categoriaController.getDescripcionValidator());
        getSwingValidator().addSwingValidator(categoriaController.getEdadesValidator());
    }

}