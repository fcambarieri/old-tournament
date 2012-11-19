/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.domain;

import java.util.Date;

/**
 *
 * @author fernando
 */
public interface Estado <T>{

    String getDisplayDate();
    
    String getDisplayWithDate() ;
    
    String getDisplayWithOutDate();
    
    T getTipoEstado();
    
    void setTipoEstado(T tipoEstado);
    
    T[] getTipoEstados();
    
    Date getFechaDesde();
    
    void setFechaDesde(Date fechaDesde);

}
