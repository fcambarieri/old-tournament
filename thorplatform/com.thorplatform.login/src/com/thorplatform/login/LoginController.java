package com.thorplatform.login;

import com.thorplatform.swing.LoginGlassPane;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.validator.StringPropertyValidator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.openide.util.Lookup;

public class LoginController extends SwingModalController {

    private Listener listener;
    private LoginForm form;
    public final Property<String> userName = new Property("userName");
    public final Property<String> pwd = new Property("password");
    private LoginGlassPane loginGlassPane;
    private boolean loginSuccessful;
    private JButton btnAcepetar = new JButton();
    private JButton btnCancelar = new JButton();

    public LoginController() {
        this.form = new LoginForm();
    }

    @Override
    public void initController() {
        super.initController();

        getSwingBinder().bindTextComponentToString(this.form.txtUserName, this.userName);
        getSwingBinder().bindTextComponentToString(this.form.txtPassword, this.pwd);

        getSwingValidator().addSwingValidator(new StringPropertyValidator(this.userName, "Ingrese un nombre de usuario"));
        
        configureView();
    }

    @Override
    protected JButton getAcceptButton() {
        return this.btnAcepetar;
    }

    @Override
    protected JButton getCancelButton() {
        return this.btnCancelar;
    }

    @Override
    protected JPanel getForm() {
        return this.form;
    }

    private void configureView() {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        ActionListener escListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LoginController.this.processLoginCancelled();
            }
        };
        this.form.registerKeyboardAction(escListener, stroke, 1);

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LoginController.this.login();
            }
        };
        setTitle("Login");
        this.form.txtUserName.addActionListener(actionListener);
        this.form.txtPassword.addActionListener(actionListener);

        this.loginGlassPane = new LoginGlassPane();
        clearFeedback();
    }

    private void clearFeedback() {
        this.form.pnlFeedBack.setOpaque(false);
        this.form.lblFeedback.setText("");
        this.form.pnlFeedBack.setVisible(false);
    }

    private void showFeedback(String feedback) {
        this.form.pnlFeedBack.setBackground(new Color(0, 0, 0, 20));
        this.form.lblFeedback.setText(feedback);
        this.form.pnlFeedBack.setVisible(true);
        this.form.pnlFeedBack.setOpaque(true);
    }

    private void login() {
        clearFeedback();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JRootPane rootPane = SwingUtilities.getRootPane(LoginController.this.form.txtUserName);
                LoginController.this.loginGlassPane.setSize(LoginController.this.form.getSize());
                rootPane.setGlassPane(LoginController.this.loginGlassPane);
                LoginController.this.loginGlassPane.start();
                Thread performer = new Thread(new LoginController.LoginRunnable(), "Performer");
                performer.start();
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void processSuccessfulLogin() {
        this.loginSuccessful = true;
        if (this.listener != null) {
            this.listener.onInteractionDone(this.loginSuccessful);
        }
        this.btnAcepetar.doClick();
    }

    private void processUnsuccessfulLogin(Throwable t) {
        this.loginSuccessful = false;

        Throwable rootCause = t;
        while ((rootCause != t.getCause()) && (t.getCause() != null)) {
            rootCause = t.getCause();
        }
        String message = rootCause.getMessage();
        if (message == null) {
            message = "No se pudo autenticar con el servidor";
        }
        showFeedback(message);
        //this.loginGlassPane.stop();
    }

    private void processLoginCancelled() {
        this.loginSuccessful = false;
        if (this.listener != null) {
            this.listener.onInteractionDone(this.loginSuccessful);
        }
    }

    private LoginClient getLoginClient() {
        return (LoginClient) Lookup.getDefault().lookup(LoginClient.class);
    }

    public boolean wasLoginSuccessful() {
        return this.loginSuccessful;
    }

    private class LoginRunnable implements Runnable {

        private LoginRunnable() {
        }

        public void run() {
            try {
                Thread.sleep(3000L);
                String server = "//localhost:1198/loginServer";
                //LoginController.access(LoginController.this, false);
                //String loginServer, String usuario, String password, String workPlace
                LoginController.this.getLoginClient().login(server, LoginController.this.userName.get(), LoginController.this.pwd.get(), null);
                LoginController.this.processSuccessfulLogin();
            } catch (Throwable t) {
                LoginController.this.processUnsuccessfulLogin(t);
            } finally {
                LoginController.this.loginGlassPane.stop();
            }
        }
    }

    public static abstract interface Listener {

        public abstract void onInteractionDone(boolean paramBoolean);
    }
}
