/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author fernando
 */
@Entity
@Indexed
public class Institucion extends Persona implements Serializable {
    
    private String nombre;
    private List<Alumno> competidores;
    private List<Profesor> profesores;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institucion)) {
            return false;
        }
        Institucion other = (Institucion) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    @OneToMany(mappedBy = "institucion", cascade=CascadeType.ALL)
    public List<Alumno> getCompetidores() {
        return competidores;
    }

    public void setCompetidores(List<Alumno> competidores) {
        this.competidores = competidores;
    }

    @OneToMany(mappedBy = "institucion", cascade=CascadeType.ALL)
    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }
    
    @Transient
    @Override
    public List<String> getKeywords() {
        List<String> keys = super.getKeywords();
        keys.add(nombre);
        
        if (getCompetidores() != null && !getCompetidores().isEmpty()) {
            for(Alumno c : getCompetidores())
                keys.addAll(c.getPersonaFisica().getKeywords());
        }
        
        if (getProfesores() != null && !getProfesores().isEmpty()) {
            for(Profesor p : getProfesores())
                keys.addAll(p.getPersonaFisica().getKeywords());
        }
        
        if (getContactos() != null && !getContactos().isEmpty()) {
            for(ContactoPersona p : getContactos())
                keys.add(p.getValor());
        }
        return keys;
    }

    @Override
    @Transient
    public String getDisplayName() {
        return getNombre();
    }

}
