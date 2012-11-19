package com.thorplatform.login.internal;

import com.thorplatform.login.EstacionDeTrabajo;
import com.thorplatform.login.LoginClient;
import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.extra.TransparentItemProxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Exceptions;

public class DefaultSecureServer
        implements LoginClient {

    private Object loginServerRef = null;
    private Object remotableServiceFactoryRef = null;
    private Map<String, List<Object>> permisos;
    private Object workPlace;
    private Object operador;

    public DefaultSecureServer() {
        this.permisos = new HashMap();
    }

    private EstacionDeTrabajo generateEstacionDeTrabajo() {
        EstacionDeTrabajo e = new EstacionDeTrabajo();
        e.setIp(getDireccionIP());
        e.setName(getHostname());
        e.setOS(System.getProperty("os.name"));
        e.setOSVersion(System.getProperty("os.version"));
        e.setWorkStationUserName(System.getProperty("user.name"));

        return e;
    }

    public void login(String loginServer, String usuario, String password, String workPlace) {
        try {
            this.loginServerRef = Remote.getItem(loginServer);
            this.remotableServiceFactoryRef = Remote.invoke(this.loginServerRef, "login", new Object[]{usuario, password, workPlace, generateEstacionDeTrabajo()});

            recuperarUserFromServer();
            recuperarWorkPlaceFromServer();
        } catch (Throwable t) {
            this.loginServerRef = null;
            throw new RuntimeException(t);
        }
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        try {
            Object remoteService = TransparentItemProxy.getItem(invokeMethod("getService", serviceClass), new Class[]{serviceClass});
            return (T) remoteService;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }

    private Object invokeMethod(String method, Object object) throws Exception {
        if (this.loginServerRef == null) {
            throw new RuntimeException("Debe autenticarse antes de obtener servicios.");
        }
        return Remote.invoke(this.remotableServiceFactoryRef, method, object);
    }

    private String getHostname() {
        try {
            InetAddress a = InetAddress.getLocalHost();

            return a.getHostName();
        } catch (UnknownHostException ex) {
        }
        return null;
    }

    private String getDireccionIP() {
        try {
            InetAddress a = InetAddress.getLocalHost();

            return a.getHostAddress();
        } catch (UnknownHostException ex) {
        }
        return null;
    }

    public List getPermisionList(String funcionalidad) {
        List permisosList = (List) this.permisos.get(funcionalidad);
        if (permisosList == null) {
            try {
                permisosList = (List) invokeMethod("recuperarPermisos", funcionalidad);
                this.permisos.put(funcionalidad, permisosList);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return permisosList;
    }

    public Object getWorkPlace() {
        return this.workPlace;
    }

    private void recuperarUserFromServer() {
        try {
            this.operador = invokeMethod("getUser", null);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void recuperarWorkPlaceFromServer() {
        try {
            this.workPlace = invokeMethod("getWorkPlace", null);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public <T> T getOperador(Class<T> claseOperador) {
        return (T) this.operador;
    }
}
