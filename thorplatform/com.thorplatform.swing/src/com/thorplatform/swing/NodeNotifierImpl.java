/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author sabon
 */
@ServiceProvider(service=NodeNotifier.class)
public class NodeNotifierImpl extends ProxyLookup implements NodeNotifier, Observer {

    private ScheduledExecutorService EX = 
                Executors.newScheduledThreadPool(10);
    

    public void notify(final Object object) {
       EX.execute(new Runnable() {

            public void run() {
                setLookups(Lookups.singleton(object));
            }
        });
    }

    public Result findLookup(Class c) {
        return lookupResult(c);
    }

    public void update(Observable o, Object arg) {
        notify(arg);
    }

    public void notify(Object object, NodeNotifierEvent event) {
    }

}
