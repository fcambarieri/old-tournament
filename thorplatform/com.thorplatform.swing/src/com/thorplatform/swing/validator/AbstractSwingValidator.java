/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.validator;

/**
 *
 * @author fernando
 */
public abstract class AbstractSwingValidator {
    
    private String message;
    
    protected AbstractSwingValidator(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public abstract boolean validate();

}