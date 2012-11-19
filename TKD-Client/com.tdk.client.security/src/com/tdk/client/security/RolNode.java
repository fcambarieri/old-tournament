/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.security;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.FuncionalidadRol;
import com.tdk.domain.security.Rol;
import com.tdk.services.SecurityServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.InputController;
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
public class RolNode extends SwingNode<Rol> {

    public RolNode(Rol rol) {
        super(new RolNodeChildren(rol), rol);
        setIconBaseWithExtension("com/tdk/client/security/roles-16x16.png");
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new ModificarRolAction(getValue())};
    }

    @Override
    public String getDisplay() {
        return "[Rol] " + getValue().getDescripcion();
    }
}

class RolNodeChildren extends Children.Keys {

    private static final Integer PERMISO = new Integer("0");
    private List<Integer> categorias;
    private Rol rol;

    public RolNodeChildren(Rol rol) {
        this.rol = rol;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        if (rol != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(PERMISO);
            setKeys(categorias);
        }
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        AbstractNode node = null;
        Integer key = (Integer) arg0;

        if (key.equals(PERMISO)) {
            node = new FuncionalidadRolNode(rol);
        }

        return new Node[]{node};

    }
}


class FuncionalidadRolNode extends AbstractNode {

    private Rol rol;
    
    public FuncionalidadRolNode(Rol rol) {
        super(new FuncionalidadRolNodeChildren(rol));
        setIconBaseWithExtension("com/tdk/client/security/permisos-16x16.png");
        this.rol = rol;
    }

    @Override
    public String getDisplayName() {
        return "Funcionalidades por Rol";
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{new AgregarPersmisoRol(rol)};
    }
    
    
}
class FuncionalidadRolNodeChildren extends Children.Keys {

    private Rol rol;

    public FuncionalidadRolNodeChildren(Rol rol) {
        this.rol = rol;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        SecurityServiceRemote service = sf.getService(SecurityServiceRemote.class);
        setKeys(service.listarFuncionesPorRol(rol.getId()));
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new FuncionalidadRolNodeLeaf((FuncionalidadRol) arg0)};
    }
}

class FuncionalidadRolNodeLeaf extends AbstractNode {

    private FuncionalidadRol funcionalidadRol;

    public FuncionalidadRolNodeLeaf(FuncionalidadRol permisoPerfiles) {
        super(Children.LEAF);
        this.funcionalidadRol = permisoPerfiles;
        setIconBaseWithExtension("com/tdk/client/security/acceso-16x16.png");
    }

    @Override
    public String getHtmlDisplayName() {
        String html = funcionalidadRol.getFuncionalidad().getDescripcion() + ": ";
        html += color(funcionalidadRol.getAlta(), "Alta");
        html += color(funcionalidadRol.getBaja(), "Baja");
        html += color(funcionalidadRol.getModificacion(), "Modifiación");
        html += color(funcionalidadRol.getConsulta(), "Consulta");
        return html;
    }

    private String color(boolean value, String str) {
        String newStr = "<font color=" + (value ? "'00FF00'" : "'FF0000'") + ">" + str + "</font> ";
        return newStr;
    }
}

/***** ACTIONS *****/
class ModificarRolAction extends AbstractSwingAction {

    private Rol rol;

    public ModificarRolAction(Rol rol) {
        super("Modificar rol", "Roles", Acceso.MODIFICACION);
        this.rol = rol;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (rol != null) {
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            InputController controller = scf.createController(InputController.class);
            controller.setTitle("Modificar rol " + rol.getDescripcion());
            controller.setLabelDescripcion("Descripcion");
            controller.setMaxLenght(new Integer("64"));
            controller.getTexto().set(rol.getDescripcion());
            boolean error = true;
            while (error && controller.showModal()) {
                rol.setDescripcion(controller.getTexto().get());
                try {
                    getSecurityService().modificarRol(rol);
                    error = false;
                } catch (Throwable ex) {
                    controller.getGuiUtils().warnnig(ex, TDKServerException.class);
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

class AgregarPersmisoRol extends AbstractSwingAction {

    private Rol rol;

    public AgregarPersmisoRol(Rol rol) {
        super("Agregar permisos a rol", "Permisos", Acceso.ALTA);
        this.rol = rol;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (rol != null) {
            SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
            ConfigurarPermisosPorRolController controller = scf.createController(ConfigurarPermisosPorRolController.class);
            controller.setRol(rol);
            controller.permisos.assignData(getSecurityService().listarFuncionesPorRol(rol.getId()));
            boolean error = true;
            while (error && controller.showModal()) {
                try {
                    getSecurityService().crearFuncionalidadesRol(controller.permisos.getList());
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