/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.utils.internal;

import com.tdk.client.utils.VigenciaControllerFactory;
import com.tdk.client.utils.VigenciaInterface;
import com.thorplatform.swing.SwingControllerFactory;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class VigenciaControllerFactoryImp implements VigenciaControllerFactory{

    private SwingControllerFactory swingFactory = null;
    
    public VigenciaControllerFactoryImp() {
        swingFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);
    }
    
    public VigenciaInterface createVigenciaInterface() {
        return swingFactory.createController(VigenciaController.class);
    }

}
