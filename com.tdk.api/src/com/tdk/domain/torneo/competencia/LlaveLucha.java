/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo.competencia;

import com.tdk.domain.torneo.CategoriaLucha;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class LlaveLucha extends Llave implements Serializable {

    private CategoriaLucha categoria;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LlaveLucha)) {
            return false;
        }
        LlaveLucha other = (LlaveLucha) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }


    @Override
    @Transient
    public String getDisplay() {
        return categoria.getDisplay();
    }

    @ManyToOne
    @JoinColumn(name="categorialucha")
    public CategoriaLucha getCategoria() {
        return categoria;
    }
    
    public void setCategoria(CategoriaLucha categoriaLucha) {
        this.categoria = categoriaLucha;
    }
    
}
