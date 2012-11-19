/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.utils.observer;

/**
 *
 * @author Fernando
 */
public interface ClientObservable {

    void addObserver(Class classToObserver, Observer o);
    
    void removeObserver(Observer o);
    
    void notifyObservers(Message message);
    
    void clearAllObservers();
    
    void clearObservers(Class classToObserver);
    
}
