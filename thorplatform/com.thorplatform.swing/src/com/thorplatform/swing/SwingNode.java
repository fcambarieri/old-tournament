/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author fernando
 */
public abstract class SwingNode<T> extends AbstractNode {

    private T value;

    public SwingNode(T t) {
        this(Children.LEAF, t);
    }

    public SwingNode(Children children, T t) {
        this(children, t, Lookups.singleton(t));
    }

    public SwingNode(Children children, T value, Lookup lookup) {
        super(children, lookup);
        this.value = value;
    }

    public SwingNode(T t, Lookup lookup) {
        super(Children.LEAF, lookup);
        this.value = t;
    }
    
    
    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    
    public abstract String getDisplay();

    @Override
    public String getDisplayName() {
        return getDisplay();
    }

    @Override
    public String toString() {
        if (value != null)
            return value.toString();
        return "Sin valor";
    }
}
