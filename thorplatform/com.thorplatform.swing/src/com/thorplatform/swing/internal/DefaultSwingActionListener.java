/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.internal;

import com.thorplatform.swing.actions.NotifierSwingActionListener;
import com.thorplatform.swing.actions.SwingActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author fernando
 */
public class DefaultSwingActionListener implements NotifierSwingActionListener{

    private List<SwingActionListener> listeners = new ArrayList<SwingActionListener>();
    private boolean isLogin = false;
    
    public void addSwingActionListener(SwingActionListener swingActionListener) {
        if(swingActionListener != null && !listeners.contains(swingActionListener))
            listeners.add(swingActionListener);
    }

    public void remove(int index) {
        if (index < 0 || index >= listeners.size())
            throw new IndexOutOfBoundsException("El index se encuentra fuera de rango");
        listeners.remove(index);
    }

    public void remove(SwingActionListener swingActionListener) {
        if (listeners.contains(swingActionListener))
            listeners.remove(swingActionListener);
    }

    public SwingActionListener getSwingActionListener(int index) {
        if (index < 0 || index >= listeners.size())
            throw new IndexOutOfBoundsException("El index se encuentra fuera de rango");
        
        return listeners.get(index);
    }

    public void fireLoginNotificacion(boolean isLogin) {
        this.setIsLogin(isLogin);
        SwingUtilities.invokeLater(new FireListeners(listeners, isLogin));
        
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    private void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    class FireListeners implements Runnable {
        
        private List<SwingActionListener> listeners;
        private boolean isLogin;
        
        public FireListeners(List<SwingActionListener> listeners, boolean isLogin) {
            this.listeners = listeners;
            this.isLogin = isLogin;
        }
        
        public void run() {
            for(SwingActionListener wal : listeners) {
                wal.setIsLogin(isLogin);
            }
        }
        
    }
}
