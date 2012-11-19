/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.security;

import com.tdk.domain.security.Acceso;
import com.thorplatform.login.LoginController;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.actions.AbstractMenuSwingAction;
import com.thorplatform.swing.actions.NotifierSwingActionListener;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;

/**
 *
 * @author fernando
 */
public class LoginAction extends AbstractMenuSwingAction {
    
    public LoginAction() {
        super("Login");
        fireLogout();
        setEnabled(true);
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage("com/tdk/client/security/user-auth.png", true)));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final Frame mainFrame = WindowManager.getDefault().getMainWindow();
                mainFrame.addWindowListener(new WindowAdapter() {
                    public void windowActivated(WindowEvent e) {
                        mainFrame.removeWindowListener(this);
                        mainFrame.setTitle("Bienvenido al sistema TKD");
                    }
                });
            }
        });
        
//        StatusDisplayer.getDefault().addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                if (StatusDisplayer.getDefault().getStatusText() == null ||
//                        StatusDisplayer.getDefault().getStatusText().trim().length() == 0) {
//                    
//                    StatusDisplayer.getDefault().setStatusText("In Welcome fernando");
//                }
//                StatusDisplayer.getDefault().setStatusText("Welcome fernando");
//            }
//        });
    }

    public void actionPerformed(ActionEvent arg0) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        LoginController controller = scf.createController(LoginController.class);
        controller.setListener(new LoginController.Listener() {
            public void onInteractionDone(boolean arg0) {
                fire(arg0);
            }
        });
        
        controller.showModal();
    }
    
    private void fire(boolean isLogin) {
        if (isLogin)
            fireLogin();
        
        setEnabled(!isLogin);
    }
    
    private void fireLogin() {
        NotifierSwingActionListener notifier = Lookup.getDefault().lookup(NotifierSwingActionListener.class);
        notifier.fireLoginNotificacion(true);
    }
    
    private void fireLogout() {
        NotifierSwingActionListener notifier = Lookup.getDefault().lookup(NotifierSwingActionListener.class);
        notifier.fireLoginNotificacion(false);
    }

    @Override
    protected String getFuncionality() {
        return "Login";
    }

    @Override
    protected Object getAccess() {
        return Acceso.ALTA;
    }

    public void setIsLogin(boolean arg0) {
        setEnabled(!arg0);
    }

}
