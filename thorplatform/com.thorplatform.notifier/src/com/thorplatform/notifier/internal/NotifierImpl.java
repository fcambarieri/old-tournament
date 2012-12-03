/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.notifier.internal;

import com.thorplatform.notifier.Notifier;
import com.thorplatform.notifier.NotifierEvent;
import com.thorplatform.notifier.NotifierObject;
import com.thorplatform.notifier.client.ClientNotyfier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;

/**
 *
 * @author sabon
 */
public class NotifierImpl implements Notifier {

    private static final Logger logger = Logger.getLogger(NotifierImpl.class.getName());
    private ExecutorService executor = Executors.newFixedThreadPool(30);
    private List<NotifierObject> notiferObjectsList = Collections.synchronizedList(new ArrayList<NotifierObject>());
    private ClientNotyfier clients = Lookup.getDefault().lookup(ClientNotyfier.class);

    @Override
    public synchronized void mark(Class className) {
        mark(className, null);
    }

    @Override
    public synchronized void mark(Class className, Object object) {
        mark(className, NotifierEvent.ALL, object);
    }

    @Override
    public synchronized void mark(Class className, NotifierEvent event, Object object) {
        NotifierObject no = new NotifierObject(event, className, object);
        if (!this.notiferObjectsList.contains(no)) {
            this.notiferObjectsList.add(no);
            this.logger.info("Mark on:" + className.getName());
        } else {
            this.logger.info("Mark " + className.getName() + " allready exists!");
        }
    }

    @Override
    public void notifyAllListener() {
        if (clients == null) {
            logger.info("ClientNotyfier does not been implemented!");
            return;
        }
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (NotifierObject no : notiferObjectsList) {
                            clients.fireNotifierListener(no);
                        }
                    } catch (Throwable e) {
                        logger.log(Level.SEVERE, "Notifing clients", e);
                    }
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al notificar", e);
        } finally {
        }
    }
}
