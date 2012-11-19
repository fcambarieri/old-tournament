/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

/**
 * <P> Está clase solo tiene un método que devuelve la instancia de un nuevo 
 * binder<P>
 * @author fernando
 */
public interface SwingBinderFactory {

    /**
     * Devuelve la instacia de un nuevo binder
     * @return SwingBinder
     */
    SwingBinder createBinder();
    
}
