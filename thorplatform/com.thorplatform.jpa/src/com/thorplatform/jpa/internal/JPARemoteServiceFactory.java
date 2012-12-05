package com.thorplatform.jpa.internal;

import com.thorplatform.jpa.JPAServiceFactory;
import gnu.cajo.utils.extra.TransparentItemProxy;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;

public class JPARemoteServiceFactory implements JPAServiceFactory {

    private String serverUrl;
    private HashMap<Class, Object> serviceInstanceCache;

    public JPARemoteServiceFactory() {
        this.serviceInstanceCache = new HashMap();
        String prop = System.getProperty("serverHost");
        if (prop == null) {
            this.serverUrl = "//localhost:1198";
        } else {
            this.serverUrl = ("//" + prop + ":1198");
        }
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        try {
            return (T) lookupService(getUrl(serviceClass), serviceClass);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }

    private String getUrl(Class serviceClass) {
        return this.serverUrl + "/" + serviceClass.getName();
    }

    private Object lookupService(String serviceUrlString, Class serviceClass) throws Exception {
        Object serviceInstance = this.serviceInstanceCache.get(serviceClass);
        if (serviceInstance == null) {
            serviceInstance = TransparentItemProxy.getItem(serviceUrlString, new Class[]{serviceClass});
            this.serviceInstanceCache.put(serviceClass, serviceInstance);
        }

        return serviceInstance;
    }
}