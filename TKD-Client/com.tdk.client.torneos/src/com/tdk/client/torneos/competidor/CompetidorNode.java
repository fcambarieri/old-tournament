/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.competidor;

import com.tdk.client.torneos.CinturonNode;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.CompetidorCategoria;
import com.thorplatform.swing.SwingNode;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author fernando
 */
public class CompetidorNode extends SwingNode<Competidor> {

    public CompetidorNode(Competidor competidor) {
        super(new CompetidorNodeChildren(competidor), competidor);
        setIconBaseWithExtension("com/tdk/client/torneos/competidor/competidor-16x16.png");
    }

    @Override
    public String getDisplay() {
        return getValue().toString();
    }
}

class CompetidorNodeChildren extends Children.Keys {

    private static final Integer CINTURON = new Integer(0);
    private static final Integer LUCHA = new Integer(1);
    private static final Integer FORMA = new Integer(2);
    private Competidor competidor;
    private List<Integer> categorias = null;

    public CompetidorNodeChildren(Competidor c) {
        competidor = c;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        if (competidor != null) {
            categorias = new ArrayList<Integer>();
            categorias.add(CINTURON);
            categorias.add(LUCHA);
            categorias.add(FORMA);
            setKeys(categorias);
        }
    }

    @Override
    protected Node[] createNodes(Object arg0) {
        Node node = null;
        Integer key = (Integer) arg0;
        if (key.equals(CINTURON)) {
            node = new CinturonNode(competidor.getCinturon());
        } else if (key.equals(LUCHA) && competidor.getCompetidorCategoriaLucha() != null) {
            node = new CompetidorCategoriaNode("Lucha: ", competidor.getCompetidorCategoriaLucha(), "com/tdk/client/torneos/combate-16x16.png");
        } else if (key.equals(FORMA) && competidor.getCompetidorCategoriaForma() != null) {
            node = new CompetidorCategoriaNode("Forma: ", competidor.getCompetidorCategoriaForma(), "com/tdk/client/torneos/formas-16x16.png");
        }

        return new Node[]{node};
    }
}

class CompetidorCategoriaNode extends SwingNode<CompetidorCategoria> {

    private String title;

    public CompetidorCategoriaNode(String title, CompetidorCategoria categoria, String iconPath) {
        super(categoria);
        setIconBaseWithExtension(iconPath);
        this.title = title;
    }

    @Override
    public String getDisplay() {
        if (getValue() != null) {
            return title + getValue().getDisplay();
        }
        return "No inscripcto";
    }

    @Override
    public String getHtmlDisplayName() {
        if (getValue() != null) {
            return getValue().getHTMLDisplay();
        }
        return "No inscripcto";
    }

    
}

