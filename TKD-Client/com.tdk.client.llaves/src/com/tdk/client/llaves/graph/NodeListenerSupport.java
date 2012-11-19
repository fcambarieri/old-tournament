/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;

/**
 *
 * @author fernando
 */
public interface NodeListenerSupport {

    void addNodeListener(NodeListener listener);

    void removeNodeListener(NodeListener listener);

    void notifyListener(Competencia c) ;
}
