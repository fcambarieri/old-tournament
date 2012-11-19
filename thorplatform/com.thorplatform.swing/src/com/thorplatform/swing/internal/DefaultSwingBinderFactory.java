/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.internal;

import com.thorplatform.swing.SwingBinder;
import com.thorplatform.swing.SwingBinderFactory;

/**
 *
 * @author fernando
 */
public class DefaultSwingBinderFactory implements SwingBinderFactory{

    public SwingBinder createBinder() {
        return new DefaultSwingBinder();
    }

}
