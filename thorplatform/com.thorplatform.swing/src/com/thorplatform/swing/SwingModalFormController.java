/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import java.awt.BorderLayout;
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
 * @author fernando
 */
public abstract class SwingModalFormController extends SwingController {

    private SwingModalForm swingModalPanel = new SwingModalForm();
    private JDialog modalDialog;
    private boolean modalResult;

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        getAcceptButton().setEnabled(canAcceptDialog());
    }

    @Override
    public void initController() {

        getSwingModalPanel().setLayout(new BorderLayout());
        getSwingModalPanel().add(getForm(), BorderLayout.CENTER);

        getSwingValidator().setJLabel(getSwingModalPanel().lblMessage);

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
        getSwingModalPanel().registerKeyboardAction(escListener, stroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

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

    public boolean showModal() {
        modalDialog = getGuiUtils().panelDialog(getSwingModalPanel(), getTitle(), getDefaultButton());
        modalResult = false;

        if (getFocusedComponent() != null) {
            getFocusedComponent().requestFocusInWindow();
        }
        modalDialog.setVisible(true);
        return modalResult;
    }

    protected void acceptModalDialog() {
    }

    protected void cancelModalDialog() {
    }

    protected JButton getDefaultButton() {
        return getSwingModalPanel().btnAceptar;
    }

    protected JButton getAcceptButton() {
        return getSwingModalPanel().btnAceptar;
    }

    protected JButton getCancelButton() {
        return getSwingModalPanel().btnCancelar;
    }

    private SwingModalForm getSwingModalPanel() {
        return swingModalPanel;
    }
}
