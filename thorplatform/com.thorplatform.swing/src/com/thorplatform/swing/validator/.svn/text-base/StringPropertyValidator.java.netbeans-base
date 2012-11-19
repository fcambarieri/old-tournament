/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.validator;

import com.thorplatform.swing.Property;

/**
 *
 * @author fernando
 */
public class StringPropertyValidator extends AbstractSwingPropertyValidator {

    private Integer minLength;
    private Integer maxLength;
    private Property<String> property;
    
    public StringPropertyValidator(Property<String> property, String message) {
        this(property, message,null,null);
        
    }
    
    public StringPropertyValidator(Property<String> property, String message, Integer minLength, Integer maxLength) {
        super(property, message);
        this.property = property;
        this.maxLength = maxLength;
        this.minLength = minLength;
    }
    
    public boolean validate() {
        boolean required = getProperty().get() != null;
        
        boolean valMin = required && getProperty().get().trim().length() > 0;
        boolean valMax = valMin;
                
        if (required) {
            valMin = (getMinLength() == null) || (getProperty().get().trim().length() >= getMinLength().intValue());
            valMax = (getMaxLength() == null) || (getProperty().get().trim().length() <= getMaxLength().intValue());
        }
        
        return required && valMin && valMax;
            
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Property<String> getProperty() {
        return property;
    }

}
