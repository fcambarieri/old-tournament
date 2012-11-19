package com.thorplatform.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import org.openide.util.Utilities;

/**
 *
 * @author Fernando
 */
public abstract class SwingModalController extends SwingController {
    
    private JDialog modalDialog;
    private boolean modalResult;
    
    private void onAcceptModalDialog() {
        acceptModalDialog();
        modalResult = true;
        modalDialog.setVisible(false);
    }
    
    private void onCancelModalDialog() {
        cancelModalDialog();
        modalResult = false;
        modalDialog.setVisible(false);
    }
    
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        getAcceptButton().setEnabled(canAcceptDialog());
    }

    /**
     * Retorna verdadero si se presiono el botòn de aceptar falso si
     * se presiono el botón de cancelar.
     * @return boolean
     */
    public boolean showModal() {
        modalDialog = getGuiUtils().panelDialog(getForm(), getTitle(), getDefaultButton());
        modalResult = false;
        
        if (getFocusedComponent() != null)
            getFocusedComponent().requestFocusInWindow();
        
        modalDialog.setVisible(true);
        return modalResult;
    }
    
    @Override
    public void initController() {
        getAcceptButton().setEnabled(false);
        
        getAcceptButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAcceptModalDialog();
            }
        });
        
        getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelModalDialog();
            }
        });
        
        getCancelButton().setIcon(new ImageIcon(Utilities.loadImage("com/thorplatform/swing/cancelar.png")));
        
        getAcceptButton().setIcon(new ImageIcon(Utilities.loadImage("com/thorplatform/swing/ok-16x16.png")));
        
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        ActionListener escListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelModalDialog();
            }
        };
        getForm().registerKeyboardAction(escListener, stroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    protected void acceptModalDialog() {
    }
    
    protected void cancelModalDialog() {
    }
    
       
    protected JButton getDefaultButton() {
        return getAcceptButton();
    }
    
    @Override
    protected JComponent getFocusedComponent() {
        return null;
    }
    
    protected abstract JButton getAcceptButton();
    protected abstract JButton getCancelButton();
    
}
