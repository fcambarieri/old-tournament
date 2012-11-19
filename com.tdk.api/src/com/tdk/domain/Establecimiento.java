/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain;

import java.io.Serializable;

/**
 *
 * @author fernando
 */
public class Establecimiento implements Serializable{

    private String lugar;

    public Establecimiento(String lugar) {
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public String toString() {
        return lugar;
    }
    
    
}
