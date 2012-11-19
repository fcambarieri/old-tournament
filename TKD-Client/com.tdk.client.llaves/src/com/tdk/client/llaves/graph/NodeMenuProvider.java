/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import java.awt.Point;
import java.util.Collection;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class NodeMenuProvider implements PopupMenuProvider{


    public JPopupMenu getPopupMenu(Widget w, Point arg1) {
        JPopupMenu menu = new JPopupMenu("menu");
        Node node = (Node)w;

        //Lookup.Template<NodeMenuItem> template = new Lookup.Template<NodeMenuItem>();
        //Lookup.Result<NodeMenuItem> collections = Lookup.getDefault().lookup(template);

        Collection<? extends NodeMenuItem> collections = Lookup.getDefault().lookupAll(NodeMenuItem.class);
        for(NodeMenuItem nmi : collections) {
            nmi.setCompetencia(node.getCompetencia());
            menu.add(nmi);
        }

        return menu;
    }

}
