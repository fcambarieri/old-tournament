/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo.competencia;

import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class LlaveForma extends Llave implements Serializable {

    private CategoriaForma categoriaForma;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LlaveForma)) {
            return false;
        }
        LlaveForma other = (LlaveForma) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }


    @Override
    @Transient
    public String getDisplay() {
        return getCategoria().getDisplay();
    }

    @ManyToOne
    @JoinColumn(name="categoriaforma")
    public CategoriaForma getCategoria() {
        return categoriaForma;
    }

    public void setCategoria(CategoriaForma forma) {
        this.categoriaForma = forma;
    }
}
