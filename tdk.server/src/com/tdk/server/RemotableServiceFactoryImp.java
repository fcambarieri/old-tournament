/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.server;

import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.Usuario;
import com.tdk.services.SecurityServiceRemote;
import com.thorplatform.server.RemotableServiceFactory;
import java.util.List;

/**
 *
 * @author fernando
 */
public class RemotableServiceFactoryImp extends RemotableServiceFactory{

    @Override
    public List<?> recuperarPermisos(String funcionalidad) {
        List<Acceso> accesos = getSecurityService().recuperarPermiso(getUsuario(), funcionalidad);
        return accesos;
       // return new ArrayList<Acceso>(Arrays.asList(Acceso.values())); 
    }
    
    private SecurityServiceRemote getSecurityService() {
        return (SecurityServiceRemote) getJPALocalServiceFactory().getService(SecurityServiceRemote.class);
    }
    
    private Usuario getUsuario() {
        return (Usuario) getUser();
    }

}
