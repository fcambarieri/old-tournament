/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias.node;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.torneo.Categoria;
import com.tdk.services.TorneoServiceRemote;
import com.thorplatform.swing.SwingNode;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;


/**
 *
 * @author sabon
 */
public class CategoriasNode extends SwingNode<Categoria>{

    public CategoriasNode(Categoria categoria) {
        super(categoria);
    }
    
    @Override
    public String getDisplay() {
        return getValue().getDisplay();
    }
}

class CategoriaNodeChildren extends Children.Keys {

    private Categoria categoria;
    
    CategoriaNodeChildren(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        if (categoria != null) {
            //setKeys(getTorneoService().li);
        }
    }

     public TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }
    
    @Override
    protected Node[] createNodes(Object t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
