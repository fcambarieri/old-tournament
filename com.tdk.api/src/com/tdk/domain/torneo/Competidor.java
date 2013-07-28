/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.domain.torneo;

import com.tdk.domain.*;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author fernando
 */
@Entity
public class Competidor implements Serializable {

    private Long id;
    private Alumno alumno;
    private CompetidorCategoriaLucha competidorCategoriaLucha;
    private CompetidorCategoriaForma competidorCategoriaForma;
    private Cinturon cinturon;
    private Torneo torneo;

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
        if (!(object instanceof Competidor)) {
            return false;
        }
        Competidor other = (Competidor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getAlumno().getPersonaFisica().getDisplayName();
    }

    @ManyToOne
    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public CompetidorCategoriaLucha getCompetidorCategoriaLucha() {
        return competidorCategoriaLucha;
    }

    public void setCompetidorCategoriaLucha(CompetidorCategoriaLucha competidorCategoriaLucha) {
        this.competidorCategoriaLucha = competidorCategoriaLucha;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public CompetidorCategoriaForma getCompetidorCategoriaForma() {
        return competidorCategoriaForma;
    }

    public void setCompetidorCategoriaForma(CompetidorCategoriaForma competidorCategoriaForma) {
        this.competidorCategoriaForma = competidorCategoriaForma;
    }

    @ManyToOne
    public Cinturon getCinturon() {
        return cinturon;
    }

    public void setCinturon(Cinturon cinturon) {
        this.cinturon = cinturon;
    }

    @ManyToOne
    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
    
    @Transient
    public String getDisplayCompetidor() {
        StringBuilder sb = new StringBuilder();
        sb.append(alumno.getPersonaFisica().getNombre().substring(0, 1).toUpperCase());
        sb.append(". ");
        sb.append(alumno.getPersonaFisica().getApellido());
        sb.append("(");
        sb.append(alumno.getInstitucion().getDisplayName());
        sb.append(")");
        return sb.toString();
    }
    
    @Transient
    public String getDisplayCompetidorLucha() {
        if (competidorCategoriaLucha != null) {
            return alumno.getPersonaFisica().getApellido() + 
                    "("+alumno.getInstitucion().getDisplayName() + ")" 
                    + competidorCategoriaLucha.getDisplay();
        } else {
            return "No inscripto";
        }
        
    }
    @Transient
    public String getDisplayCompetidorForma() {
          if (competidorCategoriaForma != null) {
            return alumno.getPersonaFisica().getApellido() + 
                    "("+alumno.getInstitucion().getDisplayName() + ")" 
                    + competidorCategoriaForma.getDisplay();
        } else {
            return "No inscripto";
        }
      
    }
}
