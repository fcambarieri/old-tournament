/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.utils.observer.impl;

import com.thorplatform.utils.observer.ClientObservable;
import com.thorplatform.utils.observer.Message;
import com.thorplatform.utils.observer.Observer;
import com.thorplatform.utils.observer.ServerObservable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openide.util.Lookup;

/**
 *
 * @author Fernando
 */
public class ClientObservableImpl implements ClientObservable {

    private Map<String, List<Observer>> listeners = new HashMap<String, List<Observer>>();

    public ClientObservableImpl() {
            ServerObservable server = Lookup.getDefault().lookup(ServerObservable.class);
            server.addClientObservable(this);
    }

    public synchronized void addObserver(Class classToObserver, Observer o) {
        if (!listeners.containsKey(classToObserver.getName())) {
            listeners.put(classToObserver.getName(), new ArrayList<Observer>());
        }
        listeners.get(classToObserver.getName()).add(o);
    }

    public synchronized void removeObserver(Observer o) {
        Iterator<String> it = listeners.keySet().iterator();
        List<Observer> obs = null;
        boolean exit = false;
        while (!exit && it.hasNext()) {
            obs = listeners.get(it.next());
            exit = obs.contains(o);
        }

        if (exit) {
            obs.remove(o);
        }
    }

    public void notifyObservers(Message message) {
        if (listeners.containsKey(message.getClassName().getName())) {
            Notifier notifier = new Notifier(listeners.get(message.getClassName().getName()), message);

            new Thread(notifier).start();
        }
    }

    public void clearAllObservers() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    public void clearObservers(Class classToObserver) {
        if (listeners.containsKey(classToObserver.getName())) {
            listeners.remove(classToObserver.getName());
        }
    }

    class Notifier implements Runnable {

        private List<Observer> obs;
        private Message message;

        public Notifier(List<Observer> listeners, Message msg) {
            this.obs = listeners;
            this.message = msg;
        }

        public void run() {
            for (Observer o : obs) {
                o.update(message);
            }
        }
    }
}
