/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

/**
 *
 * @author fernando
 */
public enum TipoEstadoCompetidor {
    
    ACTIVO("Activo"),
    DESCALIFICADO("Descalificado"),
    CAMPEON("Campeon"),
    SUB_CAMPEON("Subcampeon"),
    PERDEDOR("Perdedor"),
    LESIONADO("Lesionado");
    
    private String name;
    
    private TipoEstadoCompetidor(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
}
