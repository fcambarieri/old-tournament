/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import org.openide.util.Lookup;

/**
 *
 * @author sabon
 */
public interface NodeNotifier {
    
    enum NodeNotifierEvent {
        ADD,
        REMOVE,
        UPDATE
    }
    
    void notify(Object object, NodeNotifierEvent event);
    
    Lookup.Result findLookup(Class c);
    
}
