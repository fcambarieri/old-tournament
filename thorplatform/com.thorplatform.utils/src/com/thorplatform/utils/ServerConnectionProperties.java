/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.utils;

/**
 *
 * @author sabon
 */
public interface ServerConnectionProperties {

    enum Key {
        PORT,
        HOST,
        SERVICE_NAME,
        CONNECTION
    }
    
    String get(Key k);
    
    void set(Key k, String value);
    
    String getServerConnection() ;
    
    void save();
}
