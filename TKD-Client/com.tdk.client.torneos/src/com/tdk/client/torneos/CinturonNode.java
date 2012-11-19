/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos;

import com.tdk.domain.torneo.Cinturon;
import com.thorplatform.swing.SwingNode;

/**
 *
 * @author fernando
 */
public class CinturonNode extends SwingNode<Cinturon>{

    public CinturonNode(Cinturon cinturon){
        super(cinturon);
        setIconBaseWithExtension("com/tdk/client/torneos/cinturones-16x16.png");
    }
    
    @Override
    public String getDisplay() {
        return getValue().getDescripcion();
    }

}
