/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.api.internal;

import com.tdk.client.api.ServiceFactory;
import com.thorplatform.login.LoginClient;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class ServiceFactoryImp implements ServiceFactory{

    private LoginClient loginClient;
    private Map<Class, Object> serviceCache;
    
    public ServiceFactoryImp() {
        loginClient = Lookup.getDefault().lookup(LoginClient.class);
        serviceCache = new HashMap<Class, Object>();
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> className) {
        Object o = serviceCache.get(className);
        if (o == null) {
            o = loginClient.getService(className);
            serviceCache.put(className, o);
        }
        return (T) o;
    }

}
