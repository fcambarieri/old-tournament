/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import com.tdk.domain.Sexo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class CategoriaLucha extends Categoria implements Serializable {
 
    private Sexo sexo;
    private List<Peso> pesos;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriaLucha)) {
            return false;
        }
        CategoriaLucha other = (CategoriaLucha) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }

    @Override
    @Transient
    public String getDisplay() {
        return getDescripcion() + " desde " + getEdadInferior() + " hasta "+ getEdadSuperior()
                + " [" +getSexo().toString() + "]";
    }

    @Enumerated(EnumType.ORDINAL)
    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    
    @OneToMany(mappedBy = "categoriaLucha", cascade=CascadeType.ALL/*, fetch=FetchType.EAGER*/)
    public List<Peso> getPesos() {
        return pesos;
    }

    public void setPesos(List<Peso> pesos) {
        this.pesos = pesos;
    }

}
