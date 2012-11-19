/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author fernando
 */
@Entity
public class Torneo implements Serializable {

    private Long id;
    private String nombre;
    private EstadoTorneo estadoTorneo;
    private Date fechaDesde;
    private Date fechaHasta;
    private List<EstadoTorneo> historialEstados;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
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
        if (!(object instanceof Torneo)) {
            return false;
        }
        Torneo other = (Torneo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre() + " - Tipo de estado: " + getEstadoTorneo().getDisplayWithOutDate();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public EstadoTorneo getEstadoTorneo() {
        return estadoTorneo;
    }

    public void setEstadoTorneo(EstadoTorneo estadoTorneo) {
        this.estadoTorneo = estadoTorneo;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    
    @OneToMany(mappedBy = "torneo", cascade=CascadeType.ALL)
    public List<EstadoTorneo> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<EstadoTorneo> historialEstados) {
        this.historialEstados = historialEstados;
    }

}
