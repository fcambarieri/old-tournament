/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.validator;

import javax.swing.JLabel;

/**
 *
 * @author fernando
 */
public interface SwingValidatorHandler {
    
    void addSwingValidator(AbstractSwingValidator swingValildator);
    void remove(AbstractSwingValidator swingValildator);
    AbstractSwingValidator get(int index);
    boolean validate();
    void setJLabel (JLabel jlabel);
    

}
