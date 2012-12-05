/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author fernando
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Indexed
public abstract class Persona implements Serializable {
    
    
    private Long id;
    private List<ContactoPersona> contactos;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
    
    @Transient
    public abstract String getDisplayName();

    @OneToMany(mappedBy="persona", cascade=CascadeType.ALL)
    public List<ContactoPersona> getContactos() {
        return contactos;
    }

    public void setContactos(List<ContactoPersona> contactos) {
        this.contactos = contactos;
    }

    @Transient
    public List<String> getKeywords() {
        List<String> keys = new ArrayList<String>();
        keys.add(getDisplayName());
        return keys;
    }
}
