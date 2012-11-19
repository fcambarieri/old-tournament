/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class CategoriaForma extends Categoria implements Serializable {
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriaForma)) {
            return false;
        }
        CategoriaForma other = (CategoriaForma) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    
    @Override
    @Transient
    public String getDisplay() {
        return getDescripcion() + " - desde " + getEdadInferior() + " hasta " +
                getEdadSuperior() + " años";
    }

    

}
