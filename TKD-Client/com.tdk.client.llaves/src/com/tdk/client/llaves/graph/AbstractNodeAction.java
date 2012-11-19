/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public abstract class AbstractNodeAction extends AbstractAction implements NodeMenuItem {

    protected Competencia competencia;
    private NodeListenerSupport listenerSupport = Lookup.getDefault().lookup(NodeListenerSupport.class);

    public AbstractNodeAction() {
        putValue(NAME, getTitle());
    }

    public void setCompetencia(Competencia c) {
        this.competencia = c;
    }

    protected void notifyListeners(Competencia c) {
        this.competencia = c;
        listenerSupport.notifyListener(competencia);
    }

    public abstract String getTitle();

    public abstract Icon getIcon();
}
