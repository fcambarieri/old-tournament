/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author fernando
 */
@Entity
@Indexed
public class PersonaFisica extends Persona implements java.io.Serializable {
    
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String nombre;
    @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String apellido;
    @DateBridge(resolution = Resolution.DAY)
    private Date fechaNacimiento;
    private Sexo sexo;
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonaFisica)) {
            return false;
        }
        PersonaFisica other = (PersonaFisica) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    @Transient
    public String getDisplayName() {
        return apellido + " " + nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Enumerated(EnumType.ORDINAL)
    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    @Override
    @Transient
    public List<String> getKeywords() {
        List<String> keys = super.getKeywords();
        keys.add(nombre);
        keys.add(apellido);
        return keys;
    }

    
}
