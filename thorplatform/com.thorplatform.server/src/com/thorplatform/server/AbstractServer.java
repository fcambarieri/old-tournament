package com.thorplatform.server;

import com.thorplatform.jpa.JPALocalServiceFactory;
import com.thorplatform.server.api.AbstractLoginServer;
import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.TimeZone;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openide.util.Lookup;

public abstract class AbstractServer {

    protected JPALocalServiceFactory jpaLocalServiceFactory;
    protected ServerConfig serverConfig;
    protected JPAConfig jpaConfig;
    private String serverName;
    protected boolean isSecure;
    protected Log logger = LogFactory.getLog(this.getClass().getName());

    protected AbstractServer(String serverName) {
        this.serverName = serverName;
        this.serverConfig = Lookup.getDefault().lookup(ServerConfig.class);
        this.jpaConfig = Lookup.getDefault().lookup(JPAConfig.class);
    }

    private void configurePersistenceUnit() {
        this.jpaLocalServiceFactory = new JPALocalServiceFactory();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersisnteceUnitName(), getPersisnteceConfiguration());
        this.jpaLocalServiceFactory.setEntityManagerFactory(emf);
    }

    protected void configureRMIServer() throws NumberFormatException, UnknownHostException {
        String serverInterface = this.serverConfig.get("server_interface");
        int serverPort = Integer.parseInt(this.serverConfig.get("server_port"));
        Remote.config(serverInterface, serverPort, serverInterface, serverPort);
    }

    protected void bindServices(Object object, String name) throws Exception {
        ItemServer.bind(object, name);
    }

    protected void exportService(Class serviceClass) throws Exception {
        ItemServer.bind(this.jpaLocalServiceFactory.getService(serviceClass), serviceClass.getName());
    }

    protected void defineService(Class remoteClass, Class beanClass) {
        if (this.jpaLocalServiceFactory == null) {
            logger.error("The JpaLocalServiceFactory is null");
            throw new NullPointerException("The JpaLocalServiceFactory is null");
        }
        this.jpaLocalServiceFactory.defineService(remoteClass, beanClass);
    }

    private void configureTimeZone() {
        if (this.serverConfig.get("zonaHoraria") != null) {
            TimeZone.setDefault(TimeZone.getTimeZone(this.serverConfig.get("zonaHoraria")));
        } else {
            TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        }
    }

    private void startServer() throws NumberFormatException, UnknownHostException {

        bindShutdownHook();
        try {
            logger.info(String.format("Iniciando {0} Application Server...", this.serverName));
            loadConfig();
            configureTimeZone();
            configureRMIServer();
            configurePersistenceUnit();
            configureServices();
            logger.info(String.format("{0} Application Server iniciado...", this.serverName));
        } catch (Throwable e) {
            logger.error("The application could not start. Shutting down!", e);
            stopServer();
        }
    }

    public void configureServices() {
        defineAllServices();
        try {
            if (this.isSecure) {
                bindLoginServer(defineLoginServer());
            } else {
                bindAllServices();
            }
        } catch (RemoteException ex) {
            logger.error("Fail binding services", ex);
            stopServer();
        }
    }

    public void stopServer() {
        logger.info(String.format("Stopping {0} Application Server ...", this.serverName));
        try {
            for (String name : ItemServer.list()) {
                ItemServer.unbind(name);
                logger.info(String.format("Unbind service {0} ", name));
            }
            Remote.shutdown();
        } catch (Exception e) {
            logger.error("Fail unbinding services", e);
        }

        logger.info(String.format("Exit from {0} Application Server ...", this.serverName));

        System.exit(0);
    }

    private void bindShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    AbstractServer.this.stopServer();
                } catch (Throwable t) {
                    logger.error("Fail stopping server", t);
                }
            }
        });
    }

    public void runServer(String[] args) throws NumberFormatException, UnknownHostException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (args.length == 0) {
            startServer();
        } else if (args[0].equals("start")) {
            startServer();
        } else if (args[0].equals("stop")) {
            stopServer();
        } else {
            Runnable runnable = (Runnable) Class.forName(args[0]).newInstance();
            new Thread(runnable).start();
        }
    }

    private void bindLoginServer(AbstractLoginServer loginServer) throws RemoteException {
        try {
            if (loginServer == null) {
                throw new NullPointerException("Esta intentando iniciar en forma segura y no hay un loginServer definido");
            }
            loginServer.setJpaLocalServiceFactory(this.jpaLocalServiceFactory);
            ItemServer.bind(loginServer, "loginServer");
        } catch (IOException ex) {
            logger.error("Binding loginServer", ex);
        }
    }

    protected abstract void loadConfig();

    protected abstract Map getPersisnteceConfiguration();

    protected abstract String getPersisnteceUnitName();

    protected abstract AbstractLoginServer defineLoginServer();

    protected abstract void defineAllServices();

    protected abstract void bindAllServices();
}
