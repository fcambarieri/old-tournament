/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.actions;

/**
 *
 * @author fernando
 */
public interface ValidatePermision {

    /*
     * Este metodo devuelve verdadero o falso si el usuario tiene o no permisos
     * @param String funacionalidad a la cual quiere acceder
     * @param Object es el tipo de acceso que sea realizar
     * @return true si para esa funcionalidad y acceso tiene permiso y falso de
     *  lo contrario
     */
    boolean getAccess(String funcionality, Object acceso);
    
    /*
     *  Devuelve verdadero si el usuario tiene permiso para la funcionalidad 
     *  que esta tratando de realizar.
     */
    boolean getMenuAccess(String funcionality);
}
