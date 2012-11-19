/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.security;

import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingModalController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public abstract class ConfigurarPermisosController extends SwingModalController{

    protected ConfigurarPermisosForm form = new ConfigurarPermisosForm();
    
    protected final Property<Integer> indexTable = new Property<Integer>("index");
    
    
    @Override
    protected JButton getAcceptButton() {
        return form.btnAceptar;
    }

    @Override
    protected JButton getCancelButton() {
        return form.btnCancelar;
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        configureTableView();
        configureTableBindings();
        installActions();
    }
    
    protected abstract void configureTableView();
    protected abstract void configureTableBindings();

    private void installActions() {
        form.btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                agregarPermisoAction();
            }
        });
        
        form.btnQuitar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                quitarPermisoAction();
            }
        });
        
        form.btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (indexTable.get() != null && indexTable.get().intValue() >= 0)
                        editarPermiso(indexTable.get().intValue());
            }
        });
    }
    
    protected abstract void agregarPermisoAction();
    
    protected abstract void quitarPermisoAction() ;
    
    protected abstract void editarPermiso(int index);

    protected SwingControllerFactory getSwingControllerFactory() {
        return Lookup.getDefault().lookup(SwingControllerFactory.class);
    }
    
    protected void createHighlighterPipeline(JXTable list) {
        HighlighterPipeline highlighter = new HighlighterPipeline(new Highlighter[]{ AlternateRowHighlighter.quickSilver});
        list.setHighlighters(highlighter);
    }
    
    
}
