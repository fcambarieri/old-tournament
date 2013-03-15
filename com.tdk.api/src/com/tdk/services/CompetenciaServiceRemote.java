/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.services;

import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.competencia.Competencia;
import com.tdk.domain.torneo.competencia.Llave;
import com.tdk.domain.torneo.competencia.LlaveForma;
import com.tdk.domain.torneo.competencia.LlaveLucha;
import java.util.List;

/**
 *
 * @author fernando
 */
public interface CompetenciaServiceRemote {

    Llave crearLlave(Llave llave);
    
    LlaveForma crearLlaveForma(Cinturon cinturon, CategoriaForma categoria, Torneo torneo);
    
    void modificarLlave(Llave llave);
    
    void eliminarLlave(Long idLlave);
    
    List<Llave> listarLlave(String patron);
    
    List<LlaveLucha> listarLlaveLucha(String patron);
    
    List<LlaveForma> listarLlaveFormas(String patron);
    
    void modificarCompetencia(Competencia competencia);
    
    void finalizarCompentecia(Competencia competencia);

    Competencia recuperarCompetencia(Long idCompetencia, boolean completa);
    
     public Llave crearLlave(Llave llave, List<Competidor> competidores);
     
     public LlaveLucha crearLlaveLucha(Cinturon cinturon, CategoriaLucha categoria, Torneo torneo);
    
}
