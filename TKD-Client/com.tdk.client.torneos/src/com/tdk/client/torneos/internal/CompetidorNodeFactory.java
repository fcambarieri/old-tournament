/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos.internal;

import com.tdk.client.torneos.competidor.CompetidorNode;
import com.tdk.domain.torneo.Competidor;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingNode;

/**
 *
 * @author fernando
 */
public class CompetidorNodeFactory implements NodeFactory<Competidor>{

    public SwingNode<Competidor> createNode(Competidor comp) {
        return new CompetidorNode(comp);
    }

    public ChoiceRootNode createRootNode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ChoiceRootNode createRootNode(StrategySearchPattern<? extends Competidor> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Competidor getObject(SwingNode node) {
        if (node instanceof CompetidorNode) {
            return ((CompetidorNode)node).getValue();
        } else {
            return null;
        }
    }

    public Class getNodeSelectedClass() {
        return CompetidorNode.class;
    }

    public Class getRegisterClass() {
        return Competidor.class;
    }


}
