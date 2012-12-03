package com.thorplatform.server;

import com.thorplatform.jpa.JPAInvocationHandler;
import com.thorplatform.jpa.JPALocalServiceFactory;
import com.thorplatform.jpa.JPAService;
import com.thorplatform.login.WorkStation;
import gnu.cajo.invoke.Remote;
import java.lang.reflect.Proxy;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RemotableServiceFactory {

    private JPALocalServiceFactory jpaLocalServiceFactory;
    private Object operator;
    private Object workPlace;
    private WorkStation estacionDeTrabajo;
    private Log log = LogFactory.getLog(this.getClass());

    protected RemotableServiceFactory() {
        this(null);
    }

    protected RemotableServiceFactory(JPALocalServiceFactory jpaLocalServiceFactory) {
        this.jpaLocalServiceFactory = jpaLocalServiceFactory;
    }

    public Object getService(Class serviceClass) {
        Proxy proxy = (Proxy) getJPALocalServiceFactory().getService(serviceClass);
        JPAInvocationHandler invocationHandler = (JPAInvocationHandler) Proxy.getInvocationHandler(proxy);
        JPAService localService = invocationHandler.getLocalService();

        localService.setProperty("operador", getUser());
        localService.setProperty("workStation", getWorkStation());
        localService.setProperty("workPlace", getWorkPlace());

        log.info("User: " + getUser().toString() + " - WorkStation: " + getWorkStation().toString() + " - WorkPlace: " + getWorkPlace().toString());
        try {
            return new Remote(proxy);
        } catch (Throwable t) {
            log.error("Creation remote", t);
            throw new RuntimeException(t);
        }
    }

    public JPALocalServiceFactory getJPALocalServiceFactory() {
        return getJpaLocalServiceFactory();
    }

    public Object getUser() {
        return this.operator;
    }

    public void setUser(Object operator) {
        this.operator = operator;
    }

    public Object getWorkPlace() {
        return this.workPlace;
    }

    public void setWorkPlace(Object workPlace) {
        this.workPlace = workPlace;
    }

    public WorkStation getWorkStation() {
        return this.estacionDeTrabajo;
    }

    public void setWorkStation(WorkStation workStation) {
        this.estacionDeTrabajo = workStation;
    }

    public abstract List<?> recuperarPermisos(String paramString);

    public JPALocalServiceFactory getJpaLocalServiceFactory() {
        return this.jpaLocalServiceFactory;
    }

    public void setJpaLocalServiceFactory(JPALocalServiceFactory jpaLocalServiceFactory) {
        this.jpaLocalServiceFactory = jpaLocalServiceFactory;
    }
    
    protected Log getLog() {
        return this.log;
    }
}
