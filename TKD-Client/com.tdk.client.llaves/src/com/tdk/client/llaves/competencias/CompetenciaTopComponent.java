/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves.competencias;

import com.tdk.client.llaves.graph.LlaveGraph;
import com.tdk.domain.torneo.competencia.Llave;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author fernando
 */
public class CompetenciaTopComponent extends TopComponent {

    static final String ICON_PATH = "com/tdk/client/llaves/llaves-16x16.png";
    private Llave llave;

    public CompetenciaTopComponent(Llave llave) {
        setToolTipText(llave.getDisplay());
        setName(llave.getDisplay());
        setIcon(Utilities.loadImage(ICON_PATH, true));

        this.llave = llave;
       // model.addNode(llave);
        initComponent();
    }

    private void initComponent() {
        setLayout(new BorderLayout());

        LlaveGraph graph = new LlaveGraph();


        JScrollPane scrollPanel = new JScrollPane(graph.createView());
        scrollPanel.getHorizontalScrollBar().setUnitIncrement(32);
        scrollPanel.getHorizontalScrollBar().setBlockIncrement(256);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(32);
        scrollPanel.getVerticalScrollBar().setBlockIncrement(256);

        add(scrollPanel, BorderLayout.CENTER);

        graph.build(llave);
    }
}
