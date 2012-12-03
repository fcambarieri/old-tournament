package com.thorplatform.jpa;

import com.thorplatform.notifier.Notifier;
import com.thorplatform.utils.DateTimeUtils;
import com.thorplatform.utils.NumericUtils;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import org.openide.util.Lookup;

public class JPALocalServiceFactory implements JPAServiceFactory {

    private EntityManagerFactory entityManagerFactory;
    private static HashMap<Class, Class> services;
    private NumericUtils numericUtils;
    private DateTimeUtils dateTimeUtils;
    private Notifier notifier;
    private JPAInterceptorService interceptorService;

    public JPALocalServiceFactory() {
        services = new HashMap();
        this.numericUtils = ((NumericUtils) Lookup.getDefault().lookup(NumericUtils.class));
        this.dateTimeUtils = ((DateTimeUtils) Lookup.getDefault().lookup(DateTimeUtils.class));
        this.interceptorService = ((JPAInterceptorService) Lookup.getDefault().lookup(JPAInterceptorService.class));
        this.notifier = ((Notifier) Lookup.getDefault().lookup(Notifier.class));
    }

    public void setEntityManagerFactory(EntityManagerFactory value) {
        this.entityManagerFactory = value;
    }

    public void defineService(Class serviceClass, Class implementationClass) {
        services.put(serviceClass, implementationClass);
    }

    @Override
    public <T> T getService(Class<T> serviceInterfaceClass) {
        try {
            Class localServiceClass = (Class) services.get(serviceInterfaceClass);
            JPAService localService = (JPAService) localServiceClass.newInstance();
            localService.setNumericUtils(this.numericUtils);
            localService.setDateTimeUtils(this.dateTimeUtils);
            localService.setNotifier(this.notifier);

            Class[] proxiedInterfaces = {serviceInterfaceClass};
            ClassLoader classLoader = serviceInterfaceClass.getClassLoader();
            JPAInvocationHandler jpaProxyHandler = new JPAInvocationHandler();
            jpaProxyHandler.setLocalService(localService);
            jpaProxyHandler.setEntityManagerFactory(this.entityManagerFactory);
            jpaProxyHandler.setInterceptorService(this.interceptorService);

            return (T) Proxy.newProxyInstance(classLoader, proxiedInterfaces, jpaProxyHandler);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        
    }
}