/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import com.tdk.utils.FechaHoraUtils;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class CompetidorCategoria implements Serializable {

    private Long id;
    private EstadoCompetidor estadoCompetidor;
    private List<EstadoCompetidor> estados;

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
        if (!(object instanceof CompetidorCategoria)) {
            return false;
        }
        CompetidorCategoria other = (CompetidorCategoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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

    @Transient
    public abstract String getHTMLDisplay();

    @OneToOne(cascade=CascadeType.ALL)
    public EstadoCompetidor getEstadoCompetidor() {
        return estadoCompetidor;
    }

    public void setEstadoCompetidor(EstadoCompetidor estadoCompetidor) {
        this.estadoCompetidor = estadoCompetidor;
    }

    @OneToMany(mappedBy = "competidorCategoria", /*fetch=FetchType.EAGER,*/ cascade=CascadeType.ALL)
    public List<EstadoCompetidor> getEstados() {
        return estados;
    }

    public void setEstados(List<EstadoCompetidor> estados) {
        this.estados = estados;
    }

    @Transient
    protected boolean isActivo() {
        boolean result = false;
        switch (getEstadoCompetidor().getTipoEstado()) {
            case ACTIVO:
                result = true;
                break;
            case CAMPEON:
                result = true;
                break;
            default:
                result = false;

        }
        return result;
    }

}
