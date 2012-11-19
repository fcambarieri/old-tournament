/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.internal;

import com.tdk.client.personas.institucion.AlumnoNode;
import com.tdk.client.personas.institucion.AlumnoRootNode;
import com.tdk.domain.Alumno;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingNode;

/**
 *
 * @author fernando
 */
public class AlumnoNodeFactory implements NodeFactory<Alumno>{

    public SwingNode<Alumno> createNode(Alumno alumno) {
        return new AlumnoNode(alumno);
    }

    public ChoiceRootNode createRootNode() {
        return new AlumnoRootNode();
    }

    public ChoiceRootNode createRootNode(StrategySearchPattern<? extends Alumno> strategy) {
        return new AlumnoRootNode(strategy);
    }

    public Alumno getObject(SwingNode node) {
        if (node instanceof AlumnoNode) {
            return (Alumno) node.getValue();
        }
        return null;
    }

    public Class getNodeSelectedClass() {
        return AlumnoNode.class;
    }

    public Class getRegisterClass() {
        return Alumno.class;
    }

}
