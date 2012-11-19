/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class CompetidorCategoriaForma extends CompetidorCategoria implements Serializable {

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
        if (!(object instanceof CompetidorCategoriaForma)) {
            return false;
        }
        CompetidorCategoriaForma other = (CompetidorCategoriaForma) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

   

    @ManyToOne
    public CategoriaForma getCategoriaForma() {
        return categoriaForma;
    }

    public void setCategoriaForma(CategoriaForma categoriaForma) {
        this.categoriaForma = categoriaForma;
    }

    @Override
    @Transient
    public String getDisplay() {
        return getEstadoCompetidor().getDisplayWithOutDate() + " " +
                categoriaForma.getDisplay();
    }

    @Override
    @Transient
    public String getHTMLDisplay() {
            String html = "<font color=" + (isActivo() ?  "'00FF00'" : "'FF0000'") + ">"
                    + getEstadoCompetidor().getDisplayWithOutDate() + "</font> ";
            html += categoriaForma.getDisplay();
            return html;
    }
}
