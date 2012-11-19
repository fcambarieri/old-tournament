/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.domain.torneo;

import com.tdk.domain.*;
import com.tdk.utils.FechaHoraUtils;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class EstadoCompetidor implements Estado<TipoEstadoCompetidor>, java.io.Serializable {

    private Long id;
    private TipoEstadoCompetidor tipoEstadoCompetidor;
    private CompetidorCategoria competidorCategoria;
    private Date fechaDesde;

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
        if (!(object instanceof EstadoCompetidor)) {
            return false;
        }
        EstadoCompetidor other = (EstadoCompetidor) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTipoEstadoCompetidor().toString();
    }

    @Override
    @Transient
    public String getDisplayWithDate() {
        return getDisplayWithDate() + " - " + getFechaDesde();
    }

    @Transient
    public String getDisplayWithOutDate() {
        return toString();
    }

    @Enumerated(EnumType.ORDINAL)
    public TipoEstadoCompetidor getTipoEstadoCompetidor() {
        return tipoEstadoCompetidor;
    }

    public void setTipoEstadoCompetidor(TipoEstadoCompetidor tipoEstadoCompetidor) {
        this.tipoEstadoCompetidor = tipoEstadoCompetidor;
    }

    @Override
    @Transient
    public TipoEstadoCompetidor[] getTipoEstados() {
        return tipoEstadoCompetidor.values();
    }

    @ManyToOne
    public CompetidorCategoria getCompetidorCategoria() {
        return competidorCategoria;
    }

    public void setCompetidorCategoria(CompetidorCategoria competidorCategoria) {
        this.competidorCategoria = competidorCategoria;
    }

    @Transient
    public TipoEstadoCompetidor getTipoEstado() {
        return getTipoEstadoCompetidor();
    }

    public void setTipoEstado(TipoEstadoCompetidor tipoEstado) {
        this.setTipoEstadoCompetidor(tipoEstado);
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date date) {
        this.fechaDesde = date;
    
    }

    @Transient
    public String getDisplayDate() {
        if (fechaDesde != null) {
            return FechaHoraUtils.formatearFechaHora(fechaDesde);
        }
        return "Sin info";
    }
}
