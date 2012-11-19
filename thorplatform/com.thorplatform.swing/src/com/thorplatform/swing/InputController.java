/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

import com.thorplatform.swing.validator.StringPropertyValidator;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class InputController extends SwingModalController {

    private InputForm form = new InputForm();
    
    private Property<String> texto = new Property<String>("texto");
    
    private StringPropertyValidator stringValidator;
    private Integer maxLenght = null;
    
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
        stringValidator = new StringPropertyValidator(texto, "Ingrese un valor", 1,getMaxLenght());
        getSwingValidator().addSwingValidator(stringValidator);
        getSwingBinder().bindTextComponentToString(form.txtInput, texto);
        texto.set(null);
    }

    public Integer getMaxLenght() {
        return maxLenght;
    }

    public void setMaxLenght(Integer maxLenght) {
        this.maxLenght = maxLenght;
        stringValidator.setMaxLength(maxLenght);
    }
    
    public void setLabelDescripcion(String text) {
        form.lblDescripicion.setText(text);
    }
    
    public Property<String> getTexto() {
        return texto;
    }

}
