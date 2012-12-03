package com.thorplatform.server.api;

import com.thorplatform.jpa.JPALocalServiceFactory;
import com.thorplatform.login.WorkStation;
import com.thorplatform.server.LoginServer;
import com.thorplatform.server.RemotableServiceFactory;
import com.thorplatform.server.ThroServerException;
import gnu.cajo.invoke.Remote;
import java.rmi.RemoteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractLoginServer implements LoginServer {

    private JPALocalServiceFactory jpaLocalServiceFactory;
    protected Log log = LogFactory.getLog(this.getClass().getName());

    protected AbstractLoginServer(JPALocalServiceFactory jpaLocalServiceFactory) {
        this.jpaLocalServiceFactory = jpaLocalServiceFactory;
    }

    public Object login(String user, String password, String workPlace, WorkStation estacionDeTrabajo) {
        Object operator = validate(user, password);

        if (operator == null) {
            log.error("Invalid user or password");
            throw new ThroServerException("Invalid user or password.");
        }
        RemotableServiceFactory remotableServiceFactory = getRemotableServiceFactory();
        remotableServiceFactory.setJpaLocalServiceFactory(this.jpaLocalServiceFactory);
        remotableServiceFactory.setUser(operator);
        remotableServiceFactory.setWorkPlace(getWorkPlace(workPlace));
        remotableServiceFactory.setWorkStation(estacionDeTrabajo);
        try {
            return new Remote(remotableServiceFactory);
        } catch (RemoteException ex) {
            log.error("Fail creation remotableServiceFactory", ex);
            throw new ThroServerException(ex);
        }
    }

    public JPALocalServiceFactory getJpaLocalServiceFactory() {
        return this.jpaLocalServiceFactory;
    }

    public void setJpaLocalServiceFactory(JPALocalServiceFactory jpaLocalServiceFactory) {
        this.jpaLocalServiceFactory = jpaLocalServiceFactory;
    }

    public abstract RemotableServiceFactory getRemotableServiceFactory();
}
