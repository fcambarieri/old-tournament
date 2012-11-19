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
import javax.persistence.ManyToOne;

/**
 *
 * @author fernando
 */
@Entity
public class Peso implements Serializable {
    
    private Long id;
    private CategoriaLucha categoriaLucha;
    private Integer pesoInferior;
    private Integer pesoSuperior;
    
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
        if (!(object instanceof Peso)) {
            return false;
        }
        Peso other = (Peso) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getPesoInferior() + " - " + getPesoSuperior() ;
    }

    @ManyToOne
    public CategoriaLucha getCategoriaLucha() {
        return categoriaLucha;
    }

    public void setCategoriaLucha(CategoriaLucha categoriaLucha) {
        this.categoriaLucha = categoriaLucha;
    }

    public Integer getPesoInferior() {
        return pesoInferior;
    }

    public void setPesoInferior(Integer pesoInferior) {
        this.pesoInferior = pesoInferior;
    }

    public Integer getPesoSuperior() {
        return pesoSuperior;
    }

    public void setPesoSuperior(Integer pesoSuperior) {
        this.pesoSuperior = pesoSuperior;
    }

}
