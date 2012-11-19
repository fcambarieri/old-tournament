/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias.node;

import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Torneo;

/**
 *
 * @author sabon
 */
public class CategoriaTorneoDTO {
 
    private Torneo torneo;
    private Cinturon cinturon;
    private Categoria categoria;

    public CategoriaTorneoDTO(Torneo torneo, Cinturon cinturon, Categoria categoria) {
        this.torneo = torneo;
        this.cinturon = cinturon;
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Cinturon getCinturon() {
        return cinturon;
    }

    public Torneo getTorneo() {
        return torneo;
    }
    
    public String getDisplay() {
        return cinturon.getDescripcion() + " " + categoria.getDisplay();
    }
}
