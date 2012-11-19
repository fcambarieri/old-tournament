/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.utils.observer.impl;

import com.thorplatform.utils.observer.ClientObservable;
import com.thorplatform.utils.observer.Message;
import com.thorplatform.utils.observer.ServerObservable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fernando
 */
public class ServerObservableImpl implements ServerObservable {

    private List<ClientObservable> clients = new ArrayList<ClientObservable>();
    private List<Message> messages = new ArrayList<Message>();

    public synchronized void doMark() {
        for (Message m : messages) {
            for (ClientObservable c : clients) {
                c.notifyObservers(m);
            }
        }
        
        messages.clear();
    }

    public synchronized void addClientObservable(ClientObservable client) {
        clients.add(client);
    }

    public synchronized void removeClientObservable(ClientObservable client) {
        clients.remove(client);
    }

    public synchronized void mark(Message message) {
        messages.add(message);
    }

    public void clearMarks() {
        synchronized(messages) {
            messages.clear();
        }
    }

}
