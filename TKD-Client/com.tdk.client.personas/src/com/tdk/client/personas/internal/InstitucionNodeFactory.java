/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.internal;

import com.tdk.client.personas.institucion.InstitucionNode;
import com.tdk.client.personas.institucion.InstitucionRootNode;
import com.tdk.domain.Institucion;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingNode;

/**
 *
 * @author fernando
 */
public class InstitucionNodeFactory implements NodeFactory<Institucion> {

    public SwingNode<Institucion> createNode(Institucion institucion) {
        return new InstitucionNode(institucion);
    }

    public ChoiceRootNode createRootNode(StrategySearchPattern<? extends Institucion> strategy) {
        return new InstitucionRootNode(strategy);
    }

    public Institucion getObject(SwingNode node) {
        if (node instanceof InstitucionNode)
            return (Institucion) node.getValue();
        return null;
    }

    public Class getNodeSelectedClass() {
        return InstitucionNode.class;
    }

    public Class getRegisterClass() {
        return Institucion.class;
    }

    public ChoiceRootNode createRootNode() {
        return new InstitucionRootNode();
    }

}
