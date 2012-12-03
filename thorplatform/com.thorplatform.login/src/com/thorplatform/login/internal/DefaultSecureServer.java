package com.thorplatform.login.internal;

import com.thorplatform.login.LoginClient;
import com.thorplatform.login.WorkStation;
import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.extra.TransparentItemProxy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Exceptions;

public class DefaultSecureServer implements LoginClient {

    private Object loginServerRef = null;
    private Object remotableServiceFactoryRef = null;
    private Map<String, List<Object>> permisos;
    private Object workPlace;
    private Object operador;

    public DefaultSecureServer() {
        this.permisos = new HashMap();
    }

    private WorkStation generateWorkStation() {
        WorkStation e = new WorkStation();
        e.setIp(getDireccionIP());
        e.setName(getHostname());
        e.setOS(System.getProperty("os.name"));
        e.setOSVersion(System.getProperty("os.version"));
        e.setWorkStationUserName(System.getProperty("user.name"));
        return e;
    }

    @Override
    public void login(String loginServer, String usuario, String password, String workPlace) {
        try {
            this.loginServerRef = Remote.getItem(loginServer);
            this.remotableServiceFactoryRef = Remote.invoke(this.loginServerRef, "login", new Object[]{usuario, password, workPlace, generateWorkStation()});

            recuperarUserFromServer();
            recuperarWorkPlaceFromServer();
        } catch (Throwable t) {
            this.loginServerRef = null;
            Exceptions.printStackTrace(t);
            throw new RuntimeException(t);
        }
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        try {
            Object remoteService = TransparentItemProxy.getItem(invokeMethod("getService", serviceClass), new Class[]{serviceClass});
            return (T) remoteService;
        } catch (Throwable t) {
            Exceptions.printStackTrace(t);
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
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private String getDireccionIP() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
                NetworkInterface ni = NetworkInterface.getByName("eth0");

                Enumeration<InetAddress> ias = ni.getInetAddresses();

                InetAddress iaddress;
                do {
                    iaddress = ias.nextElement();
                } while (!(iaddress instanceof Inet4Address));

                return iaddress.getHostAddress();
            }

            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnknownHostException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
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

    @Override
    public <T> T getOperador(Class<T> claseOperador) {
        return (T) this.operador;
    }
}
