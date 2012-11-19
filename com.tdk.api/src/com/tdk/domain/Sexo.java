/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain;

/**
 *
 * @author fernando
 */
public enum Sexo {
    
    FEMENINO("Femenino"),
    MASCULINO("Masculino");
    
    private String name;
    
    private Sexo(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
