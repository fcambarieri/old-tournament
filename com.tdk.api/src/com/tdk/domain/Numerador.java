/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author fernando
 */
@Entity
public class Numerador implements Serializable {

    private Long id;
    private String keyNumerador;
    private Long numeroActual;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Numerador)) {
            return false;
        }
        Numerador other = (Numerador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tdk.domain.Numerador[id=" + id + "]";
    }
    
    public String getKeyNumerador() {
        return keyNumerador;
    }

    public void setKeyNumerador(String keyNumerador) {
        this.keyNumerador = keyNumerador;
    }

    public Long getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(Long numeroActual) {
        this.numeroActual = numeroActual;
    }
    
    public Long incrementarNumeroActual() {
        if (numeroActual == null)
            throw new RuntimeException("Error: Numerador no inicializado.");
        else
            numeroActual++;
        
        return numeroActual;
    }

}
