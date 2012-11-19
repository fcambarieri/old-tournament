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
public class NumberPropertyValidator extends AbstractSwingPropertyValidator {

    private Property<? extends Number> numberProperty;
    private Number maxValue;
    private Number minValue;

    public NumberPropertyValidator(Property<? extends Number> numberProperty, String message) {
        this(numberProperty, message, null, null);
    }

    public NumberPropertyValidator(Property<? extends Number> numberProperty, String message, Number minValue, Number maxValue) {
        super(numberProperty, message);
        this.numberProperty = numberProperty;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Property<? extends Number> getProperty() {
        return numberProperty;
    }

    public boolean validate() {
        boolean required = getProperty().get() != null;

        boolean val = required;

        if (required) {
            if (getProperty().get().getClass().isAssignableFrom(Integer.class)) {
                val = validateInteger();
            } else if (getProperty().get().getClass().isAssignableFrom(Long.class)) {
                val = validateLong();
            }
        }

        return required && val;
    }

    private boolean validateInteger() {
        boolean max = ((maxValue == null) || (getProperty().get().intValue() <= maxValue.intValue()));
        boolean min = ((minValue == null) || (getProperty().get().intValue() >= minValue.intValue()));
        return min && max;

    }

    private boolean validateLong() {
        boolean max = ((maxValue == null) || (getProperty().get().longValue() <= maxValue.longValue()));
        boolean min = ((minValue == null) || (getProperty().get().longValue() >= minValue.longValue()));
        return min && max;
    }
}
