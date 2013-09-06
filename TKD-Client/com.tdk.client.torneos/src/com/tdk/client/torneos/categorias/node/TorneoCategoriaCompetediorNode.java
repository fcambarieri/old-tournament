/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias.node;

import com.tdk.client.api.ServiceFactory;
import com.tdk.domain.torneo.Categoria;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.services.TorneoServiceRemote;
import com.thorplatform.swing.SwingNode;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;


/**
 *
 * @author sabon
 */
public class TorneoCategoriaCompetediorNode extends SwingNode<Torneo>{

    public TorneoCategoriaCompetediorNode(Torneo c) {
        super(new TipoCategoriaNodeChildren(c), c);
        setIconBaseWithExtension("com/tdk/client/torneos/dobok_16.png");
    }

    @Override
    public String getDisplay() {
        return "Categorias";
    }
}

class TipoCategoriaNodeChildren extends Children.Keys {
    
    private Torneo torneo;
    
    private static final Integer CATEGORIA_FORMA = new Integer(0);
    private static final Integer CATEGORIA_LUCHA = new Integer(1);
    private List<Integer> categorias = null;
    
    public TipoCategoriaNodeChildren(Torneo torneo) {
        this.torneo = torneo;
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        if (torneo != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(CATEGORIA_FORMA);
            categorias.add(CATEGORIA_LUCHA);
            setKeys(categorias);
        }
     }
    
    @Override
    protected Node[] createNodes(Object t) {
        AbstractNode node = null;
        Integer key = (Integer) t;
        if (key.equals(CATEGORIA_FORMA)) {
            node = new TipoCategoriaNode("Forma", "com/tdk/client/torneos/formas-16x16.png", new CategoriaFormaNodeChildren(torneo), torneo);
        } else if (key.equals(CATEGORIA_LUCHA)) {
           node = new TipoCategoriaNode("Lucha", "com/tdk/client/torneos/combate-16x16.png", new CategoriaFormaNodeChildren(torneo), torneo);
        }

        return new Node[]{node};
        
    }
}

class TipoCategoriaNode extends SwingNode<Torneo> {

    private String tipoCategoria;

    public TipoCategoriaNode(String tipoCategoria, String icon, Children children, Torneo t) {
        super(children, t);
        this.tipoCategoria = tipoCategoria;
        if (icon != null) {
            setIconBaseWithExtension(icon);
        }
    }
    
    @Override
    public String getDisplay() {
        return tipoCategoria;
    }
}

class CategoriaFormaNodeChildren extends Children.Keys {

    private Torneo torneo;

    public CategoriaFormaNodeChildren(Torneo torneo) {
        this.torneo = torneo;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(getTorneoService().listarCategoriasForma("%"));
    }
    
    @Override
    protected Node[] createNodes(Object t) {
        return new Node[] {new CategoriaNode((Categoria)t, torneo)};
    }
 
    public TorneoServiceRemote getTorneoService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(TorneoServiceRemote.class);
    }
}

class CategoriaLuchaNodeChildren extends Children.Keys {

    private Torneo torneo;

    public CategoriaLuchaNodeChildren(Torneo torneo) {
        this.torneo = torneo;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(getTorneoService().listarCategoriasLucha("%"));
    }
    
    @Override
    protected Node[] createNodes(Object t) {
        return new Node[] {new CategoriaNode((Categoria)t, torneo)};
    }
 
    public TorneoServiceRemote getTorneoService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(TorneoServiceRemote.class);
    }
}

class CategoriaNode extends AbstractNode {

    private Categoria categoria;
    private Torneo torneo;

    public CategoriaNode(Categoria categoria, Torneo torneo) {
        super(new CategoriaTorneoDTONodeChildren(categoria, torneo));
        this.categoria = categoria;
        this.torneo = torneo;
         setIconBaseWithExtension("com/tdk/client/torneos/vs-16x16.gif");
    }

    @Override
    public String getDisplayName() {
        return categoria.getDisplay();
    }
}

class CategoriaTorneoDTONodeChildren extends Children.Keys {

    private Torneo torneo;
    private Categoria categoria;
    public CategoriaTorneoDTONodeChildren(Categoria categoria, Torneo torneo) {
        this.torneo = torneo;
        this.categoria = categoria;
    }

    public TorneoServiceRemote getTorneoService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(TorneoServiceRemote.class);
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        List<Cinturon> cinturones = getTorneoService().listarCinturones("%");
        List<CategoriaTorneoDTO> categoriasXTorneo = new ArrayList<CategoriaTorneoDTO>();        
        
        for(Cinturon c : cinturones) {
                categoriasXTorneo.add(new CategoriaTorneoDTO(torneo, c, categoria));    
        }
        setKeys(categoriasXTorneo);
    }
    
    @Override
    protected Node[] createNodes(Object t) {
        return new Node[]{new CategoriaTorneoDTONode((CategoriaTorneoDTO) t)};
    }
    
}

class CategoriaTorneoDTONode extends SwingNode<CategoriaTorneoDTO> {

    public CategoriaTorneoDTONode(CategoriaTorneoDTO cat) {
        super(new TorneoCategoriaCompetediorNodeChildren(cat), cat);
        setIconBaseWithExtension("com/tdk/client/torneos/cinturones-16x16.png");
    }
    
    @Override
    public String getDisplay() {
        return getValue().getCinturon().getDescripcion();
    }
    
}

class TorneoCategoriaCompetediorNodeChildren extends Children.Keys {
    
    private CategoriaTorneoDTO categoriaTorneoDTO;
    
    TorneoCategoriaCompetediorNodeChildren(CategoriaTorneoDTO categoriaTorneoDTO) {
        this.categoriaTorneoDTO = categoriaTorneoDTO;
    }

    
    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        if (categoriaTorneoDTO != null) {
            ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
            TorneoServiceRemote torneoService = sf.getService(TorneoServiceRemote.class);
            if (categoriaTorneoDTO.getCategoria() instanceof  CategoriaForma) {
                setKeys(torneoService.listarCompetidoresForma(categoriaTorneoDTO.getCinturon().getId(), 
                        categoriaTorneoDTO.getCategoria().getId(), 
                        categoriaTorneoDTO.getTorneo().getId()));
            } else {
                setKeys(torneoService.listarCompetidoresLucha(categoriaTorneoDTO.getCinturon().getId(), 
                        categoriaTorneoDTO.getCategoria().getId(), 
                        categoriaTorneoDTO.getTorneo().getId()));
            }
        }
 
    }
    
    @Override
    protected Node[] createNodes(Object t) {
        return new Node[]{new CompetidorSimpleNode((Competidor) t)};
    }
}

class CompetidorSimpleNode extends SwingNode<Competidor> {

    public CompetidorSimpleNode(Competidor competidor) {
        super(competidor);
        setIconBaseWithExtension("com/tdk/client/torneos/competidor/competidor-16x16.png");
    }

    @Override
    public String getDisplay() {
        return getValue().toString();
    }
}