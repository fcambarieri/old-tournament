/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.utils;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

/**
 *
 * @author fernando
 */
public interface GuiUtils {

    JDialog panelDialog(JComponent form, String title, JButton defaultButton);
    
    JDialog createNoModalDialog(JComponent form, String title, JButton defaultButton);
   
    void notificacion(String message);

    void warnnig(String message);
    
    void warnnig(Throwable ex);
    
    void warnnig(Throwable ex, Class classError);

    boolean confirmation(String message);

    void notificationError(Throwable ex);

    void notificationError(String message);

    void notificationError(Throwable ex, Class classError);
    
    String findException(Throwable ex, Class classNameException);
}
