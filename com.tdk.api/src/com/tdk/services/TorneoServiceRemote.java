/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.services;

import com.tdk.domain.Sexo;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.InstitucionCompetidorDTO;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fernando
 */
public interface TorneoServiceRemote {

    Torneo crearTorneo(Torneo torneo);
    
    void modificarTorneo(Torneo torneo);
    
    List<Torneo> listarTorneos(String patron);

    List<TorneoInstitucion> listarTorneosPorInstitucion(Long idInstitucion);
    
    Torneo recuperarTorneo(Long idTorneo, boolean completo);
    
    Torneo recuperarTorneoVigente(Date fechaDesde);
    
    Cinturon crearCinturon(Cinturon cinturon);
    
    void modificarCinturon(Cinturon cinturon);
    
    Cinturon recuperarCinturon(Long idCinturon);
    
    Cinturon recuperarCinturonPorDescripcion(String descripcion);
    
    void eliminarCinturon(Long idCinturon);
    
    List<Cinturon> listarCinturones(String patron);
    
    CategoriaForma crearCategoriaForma(CategoriaForma categoriaForma);
    
    void modificarCategoriaForma(CategoriaForma categoriaForma);
    
    List<CategoriaForma> listarCategoriasForma(String descripcion);

    List<CategoriaForma> listarCategoriasFormaPorEdad(Integer edad);
    
    void eliminarCategoria(Long idCategoria);
    
    /***************************** Luchas **********************************/
    
    CategoriaLucha crearCategoriaLuchas(CategoriaLucha categoriaLucha);
    
    void modificarCategoriaLucha(CategoriaLucha categoriaLucha);
    
    Competidor recuperarCompetidor(Long idCompetidor, boolean completo);
    
    List<CategoriaLucha> listarCategoriasLucha(String descripcion);

    List<CategoriaLucha> listarCategoriasLuchasPorSexoYEdad(Sexo sexo, Integer edad);

    Competidor crearCompetidor(Competidor competidor);
    
    void modificarCompetidor(Competidor competidor);
    
    void eliminarCompetidor(Long idCompetidor);
    
    List<Competidor> listarCompetidores(String patron);
    
    List<Competidor> listarCompetidoresLucha(Long idCinturon, Long  idCategoria, Long idTorneo);
    
    List<Competidor> listarCompetidoresForma(Long idCinturon, Long  idCategoria, Long idTorneo);
    
    List<Competidor> listarCompetidoresPorTorneo(Long idTorneo, String patron);

    /** Este metodo lista los competidores según el torneo y la institución
     *  @param  Long idTorneo
     *  @param  Long IdInstitucion
     *  @param  String patron para listar los competidores
     *  @return List<Competidor> lista con todos los competidores.
     */
    List<Competidor> listarCompetidoresPorTorneoEInstitucion(Long idTorneo, Long idInstitucion, String patron);
    
    List<InstitucionCompetidorDTO> listarInstitucionCompetidorDTOPorTorneo(Long idTorneo, String patron);
    
    void eliminarInstitucionDelTorneo(Long idTorneo, Long idInstitucion);
    
    CategoriaLucha recuperarCategoriaLuchaPorSexoEdad(Integer edadInferior, Integer edadSuperior, Sexo sexo);
}
