/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.security.internal;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.Usuario;
import com.tdk.services.SecurityServiceRemote;
import com.thorplatform.login.LoginClient;
import com.thorplatform.swing.actions.ValidatePermision;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class ValidatePermisionImp implements ValidatePermision {

    public SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

    private LoginClient getLoginClient() {
        return Lookup.getDefault().lookup(LoginClient.class);
    }

    @SuppressWarnings("unchecked")
    public boolean getAccess(String funcionalidad, Object acceso) {
           if (isLogin()) {
            List<Acceso> accesos = getLoginClient().getPermisionList(funcionalidad);
            return ((accesos != null) && (accesos.contains(acceso)));
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean getMenuAccess(String funcionalidad) {
        if (isLogin()) {
            List<Acceso> accesos = getLoginClient().getPermisionList(funcionalidad);
            return ((accesos != null) && (!accesos.isEmpty()));
        }
        return false;
    }

    private boolean isLogin() {
        Usuario u = getLoginClient().getOperador(Usuario.class);
        return u != null;
    }
}
