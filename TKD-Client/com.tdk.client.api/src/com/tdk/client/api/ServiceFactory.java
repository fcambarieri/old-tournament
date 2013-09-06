/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.api;

/**
 *
 * @author fernando
 */
public interface ServiceFactory {
    
    <T> T getService(Class<T> className);
    
    boolean authenticated();
    
}
