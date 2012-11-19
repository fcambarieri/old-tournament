/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Fernando
 */
public class GlobalToolBarAction extends AbstractAction implements Presenter.Toolbar{

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Component getToolbarPresenter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
