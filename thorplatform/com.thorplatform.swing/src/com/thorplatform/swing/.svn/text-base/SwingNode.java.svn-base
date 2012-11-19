/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

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
        super(children);
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
