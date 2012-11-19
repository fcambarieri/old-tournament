/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.competencia.Competencia;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class Node extends Widget implements NodeListener {

    private LabelWidget red;
    private LabelWidget blue;
    private LabelWidget winner;
    private LabelWidget description;
    private Competencia competencia;
    private static final int offSet = 12;
    private static final Font winnerFont = new Font(Font.DIALOG, Font.BOLD, 12);

    public Node(Scene scene, Competencia c) {
        this(scene, c, new Rectangle(200, 100));
    }

    public Node(Scene scene, Competencia c, Rectangle r) {
        super(scene);

        if (c == null) {
            throw new NullPointerException("The competicion is null.");
        }

        this.red = new LabelWidget(scene);
        this.blue = new LabelWidget(scene);
        this.winner = new LabelWidget(scene);
        this.description = new LabelWidget(scene, "(".concat(c.getNumero().toString()));
        this.description.setAlignment(LabelWidget.Alignment.LEFT);

        setCompetencia(c);

        updateUI();


        setPreferredBounds(r);
        setPreferredSize(new Dimension((int) r.getWidth() + offSet, (int) r.getHeight() + 20));

        init();
    }

    private void init() {

        setBorder(new CompetenciaBorder());

        addChild(red);
        addChild(blue);
        addChild(description);

        getActions().addAction(ActionFactory.createPopupMenuAction(new NodeMenuProvider()));

        if (isRoot()) {
            addChild(winner);
            int x = (int) (getPreferredBounds().getWidth());
            int y = (int) (getPreferredBounds().getHeight() / 2) + offSet;
            winner.setPreferredLocation(new Point(x, y));
        }

        red.setPreferredLocation(new Point(0, (int) getPreferredBounds().getHeight()));
        blue.setPreferredLocation(new Point(0, offSet));

        int x = (int) (getPreferredBounds().getCenterX());
        int y = (int) (getPreferredBounds().getCenterY());

        description.setPreferredLocation(new Point(x, y));

    }

    private boolean isRoot() {
        return getCompetencia().getCompetenciaPadre() == null;
    }

    private Color isFinal(Competidor ganador, Competidor comp) {
        Color c = Color.BLACK;
        if (ganador != null && ganador.equals(comp)) {
            c = Color.GREEN;
        }
        return c;
    }

    public LabelWidget getNodeAzul() {
        return blue;
    }

    public LabelWidget getNodeRojo() {
        return red;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Competencia competencia) {
        this.competencia = competencia;
    }

    private void updateUI() {
        this.red.setLabel(competencia.getCompetidorRojo() != null ? competencia.getCompetidorRojo().getDisplayCompetidor() : "S/N");
        this.blue.setLabel(competencia.getCompetidorAzul() != null ? competencia.getCompetidorAzul().getDisplayCompetidor() : "S/N");
        this.winner.setLabel(competencia.getCompetidorGanador() != null ? competencia.getCompetidorGanador().getDisplayCompetidor() : "S/N");

        updateWinnerFont();
    }

    private void updateWinnerFont() {
        Competidor w = competencia.getCompetidorGanador();
        Competidor cr = competencia.getCompetidorRojo();
        Competidor ca = competencia.getCompetidorAzul();
        if (w != null) {
            if (w.equals(cr)) {
                red.setFont(winnerFont);
            } else if (w.equals(ca)) {
                blue.setFont(winnerFont);
            }
        }
    }

    public void update(Competencia c) {
        setCompetencia(c);
        updateUI();
    }

    class CompetenciaBorder implements Border {

        public Insets getInsets() {
            return new Insets(1, 1, 1, 1);
        }

        public void paint(Graphics2D g2, Rectangle bounds) {

            Competidor ganador = getCompetencia().getCompetidorGanador();
            Competidor ca = getCompetencia().getCompetidorAzul();
            Competidor cr = getCompetencia().getCompetidorRojo();

            g2.setPaint(isFinal(ganador, ca));
            //linea horizontal superior
            g2.drawLine(bounds.x, bounds.y + offSet, bounds.width, bounds.y + offSet);
            //linea vertical superior
            g2.drawLine(bounds.x + bounds.width, bounds.y + offSet, bounds.x + bounds.width, bounds.y + bounds.height / 2);


            g2.setPaint(isFinal(ganador, cr));
            //linea horizontal inferior
            g2.drawLine(bounds.x, bounds.y + bounds.height - offSet, bounds.width, bounds.y + bounds.height - offSet);
            //linea vertical inferior
            g2.drawLine(bounds.x + bounds.width, bounds.y + bounds.height / 2, bounds.x + bounds.width, bounds.y + bounds.height - offSet);
        }

        public boolean isOpaque() {
            return true;
        }
    }
}

