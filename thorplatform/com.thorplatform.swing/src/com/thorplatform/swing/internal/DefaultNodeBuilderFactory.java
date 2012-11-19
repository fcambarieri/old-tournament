/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.internal;

import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.NodeBuilderFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class DefaultNodeBuilderFactory implements NodeBuilderFactory{

    private Map<Class, NodeFactory> nodesChoiceField;
    
    @SuppressWarnings("unchecked")
    public DefaultNodeBuilderFactory() {
        nodesChoiceField = new HashMap<Class, NodeFactory>();
        Lookup.Template template = new Lookup.Template(NodeFactory.class);
        Lookup.Result result = Lookup.getDefault().lookup(template);
        Collection instances = result.allInstances();
        for (Iterator iterator = instances.iterator(); iterator.hasNext();) {
           NodeFactory n = (NodeFactory) iterator.next();
           nodesChoiceField.put(n.getRegisterClass(), n);
        }
    }
    
    public NodeFactory createNodeFactory(Class object) {
        return nodesChoiceField.get(object);
    }

}
