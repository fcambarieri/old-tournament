/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Categoria implements Serializable {
    
    private Long id;
    private Integer edadInferior;
    private Integer edadSuperior;
    private String descripcion;
    
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDisplay();
    }
        
    @Transient
    public abstract String getDisplay();

    public Integer getEdadInferior() {
        return edadInferior;
    }

    public void setEdadInferior(Integer edadInferior) {
        this.edadInferior = edadInferior;
    }

    public Integer getEdadSuperior() {
        return edadSuperior;
    }

    public void setEdadSuperior(Integer edadSuperior) {
        this.edadSuperior = edadSuperior;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
