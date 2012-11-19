/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author fernando
 */
public class NodeListenerSupportImp implements NodeListenerSupport {

    private Competencia competencia;
    private List<NodeListener> listeners;

    public NodeListenerSupportImp() {
        listeners = new ArrayList<NodeListener>();
    }

    public void addNodeListener(NodeListener listener) {
        listeners.add(listener);
    }

    public void removeNodeListener(NodeListener listener) {
        listeners.remove(listener);
    }

    public void notifyListener(final Competencia c) {
        SwingWorker<Void, NodeListener> sw = new SwingWorker<Void, NodeListener>() {

            @Override
            protected Void doInBackground() throws Exception {
                for(NodeListener l : listeners) {
                    l.update(c);
                }
                return null;
            }

        };

        sw.execute();
    }

    public void setCompetencia(Competencia c) {
        this.competencia = c;
    }

    public Competencia getCompetencia() {
        return competencia;
    }


}
