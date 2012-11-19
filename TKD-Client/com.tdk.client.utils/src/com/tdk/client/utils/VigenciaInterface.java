/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.utils;

import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import java.util.Date;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public interface VigenciaInterface {

    void initForUpdate(Date fechaDesde, Date fechaHasta) ;
    
    void initForConsulta(Date fechaDesde, Date fechaHasta) ;
    
    Date getFechaDesde();
    
    Date getFechaHasta() ;
    
    AbstractSwingValidator[] getValidators();
    
    void setControllerChangeEvent(SwingControllerChangeEvent controllerChangeEvent);
    
    JPanel getPanel();
    
}
