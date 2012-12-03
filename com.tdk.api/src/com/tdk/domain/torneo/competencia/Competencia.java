/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo.competencia;

import com.tdk.domain.torneo.Competidor;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author fernando
 */
@Entity
public class Competencia implements Serializable {
    
    private Long id;
    private Long numero;
    private Competencia competenciaPadre;
    private Competencia compentenciaLeft;
    private Competencia compentenciaRight;
    private Competidor competidorAzul;
    private Competidor competidorRojo;
    private Competidor competidorGanador;
    private EstadoCompetencia estadoActual;
    private List<EstadoCompetencia> estados;

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
        if (!(object instanceof Competencia)) {
            return false;
        }
        Competencia other = (Competencia) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numero != null ? numero.toString() : "S/N";
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

   
    @OneToOne()
    @JoinColumn(name="competencialeft")
    public Competencia getCompentenciaLeft() {
        return compentenciaLeft;
    }

    public void setCompentenciaLeft(Competencia compentenciaLeft) {
        this.compentenciaLeft = compentenciaLeft;
        if (this.compentenciaLeft != null) {
            this.compentenciaLeft.setCompetenciaPadre(this);
        }
    }

    @OneToOne
    @JoinColumn(name="competenciaright")
    public Competencia getCompentenciaRight() {
        return compentenciaRight;
    }

    public void setCompentenciaRight(Competencia compentenciaRight) {
        this.compentenciaRight = compentenciaRight;
        if (this.compentenciaRight != null) {
            this.compentenciaRight.setCompetenciaPadre(this);
        }
    }

    @OneToOne
    @JoinColumn(name="competenciapadre")
    public Competencia getCompetenciaPadre() {
        return competenciaPadre;
    }

    public void setCompetenciaPadre(Competencia competenciaPadre) {
        this.competenciaPadre = competenciaPadre;
    }

    @OneToOne
    @JoinColumn(name="competidorazul")
    public Competidor getCompetidorAzul() {
        return competidorAzul;
    }

    public void setCompetidorAzul(Competidor competidorAzul) {
        this.competidorAzul = competidorAzul;
    }

    @OneToOne
    @JoinColumn(name="competidorrojo")
    public Competidor getCompetidorRojo() {
        return competidorRojo;
    }

    public void setCompetidorRojo(Competidor competidorRojo) {
        this.competidorRojo = competidorRojo;
    }

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="estadoactual")
    public EstadoCompetencia getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoCompetencia estadoActual) {
        this.estadoActual = estadoActual;
    }

    @OneToMany(mappedBy = "competencia", cascade=CascadeType.ALL/*, fetch=FetchType.EAGER*/)
    public List<EstadoCompetencia> getEstados() {
        return estados;
    }

    public void setEstados(List<EstadoCompetencia> estados) {
        this.estados = estados;
    }

    @OneToOne
    @JoinColumn(name="competidorganador")
    public Competidor getCompetidorGanador() {
        return competidorGanador;
    }

    public void setCompetidorGanador(Competidor competidorGanador) {
        this.competidorGanador = competidorGanador;
    }

}
