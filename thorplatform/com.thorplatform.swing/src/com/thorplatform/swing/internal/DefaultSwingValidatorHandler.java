/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing.internal;

import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.SwingValidatorHandler;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author fernando
 */
public class DefaultSwingValidatorHandler implements SwingValidatorHandler {

    private List<AbstractSwingValidator> abstractsValidators;
    private JLabel jLabel;

    public DefaultSwingValidatorHandler() {
        abstractsValidators = new ArrayList<AbstractSwingValidator>();
        jLabel = new JLabel();
    }

    public void addSwingValidator(AbstractSwingValidator swingValildator) {
        abstractsValidators.add(swingValildator);
    }

    public void remove(AbstractSwingValidator swingValildator) {
        abstractsValidators.remove(swingValildator);
    }

    public AbstractSwingValidator get(int index) {
        return abstractsValidators.get(index);
    }

    public boolean validate() {
        boolean result = true;
        int index = 0;

        if (abstractsValidators.size() == 0) {
            return true;
        }
        
        AbstractSwingValidator validator = null;

        while (result && index < abstractsValidators.size()) {
            validator = abstractsValidators.get(index);
            result = validator.validate();
            index++;
        }

        if (validator != null) {
            changeJLabelState(!result, validator.getMessage());
        }

        return result;

    }

    public JLabel getJLabel() {
        return jLabel;
    }

    public void setJLabel(JLabel jLabel) {
        this.jLabel = jLabel;
        createFontLabel();
    }

    private void createFontLabel() {
        if (getJLabel() == null) {
            throw new NullPointerException("El Jlabel es nulo");
        }
        getJLabel().setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
        getJLabel().setForeground(new Color(45,106,197));
        loadImageIcon();
    }

    private void changeJLabelState(boolean visible, String text) {
        getJLabel().setText(visible ? text : "");
    }

    private void loadImageIcon() {
        String imgPath = System.getProperty("com.thorplatform.swing.jlabelvalidator");
        Icon icon = null;
        if (imgPath != null && imgPath.length() > 0) {
            try {
                icon = new ImageIcon(imgPath);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        if (icon == null) {
            try {
                icon = new ImageIcon("jlabelvalidator.png");
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        getJLabel().setIcon(icon);
    }
}
