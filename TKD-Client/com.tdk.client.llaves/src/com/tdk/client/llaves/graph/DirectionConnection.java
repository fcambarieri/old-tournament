/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.llaves.graph;

import com.tdk.client.llaves.graph.ConnectionNode;
import com.tdk.domain.torneo.competencia.Competencia;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class DirectionConnection implements ConnectionFactory {

    private Scene scene;

    public DirectionConnection() {
        this(null);
    }

    public DirectionConnection(Scene scene) {
        setScene(scene);
    }

    public ConnectionWidget createConnection(Widget sourceNode, Widget targetNode, Competencia c, LayerWidget layer) {
        ConnectionNode conn = new ConnectionNode(scene, c);

        //boolean izq = c.getCompetidorAzul() == null;
        
        Node source = (Node) sourceNode;
        Node target = (Node) targetNode;

        Competencia compPadre = target.getCompetencia();
        
        LabelWidget labelTarget = null;
        
        if (compPadre.getCompentenciaLeft().equals(source.getCompetencia()))
            labelTarget = target.getNodeAzul();
        else if (compPadre.getCompentenciaRight().equals(source.getCompetencia()))
            labelTarget = target.getNodeRojo();

        conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceNode, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
        conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(labelTarget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));

        //conn.setSourceAnchor(AnchorFactory.createDirectionalAnchor(sourceNode, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));
        //conn.setTargetAnchor(AnchorFactory.createDirectionalAnchor(labelTarget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL));

        conn.setRouter(RouterFactory.createOrthogonalSearchRouter(layer));
        
        return conn;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
