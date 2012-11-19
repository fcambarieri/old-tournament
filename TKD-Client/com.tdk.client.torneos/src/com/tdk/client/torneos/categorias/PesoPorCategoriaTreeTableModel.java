/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.categorias;

import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Peso;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author fernando
 */
public class PesoPorCategoriaTreeTableModel extends DefaultTreeTableModel {

    private String[] columnNames = {"Categoria", "Edad desde", "Edad hasta", "Peso desde", "Peso hasta"};
    private Class[] columnClasses = {AbstractTreeTableModel.hierarchicalColumnClass, Integer.class, Integer.class, Integer.class, Integer.class};

    public PesoPorCategoriaTreeTableModel() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        setRoot(rootNode);
    }

    public void rebuildModel(List<CategoriaLucha> categorias) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        buildModel(rootNode, categorias);
        setRoot(rootNode);
    }

    private DefaultMutableTreeNode buildModel(DefaultMutableTreeNode rootNode, List<CategoriaLucha> categorias) {
        Collections.sort(categorias, new Comparator<CategoriaLucha>() {

            public int compare(CategoriaLucha o1, CategoriaLucha o2) {
                return o1.getSexo().toString().compareToIgnoreCase(o2.getSexo().toString());
            }
        });

        int i = 0;
        while (i < categorias.size()) {
            CategoriaLucha categoriaLucha = categorias.get(i);
            Long idGrupo = categoriaLucha.getId();
            String descripcionGrupo = categoriaLucha.getDescripcion();
            DefaultMutableTreeNode grupoNode = new DefaultMutableTreeNode();
            grupoNode.setUserObject(descripcionGrupo);

            while (i < categorias.size() && idGrupo.equals(categorias.get(i).getId())) {
                DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode();
                itemNode.setUserObject(categorias.get(i));
                grupoNode.add(itemNode);
                i = i + 1;
            }

            rootNode.add(grupoNode);
        }

        return rootNode;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Class getColumnClass(int column) {
        return columnClasses[column];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(Object node, int column) {
        Object result = null;

        if (column == 0) {
            result = node;
        } else {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
            if (treeNode.getUserObject() instanceof CategoriaLucha) {
                CategoriaLucha categoria = (CategoriaLucha) treeNode.getUserObject();

                switch (column) {
                    case 1:
                        result = categoria.getDescripcion();
                        break;
                    case 2:
                        result = categoria.getEdadInferior();
                        break;
                    case 3:
                        result = categoria.getEdadSuperior();
                        break;
                    
                }
            } else if (treeNode.getUserObject() instanceof Peso) {
                Peso peso = (Peso) treeNode.getUserObject();
                switch (column) {
                    case 4:
                        result = peso.getPesoInferior();
                        break;
                    case 5:
                        result = peso.getPesoSuperior();
                        break;
                }
            }
        }

        return result;
    }
}
