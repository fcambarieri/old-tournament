/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

/**
 *
 * @author fernando
 */
public enum TipoEstadoTorneo {
    
    PENDIENTE("Pendiente"),
    ACTIVO("Activo"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado");
    
    private String name;
    
    private TipoEstadoTorneo(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
