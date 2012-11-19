package com.thorplatform.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Fernando
 */
public class Property<T> {
    
    private String name;
    private T value;
    private PropertyChangeSupport propertyChangeSupport;
    
    public Property(String name) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public T get() {
        return value;
    }
    
    public void set(T newValue) {
        T oldValue = get();
        value = newValue;
        propertyChangeSupport.firePropertyChange(name, oldValue, newValue);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(name, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
