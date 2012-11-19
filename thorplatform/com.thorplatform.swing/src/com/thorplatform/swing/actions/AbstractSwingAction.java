/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public abstract class AbstractSwingAction extends AbstractAction implements SwingActionListener {

   protected boolean isLogin;
    
   public AbstractSwingAction(String name) {
       this(name, null);
    }

    public AbstractSwingAction(String name, Icon icon) {
        this(name, icon, null, null);
    }

    public AbstractSwingAction(String name, String codigoFuncionalidad, Object acceso) {
        this(name, null, codigoFuncionalidad, acceso);
    }
    
    public AbstractSwingAction(String name, Icon icon, String codigoFuncionalidad, Object acceso) {
        super(name, icon);
        setEnabled(false);
        registerSecureAbstractSwingAction(this);
        
        if (codigoFuncionalidad != null && acceso != null)
            setAcceso(codigoFuncionalidad, acceso);
    }

    /**
     * Habilita o Inhabilita una opc�on de men�, si existe al m�nos un permiso para el operador logueado
     *
     * @param String Codigo de Funcionalidad a ver si existe alg�n permiso
     */
    public void setAccesoMenu(String codigoFuncionalidad) {
        setAccesoMenu(new String[]{codigoFuncionalidad});
    }

    /**
     * Habilita o Inhabilita una opc�on de men�, si existe al m�nos un permiso para el operador logueado
     *
     * @param List<String> lista de c�digos de funcionalidad
     */
    public void setAccesoMenu(String[] listaCodigoFuncionlidad) {
        boolean enabled = false;
        int index = 0;
        while (!enabled && index < listaCodigoFuncionlidad.length) {
            String codigo = listaCodigoFuncionlidad[index];
            enabled = getMenuAccess(codigo);
            index++;
        }
        this.setEnabled(enabled);
    }

    /**
     * Habilita o Inhabilita una opci�n de Alta, Baja, Consulta o Modificaci�n si existe para el operador
     * logueado permiso de Acceso para la funcionalidad pasada como parametro.
     *
     * @param String Cod        at com.rafting.client.utils.AbstractSwingAction.setAccesoMenu(AbstractSwingAction.java:52)igo de Funcionalidad
     * @param Acceso Valor de acceso
     */
    public void setAcceso(String codigoFuncionalidad, Object acceso) {
        setAcceso(new String[] {codigoFuncionalidad}, acceso);
    }

    /**
     * Habilita o Inhabilita una opci�n de Alta, Baja, Consulta o Modificaci�n si existe para el operador
     * logueado permiso de Acceso para las funcionalidades pasadas como parametro.
     *
     * @param String[] Array de codigos de Funcionalidades
     * @param Acceso Valor de acceso
     */
    public void setAcceso(String[] listaCodigoFuncionlidad, Object acceso) {
       boolean enabled = false;
        int index = 0;
        while (!enabled && index < listaCodigoFuncionlidad.length) {
            String codigo = listaCodigoFuncionlidad[index];
            enabled = getAccess(codigo, acceso);
            index++;
        }
        this.setEnabled(enabled);
    }

    private ValidatePermision getValidatePermisson() {
        return Lookup.getDefault().lookup(ValidatePermision.class);
    }

    public void setIslogin(boolean isLogin) {
        setEnabled(isLogin);
    }

    private  void registerSecureAbstractSwingAction(SwingActionListener action) {
        NotifierSwingActionListener notifier = Lookup.getDefault().lookup(NotifierSwingActionListener.class);
        notifier.addSwingActionListener(action);
    }
    
    private boolean getMenuAccess(String funcionalidad) {
        if (getValidatePermisson() != null)
            return getValidatePermisson().getMenuAccess(funcionalidad);
        return false;
    }
    
    private boolean getAccess(String funcionality, Object access) {
        if (getValidatePermisson() != null)
            return getValidatePermisson().getAccess(funcionality, access);
        return false;
    }
}
