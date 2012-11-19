/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias.node;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.Torneo;
import com.tdk.services.TorneoServiceRemote;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author sabon
 */
public class CategoriasRootNode extends ChoiceRootNode {

    CategoriasRootNode() {
        super(new CategoriasNodeChildren());
    }

    @Override
    protected List loadKeys(String string) {
        List list = new ArrayList();
        list.addAll(getTorneoService().listarCategoriasForma(string));
        list.addAll(getTorneoService().listarCategoriasLucha(string));
        return list;
    }

    public TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }
}

class CategoriasNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object t) {
        return new Node[]{new CategoriasNode((Categoria) t)};
    }
}
