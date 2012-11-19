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
public class CompetidorCategoriaLucha extends CompetidorCategoria implements Serializable {
    
    private Peso peso;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompetidorCategoriaLucha)) {
            return false;
        }
        CompetidorCategoriaLucha other = (CompetidorCategoriaLucha) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " - [ " + peso.toString() + "] Kg" ;
    }
   
    @ManyToOne
    public Peso getPeso() {
        return peso;
    }

    public void setPeso(Peso peso) {
        this.peso = peso;
    }

    @Override
    @Transient
    public String getDisplay() {
        return getEstadoCompetidor().getDisplayWithOutDate() + " - [ "
                + peso.toString() + "] Kg" ;
    }

    @Override
    @Transient
    public String getHTMLDisplay() {
            String html = "<font color=" + (isActivo() ?  "'00FF00'" : "'FF0000'") + ">"
                    + getEstadoCompetidor().getDisplayWithOutDate() + "</font> ";
            html += " - [ " + peso.toString() + "] Kg" ;
            return html;
    }

}
