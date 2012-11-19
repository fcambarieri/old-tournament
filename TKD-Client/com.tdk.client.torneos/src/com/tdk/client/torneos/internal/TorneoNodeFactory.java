/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos.internal;

import com.tdk.client.torneos.TorneoNode;
import com.tdk.client.torneos.TorneoRootNode;
import com.tdk.domain.torneo.Torneo;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingNode;

/**
 *
 * @author fernando
 */
public class TorneoNodeFactory implements NodeFactory<Torneo>{

    public SwingNode<Torneo> createNode(Torneo torneo) {
        return new TorneoNode(torneo);
    }

    public ChoiceRootNode createRootNode() {
        return new TorneoRootNode();
    }

    public ChoiceRootNode createRootNode(StrategySearchPattern<? extends Torneo> strategy) {
        TorneoRootNode rootNode = new TorneoRootNode();
        rootNode.setStrategySearchTorneo(strategy);
        return rootNode;
    }

    public Torneo getObject(SwingNode node) {
        if (node instanceof TorneoNode) {
            return (Torneo) node.getValue();
        }
        return null;
    }

    public Class getNodeSelectedClass() {
        return TorneoNode.class;
    }

    public Class getRegisterClass() {
        return Torneo.class;
    }

}
