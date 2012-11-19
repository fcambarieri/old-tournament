/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo.competencia;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class EstadoCompetencia implements Estado<TipoEstadoCompetencia>, Serializable {
    
    private Long id;
    private Date fechaDesde;
    private TipoEstadoCompetencia tipoEstadoCompetencia;
    private Competencia competencia;

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
        if (!(object instanceof EstadoCompetencia)) {
            return false;
        }
        EstadoCompetencia other = (EstadoCompetencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTipoEstadoCompetencia().toString();
    }

    @Transient
    public String getDisplayDate() {
         if (fechaDesde != null) {
            return FechaHoraUtils.formatearFechaHora(fechaDesde);
        }
        return "Sin info";
    }

    @Transient
    public String getDisplayWithDate() {
        return toString() + " " + getDisplayDate();
    }

    @Transient
    public String getDisplayWithOutDate() {
        return toString();
    }

    @Transient
    public TipoEstadoCompetencia getTipoEstado() {
        return getTipoEstadoCompetencia();
    }

    public void setTipoEstado(TipoEstadoCompetencia tipoEstado) {
        this.setTipoEstadoCompetencia(tipoEstado);
    }

    @Transient
    public TipoEstadoCompetencia[] getTipoEstados() {
        return TipoEstadoCompetencia.values();
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    @ManyToOne
    public Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    @Enumerated(EnumType.ORDINAL)
    public TipoEstadoCompetencia getTipoEstadoCompetencia() {
        return tipoEstadoCompetencia;
    }

    public void setTipoEstadoCompetencia(TipoEstadoCompetencia tipoEstadoCompetencia) {
        this.tipoEstadoCompetencia = tipoEstadoCompetencia;
    }

}
