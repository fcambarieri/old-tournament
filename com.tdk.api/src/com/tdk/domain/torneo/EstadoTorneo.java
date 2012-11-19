/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.domain.torneo;

import com.tdk.domain.Estado;
import com.tdk.utils.FechaHoraUtils;
import java.io.Serializable;
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
public class EstadoTorneo implements Estado<TipoEstadoTorneo>, Serializable {

    private Long id;
    private TipoEstadoTorneo tipoEstadoTorneo;
    private Torneo torneo;
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
        if (!(object instanceof EstadoTorneo)) {
            return false;
        }
        EstadoTorneo other = (EstadoTorneo) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDisplayWithOutDate();
    }

    @Override
    @Transient
    public String getDisplayWithDate() {
        return tipoEstadoTorneo.toString() + " - Desde: " + FechaHoraUtils.formatearFecha(fechaDesde); 
    }

    @Override
    @Transient
    public String getDisplayWithOutDate() {
        return tipoEstadoTorneo.toString();
    }

    @Enumerated(EnumType.ORDINAL)
    public TipoEstadoTorneo getTipoEstadoTorneo() {
        return tipoEstadoTorneo;
    }

    public void setTipoEstadoTorneo(TipoEstadoTorneo tipoEstadoTorneo) {
        this.tipoEstadoTorneo = tipoEstadoTorneo;
    }

    @ManyToOne
    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    @Override
    @Transient
    public TipoEstadoTorneo[] getTipoEstados() {
        return TipoEstadoTorneo.values();
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    @Transient
    public TipoEstadoTorneo getTipoEstado() {
        return getTipoEstadoTorneo();
    }

    public void setTipoEstado(TipoEstadoTorneo tipoEstado) {
        setTipoEstadoTorneo(tipoEstado);
    }

    @Transient
    public String getDisplayDate() {
        if (fechaDesde != null) {
            return FechaHoraUtils.formatearFechaHora(fechaDesde);
        }
        return "Sin info";
    }
}
