/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.notifier.internal;

import com.thorplatform.notifier.AbstractNotifier;
import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.extra.Queue;
import java.rmi.RemoteException;
import org.openide.util.Exceptions;

/**
 *
 * @author sabon
 */
public class QueueMessageNotifier extends AbstractNotifier {

    private Queue queue;

    public QueueMessageNotifier() {
        queue = new Queue("domain");
        
        try {
            Remote.exportObject(new Remote(queue), 1199);
        } catch (RemoteException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Override
    public void notifyAllListener() {
    }
    
}
