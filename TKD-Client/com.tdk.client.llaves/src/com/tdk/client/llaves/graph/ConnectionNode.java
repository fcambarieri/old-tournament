/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;
import java.awt.Color;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author fernando
 */
public class ConnectionNode extends ConnectionWidget implements NodeListener{

    private Competencia competencia;
    private static final Color defaultColor = Color.BLACK;

    public ConnectionNode(Scene scene, Competencia c) {
        super(scene);
        this.competencia = c;
        //setTargetAnchorShape (AnchorShape.TRIANGLE_FILLED);
    }

    @Override
    protected void paintWidget() {

        setLineColor(getCompetenciaLineColor());

        super.paintWidget();
    }

    private Color getCompetenciaLineColor() {
        Color c = defaultColor;
        switch(competencia.getEstadoActual().getTipoEstado()) {
            case FINALIZADA:
                c = Color.GREEN;

        }

        return c;
    }

    public void update(Competencia c) {
        if (competencia != null && competencia.equals(c)) {
            this.competencia = c;
        }
    }

}
