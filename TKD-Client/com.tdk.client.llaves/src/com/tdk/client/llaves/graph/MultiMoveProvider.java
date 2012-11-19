/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author fernando
 */
public class MultiMoveProvider implements MoveProvider {

        private HashMap<Widget, Point> originals = new HashMap<Widget, Point> ();
        private Point original;
        private Scene scene;

        public  MultiMoveProvider() {
            this(null);
        }

        public  MultiMoveProvider(Scene scene) {
            this.scene = scene;
        }

        public void movementStarted (Widget widget) {
           /* 
            if (isNode (object)) {
                for (Object o : getSelectedObjects ())
                    if (isNode (o)) {
                        Widget w = findWidget (o);
                        if (w != null)
                            originals.put (w, w.getPreferredLocation ());
                    }
            } else {
                originals.put (widget, widget.getPreferredLocation ());
            }*/
        }

        public void movementFinished (Widget widget) {
            originals.clear ();
            original = null;
        }

        public Point getOriginalLocation (Widget widget) {
            original = widget.getPreferredLocation ();
            return original;
        }

        public void setNewLocation (Widget widget, Point location) {
            int dx = location.x - original.x;
            int dy = location.y - original.y;
            for (Map.Entry<Widget, Point> entry : originals.entrySet ()) {
                Point point = entry.getValue ();
                entry.getKey ().setPreferredLocation (new Point (point.x + dx, point.y + dy));
            }
        }

    /**
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
