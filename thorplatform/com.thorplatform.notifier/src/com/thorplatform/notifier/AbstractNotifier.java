/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.notifier;

import com.thorplatform.notifier.internal.NotifierImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author sabon
 */
public abstract class AbstractNotifier implements Notifier {

    private static final Logger logger = Logger.getLogger(NotifierImpl.class.getName());
    private List<NotifierObject> notiferObjectsList = Collections.synchronizedList(new ArrayList<NotifierObject>());

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

}
