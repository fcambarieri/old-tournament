/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos.categorias;

import com.tdk.domain.torneo.Peso;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.NumberPropertyValidator;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class PesoController extends SwingModalController{

    private PesoForm form = new PesoForm();
    public final Property<Integer> pesoInferior = new Property<Integer>("pesoInferior");
    public final Property<Integer> pesoSuperior = new Property<Integer>("pesoSuperior");
    
    
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
        installValidators();
        configureBindings();
    }

    private void configureBindings() {
        getSwingBinder().bindTextFieldToInteger(form.txtPesoInferior, pesoInferior);
        getSwingBinder().bindTextFieldToInteger(form.txtPesoSuperior, pesoSuperior);
    }

    private void installValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new NumberPropertyValidator(pesoInferior, "Ingrese un peso inferior"));
        getSwingValidator().addSwingValidator(new NumberPropertyValidator(pesoInferior, "Ingrese un peso superior"));
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("El peso inferior debe ser menor al superior") {
            @Override
            public boolean validate() {
                if (pesoInferior.get() != null && pesoSuperior.get() != null)
                    return pesoInferior.get() < pesoSuperior.get();
                return false;
            }
        });
    }

    public Peso crearPeso() {
        return modificarPeso(new Peso());
    }

    public Peso modificarPeso(Peso peso) {
        peso.setPesoInferior(pesoInferior.get());
        peso.setPesoSuperior(pesoSuperior.get());
        return peso;
    }
    
}
