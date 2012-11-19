/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public interface ConnectionFactory {

    ConnectionWidget createConnection(Widget source, Widget target, Competencia c, LayerWidget layer);
}
