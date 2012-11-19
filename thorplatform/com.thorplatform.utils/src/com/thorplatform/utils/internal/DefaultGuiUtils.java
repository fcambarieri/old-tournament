/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.utils.internal;

import com.thorplatform.utils.ConfirmationForm;
import com.thorplatform.utils.AdvertenciaForm;
import com.thorplatform.utils.ErrorForm;
import com.thorplatform.utils.GuiUtils;
import com.thorplatform.utils.NotificacionForm;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.openide.windows.WindowManager;

/**
 *
 * @author fernando
 */
public class DefaultGuiUtils implements GuiUtils {

    public JDialog panelDialog(JComponent form, String title, JButton defaultButton) {
        JDialog dialog = new JDialog(WindowManager.getDefault().getMainWindow());
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.add(form);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setTitle(title);
        dialog.pack();
        dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        if (defaultButton != null) {
            dialog.getRootPane().setDefaultButton(defaultButton);
        }
        return dialog;
    }

    public void notificacion(String message) {
        NotificacionForm form = new NotificacionForm();
        final JDialog dialog = panelDialog(form, "Notificación", form.btnAceptar);
        form.txtArea.setText(message);
        form.setListener(new Listener() {

            public void onClick() {
                dialog.setVisible(false);
            }
        });

        dialog.setVisible(true);

    }

    public void notificationError(String message) {
        final ErrorForm form = new ErrorForm();
        final JDialog dialog = panelDialog(form, "Advertencia", form.getDefaultButton());

        form.initForm(message, new Throwable(message));

        form.setListener(new Listener() {

            public void onClick() {
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void warnnig(String message) {
        final AdvertenciaForm form = new AdvertenciaForm();
        final JDialog dialog = panelDialog(form, "Advertencia", form.btnAceptar);
        form.setListener(new Listener() {

            public void onClick() {
                dialog.setVisible(false);
            }
        });
        form.txtArea.setText("<HTML>" + message + "</HTML>");
        form.txtArea.setEditable(false);
        dialog.setVisible(true);
    }

    public boolean confirmation(String message) {
        final boolean[] confirmation = new boolean[1];
        final ConfirmationForm form = new ConfirmationForm();
        final JDialog dialog = panelDialog(form, "Confirmatión", form.btnAceptar);
        form.setListener(new ConfirmationForm.Listener() {

            public void onOkClick() {
                confirmation[0] = true;
                dialog.setVisible(false);
            }

            public void onCancelClick() {
                confirmation[0] = false;
                dialog.setVisible(false);
            }
        });
        form.txtArea.setText("<HTML>" + message + "</HTML>");
        dialog.setVisible(true);
        return confirmation[0];
    }

    public void notificationError(Throwable throwable) {
        String message;
        if (throwable.getCause() != null) {
            message = throwable.getCause().getMessage();
        } else {
            message = throwable.getMessage();
        }
        if (message == null || message.trim().length() == 0) {
            message = throwable.getClass().getName() + "<BR>" +
                    "Error sin información adicional.";
        }

        final ErrorForm form = new ErrorForm();
        final JDialog dialog = panelDialog(form, "Advertencia", form.getDefaultButton());

        form.initForm(message, throwable);

        form.setListener(new Listener() {

            public void onClick() {
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);
    }

    public void notificationError(Throwable ex, Class classError) {
        String msg = findException(ex, classError);
        if (msg != null) {
            notificationError(msg);
        } else {
            notificationError(ex);
        }
    }

    public String findException(Throwable ex, Class classNameException) {
        Throwable t = ex;
        String exceptionClassName = classNameException != null ? classNameException.getSimpleName() : "";
        try {
            while (!t.getCause().getClass().getSimpleName().equalsIgnoreCase(exceptionClassName)) {
                t = t.getCause();
            }
            return t.getCause().getMessage();
        } catch (Throwable exception) {
        }
        return null;
    }

    public void warnnig(Throwable ex, Class classError) {
        String msg = findException(ex, classError);
        if (msg != null)
            warnnig(msg);
        else {
            warnnig(ex);
        }
    }

    public JDialog createNoModalDialog(JComponent form, String title, JButton defaultButton) {
        JDialog dialog = new JDialog(WindowManager.getDefault().getMainWindow());
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.add(form);
        dialog.setModal(false);
        dialog.setResizable(false);
        dialog.setTitle(title);
        dialog.pack();
        dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        if (defaultButton != null) {
            dialog.getRootPane().setDefaultButton(defaultButton);
        }
        return dialog;
    }

    public void warnnig(Throwable throwable) {
         String message;
        if (throwable.getCause() != null) {
            message = throwable.getCause().getMessage();
        } else {
            message = throwable.getMessage();
        }
        if (message == null || message.trim().length() == 0) {
            message = throwable.getClass().getName() + "<BR>" +
                    "Error sin información adicional.";
        }

        warnnig(message);
    }

}
