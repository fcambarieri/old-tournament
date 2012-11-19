/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo.competencia;

/**
 *
 * @author fernando
 */
public enum TipoEstadoCompetencia {

    PENDIENTE("Pendiente"),
    EN_CONDICIONES("En condiciones"),
    INICIADA("Inciada"),
    CANCELADA("Cancelada"),
    FINALIZADA("Finalizada");
    
    
    private String name;
    
    private TipoEstadoCompetencia(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
