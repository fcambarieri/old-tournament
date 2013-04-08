/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves;

import com.tdk.client.nodes.BinaryNode;
import com.tdk.domain.torneo.Competidor;

/**
 *
 * @author fernando
 */
public class CompetidorBNode extends BinaryNode<Competidor> {

    public CompetidorBNode(Competidor c) {
        super(c);
    }
    
    @Override
    public void setUserObject(Object userObject) {
        super.setUserObject(userObject);
    }

    @Override
    public String getDisplay() {
        if (getUserObject() != null) {
            return getUserObject().getDisplayCompetidor();
        } else {
            //return "Sin competidor";
            return "";
        }
    }

    @Override
    public String toString() {
        return getDisplay();
    }
    
    
}
