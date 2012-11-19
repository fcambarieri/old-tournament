/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.security;

/**
 *
 * @author fernando
 */
public enum Acceso {
    
    ALTA("Alta"),
    BAJA("Baja"),
    MODIFICACION("Modificacion"),
    CONSULTA("Consulta");
    
    private String name;
    
    private Acceso(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    
}
