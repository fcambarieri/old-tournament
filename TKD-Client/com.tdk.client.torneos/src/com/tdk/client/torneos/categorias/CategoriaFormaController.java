/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos.categorias;

import com.tdk.domain.torneo.CategoriaForma;
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
public class CategoriaFormaController extends SwingModalController implements SwingControllerChangeEvent{

    private CategoriaFormaForm form = new CategoriaFormaForm();
    private CategoriaController categoriaController ;
    
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
        configureView();
        installActions();
    }


    private void configureView() {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        categoriaController = scf.createController(CategoriaController.class);
        categoriaController.setControllerNotifier(this);
        form.pnlCentral.add(categoriaController.getForm(), BorderLayout.CENTER);
        
        getSwingValidator().setJLabel(form.lblMessage);
        
        categoriaController.setSwingValidator(this.getSwingValidator());
        
        categoriaController.clearFields();
    }

    private void installActions() {
        getSwingValidator().addSwingValidator(categoriaController.getDescripcionValidator());
        getSwingValidator().addSwingValidator(categoriaController.getEdadesValidator());
    }

    public void initControllerForUpdate(CategoriaForma categoriaForma) {
        categoriaController.initControllerForUpdate(categoriaForma);
    }
    
    public CategoriaForma crearCategoriaForma() {
        return modificarCategoriaForma(new CategoriaForma());
    }

    public CategoriaForma modificarCategoriaForma(CategoriaForma categoriaForma) {
        return (CategoriaForma) categoriaController.modificarCategoria(categoriaForma);
    }

    public void notifyEvent(PropertyChangeEvent evt) {
        this.propertyChange(evt);
    }
}
