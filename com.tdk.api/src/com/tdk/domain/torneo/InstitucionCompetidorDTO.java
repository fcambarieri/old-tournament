/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain.torneo;

import com.tdk.domain.Institucion;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author fernando
 */
public class InstitucionCompetidorDTO implements Serializable {
    
    private Institucion institucion;
    
    private List<Competidor> competidores;
    
    public InstitucionCompetidorDTO(){
    }
    
    public InstitucionCompetidorDTO(Institucion institucion, List<Competidor> competidores){
        setInstitucion(institucion);
        setCompetidores(competidores);
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public List<Competidor> getCompetidores() {
        return competidores;
    }

    public void setCompetidores(List<Competidor> competidores) {
        this.competidores = competidores;
    }
            

}
