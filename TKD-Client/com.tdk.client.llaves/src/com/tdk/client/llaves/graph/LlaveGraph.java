/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;
import com.tdk.domain.torneo.competencia.Llave;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class LlaveGraph extends Scene implements NodeListener {

    private Map<Competencia, Node> mapNodes = new HashMap<Competencia, Node>();
    private Map<Competencia, ConnectionNode> mapConnection = new HashMap<Competencia, ConnectionNode>();

    private NodeListenerSupport listenerSupport = Lookup.getDefault().lookup(NodeListenerSupport.class);

    private LayerWidget nodeLayer;
    private LayerWidget connectionLayer;
    private ConnectionFactory connectionFactory;

    private static final int e = 15;
    private static final int ERROR = 5;

    public LlaveGraph() {

        nodeLayer = new LayerWidget(this);
        addChild(nodeLayer);

        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);

        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createZoomAction());

        setConnectionFactory(new DirectionConnection(this));

        getActions().addAction(new PositionAction(StatusGraph.status));

        listenerSupport.addNodeListener(this);

    }

    

    public void build(Llave llave) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Competencia root = llave.getCompetencia();
        Dimension d = tk.getScreenSize();
        int x = (int) (d.getWidth() / 4);
        int y = (int) (d.getHeight() / 4);
        int level = 3;
        int h = (30 * level) + e * level;

        calcLocations(root, x, y, h, level);

        buildConnections(root);
    }

    private void calcLocations(Competencia c, int x, int y, int h, int level) {
        if (c != null) {

            if (h < 50) h = 50;


            Node n = new Node(this, c, new Rectangle(170, h));
            n.setPreferredLocation(new Point(x, y));
            n.getActions().addAction(ActionFactory.createMoveAction());

            System.out.println("Punto (" + x + " , " + y + ") y h: " + h + " level: " + level);

            x = (int) (x - n.getPreferredBounds().getWidth() - 50);

            int leftOff = h / 4 + ERROR / level;
            int rightOff = h - (h / 4);

            nodeLayer.addChild(n);

            mapNodes.put(c, n);

            calcLocations(c.getCompentenciaLeft(), x, y - leftOff, h / 2, level - 1);
            calcLocations(c.getCompentenciaRight(), x, y + rightOff, h / 2, level - 1);
        }
    }

    private void buildConnections(Competencia c) {
        Iterator<Competencia> it = mapNodes.keySet().iterator();
        while (it.hasNext()) {
            createConnections(it.next());
        }
    }

    private void createConnections(Competencia c) {
        if (c != null) {
            if (c.getCompetenciaPadre() != null) {
                Widget nodeSource = mapNodes.get(c);
                Widget nodeTarget = mapNodes.get(c.getCompetenciaPadre());

                createLines(nodeSource, nodeTarget, c);
            }
        }
    }

    private void createLines(Widget sourceNode, Widget targetNode, Competencia c) {
        if (getConnectionFactory() != null) {
            ConnectionWidget edge = getConnectionFactory().createConnection(sourceNode, targetNode, c, nodeLayer);
            connectionLayer.addChild(edge);
            mapConnection.put(c, (ConnectionNode)edge);
        }
    }

    private void clearAll() {
        mapNodes.clear();
        mapConnection.clear();
        connectionLayer.removeChildren();
        nodeLayer.removeChildren();
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void update(Competencia c) {
        if (c != null) {
            Node w = mapNodes.get(c);
            if (w != null) w.update(c);

            ConnectionNode cw = mapConnection.get(c);
            if (cw != null) cw.update(c);

            repaint();
        }
    }
}
