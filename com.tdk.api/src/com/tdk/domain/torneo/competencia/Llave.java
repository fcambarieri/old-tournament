/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.domain.torneo.competencia;

import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Torneo;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Llave implements Serializable {

    private Long id;
    private Competencia root;
    private Cinturon cinturon;
    private Torneo torneo;

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
        if (!(object instanceof Llave)) {
            return false;
        }
        Llave other = (Llave) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getCategoria().getDisplay() + " [" + getCinturon().getDescripcion() + "]";
    }

    @Transient
    public abstract String getDisplay();

    @Transient
    public abstract Categoria getCategoria();

    @OneToOne
    @JoinColumn(name="root")
    public Competencia getCompetencia() {
        return root;
    }

    public void setCompetencia(Competencia competencia) {
        this.root = competencia;
    }

    @OneToOne
    public Cinturon getCinturon() {
        return cinturon;
    }

    public void setCinturon(Cinturon cinturon) {
        this.cinturon = cinturon;
    }

    public int size() {
        return size(root);
    }

    public int size(Competencia node) {
        if (node == null) {
            return 0;
        } else {
            return size(node.getCompentenciaLeft()) + 1 + size(node.getCompentenciaRight());
        }
    }

    @ManyToOne
    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
}
