/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.personas.internal;

import com.tdk.client.personas.PersonaNode;
import com.tdk.client.personas.PersonaRootNode;
import com.tdk.domain.Persona;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingNode;

/**
 *
 * @author fernando
 */
public class PersonaNodeFactory implements NodeFactory<Persona> {

    
    public PersonaNodeFactory() {
        System.out.println("Me Inicie " + getClass().getName());
    }
    
    public SwingNode<Persona> createNode(Persona persona) {
        return new PersonaNode(persona);
    }

    public ChoiceRootNode createRootNode(StrategySearchPattern<? extends Persona> strategy) {
        return new PersonaRootNode(strategy);
    }

    public Persona getObject(SwingNode node) {
        if (node instanceof PersonaNode) {
            return (Persona) node.getValue();
        }
        return null;
    }

    public Class getNodeSelectedClass() {
        return PersonaNode.class;
    }

    public Class getRegisterClass() {
        return Persona.class;
    }

    public ChoiceRootNode createRootNode() {
       return new PersonaRootNode();
    }
}
