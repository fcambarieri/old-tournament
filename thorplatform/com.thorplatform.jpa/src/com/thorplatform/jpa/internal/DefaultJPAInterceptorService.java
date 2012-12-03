package com.thorplatform.jpa.internal;

import com.thorplatform.jpa.JPAInterceptor;
import com.thorplatform.jpa.JPAInterceptorService;
import com.thorplatform.jpa.JPAService;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;

public class DefaultJPAInterceptorService implements JPAInterceptorService {

    private HashMap<Class<? extends JPAService>, JPAInterceptor> trackers;
    private static final Logger logger = Logger.getLogger(DefaultJPAInterceptorService.class.getName());

    public DefaultJPAInterceptorService() {
        this.trackers = new HashMap();

        Lookup.Template template = new Lookup.Template(JPAInterceptor.class);
        Lookup.Result result = Lookup.getDefault().lookup(template);
        Collection instances = result.allInstances();
        for (Iterator iterator = instances.iterator(); iterator.hasNext();) {
            JPAInterceptor tracker = (JPAInterceptor) iterator.next();
            this.trackers.put(tracker.getInterceptedLocalServiceClass(), tracker);
        }
    }

    @Override
    public void preIntercept(JPAService localService, Method method, Object[] args) {
        JPAInterceptor tracker = (JPAInterceptor) this.trackers.get(localService.getClass());
        if (tracker != null) {
            tracker.preIntercept(localService, method, args);
        } else {
            logger.log(Level.INFO, "{0} {1}", new Object[]{localService.getClass().getSimpleName(), method.getName()});
        }
    }
}
