/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing.actions;

/**
 *
 * @author fernando
 */
public abstract class AbstractMenuSwingAction extends AbstractSwingAction {

    public AbstractMenuSwingAction(String title) {
        super(title);
    }

    @Override
    public void setIslogin(boolean isLogin) {
        if (isLogin) {
            setAcceso(getFuncionality(), getAccess());
        } else {
            setEnabled(false);
        }
    }

    protected abstract String getFuncionality();

    protected abstract Object getAccess();
}