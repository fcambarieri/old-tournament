/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.security;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.FuncionalidadUsuario;
import com.tdk.domain.security.Usuario;
import com.tdk.services.SecurityServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.actions.AbstractSwingAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class UsuarioNode extends SwingNode<Usuario> {

    private Usuario usuario;

    public UsuarioNode(Usuario usuario) {
        super(new UsuarioNodeChildren(usuario), usuario);
        this.usuario = usuario;
        setIconBaseWithExtension("com/tdk/client/security/usuarios-16x16.png");
    }

    @Override
    public String toString() {
        return usuario.getUserName();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new ModificarUsuarioAction(usuario)};
    }

    @Override
    public String getDisplay() {
        return usuario.getUserName();
    }
}

class UsuarioNodeChildren extends Children.Keys {

    private static final Integer ROL = new Integer("0");
    private static final Integer FUNCIONALIDADES = new Integer("1");
    private List<Integer> categorias;
    private Usuario usuario;

    public UsuarioNodeChildren(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        AbstractNode node = null;
        Integer key = (Integer) arg0;
        if (key.equals(ROL)) {
            node = new RolNode(usuario.getRol());
        } else if (key.equals(FUNCIONALIDADES)) {
            node = new PermisoUsuarioNode(usuario);
        }
        return new Node[]{node};
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        if (usuario != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(ROL);
            categorias.add(FUNCIONALIDADES);
            setKeys(categorias);
        }
    }
}

class PermisoUsuarioNode extends AbstractNode {

    private Usuario usuario;

    public PermisoUsuarioNode(Usuario usuario) {
        super(new PermisoUsuarioNodeChildren(usuario));
        setIconBaseWithExtension("com/tdk/client/security/permisos-16x16.png");
        this.usuario = usuario;
    }

    @Override
    public String getDisplayName() {
        return "Funcionalidades por usuario";
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarPermisosPorUsuarioAction(usuario)};
    }
}

class PermisoUsuarioNodeChildren extends Children.Keys {

    private Usuario usuario;

    public PermisoUsuarioNodeChildren(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        SecurityServiceRemote service = sf.getService(SecurityServiceRemote.class);
        setKeys(service.listarFuncionalidadesPorUsuario(usuario.getId()));
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new PermisoUsuarioNodeLeaf((FuncionalidadUsuario) arg0)};
    }
}

class PermisoUsuarioNodeLeaf extends AbstractNode {

    private FuncionalidadUsuario permisoUsuarios;

    public PermisoUsuarioNodeLeaf(FuncionalidadUsuario permisoUsuarios) {
        super(Children.LEAF);
        this.permisoUsuarios = permisoUsuarios;
        setIconBaseWithExtension("com/tdk/client/security/acceso-16x16.png");
    }

    @Override
    public String getHtmlDisplayName() {
        String html = permisoUsuarios.getFuncionalidad().getDescripcion() + ": ";
        html += color(permisoUsuarios.getAlta(), "Alta");
        html += color(permisoUsuarios.getBaja(), "Baja");
        html += color(permisoUsuarios.getModificacion(), "Modifiación");
        html += color(permisoUsuarios.getConsulta(), "Consulta");
        return html;
    }

    private String color(boolean value, String str) {
        String newStr = "<font color=" + (value ? "'00FF00'" : "'FF0000'") + ">" + str + "</font> ";
        return newStr;
    }
}

/******************************************************************************/
/******************************** ACTIONS *************************************/
/******************************************************************************/
class AgregarPermisosPorUsuarioAction extends AbstractSwingAction {

    private Usuario usuario;

    public AgregarPermisosPorUsuarioAction(Usuario usuario) {
        super("Agregar permisos a usuario", "Permisos", Acceso.ALTA);
        this.usuario = usuario;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (usuario != null) {
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            ConfigurarPermisosPorUsuarioController controller = scf.createController(ConfigurarPermisosPorUsuarioController.class);
            controller.setUsuario(usuario);
            controller.permisos.assignData(getSecurityService().listarFuncionalidadesPorUsuario(usuario.getId()));
            controller.setTitle("Permisos de " + usuario.getUserName());
            boolean error = true;
            while (error && controller.showModal()) {
                try {
                    getSecurityService().crearRolUsuario(controller.permisos.getList());
                    error = false;
                } catch (Throwable ex) {
                    controller.getGuiUtils().notificationError(ex, TDKServerException.class);
                }

            }
        }
    }

    public void setIsLogin(boolean isLogin) {
        setEnabled(isLogin);
    }

    public SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }
}

class ModificarUsuarioAction extends AbstractSwingAction {

    private Usuario usuario;

    public ModificarUsuarioAction(Usuario usuario) {
        super("Modificar usuario...", "Usuarios", Acceso.MODIFICACION);
        this.usuario = usuario;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (usuario != null) {
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            UsuarioController controller = scf.createController(UsuarioController.class);
            controller.setTitle("Modificar usuario " + usuario.getUserName());
            controller.initControllerForUpdate(usuario);
            boolean error = true;
            while (error && controller.showModal()) {
                usuario = controller.modificarUsuario(usuario);
                try {
                    getSecurityService().crearUsuario(usuario);
                    error = false;
                } catch (Throwable ex) {
                    controller.getGuiUtils().warnnig(ex, TDKServerException.class);
                }
            }
        }
    }

    private SecurityServiceRemote getSecurityService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(SecurityServiceRemote.class);
    }

    public void setIsLogin(boolean isLogin) {
        setIsLogin(isLogin);
    }
}