/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.torneos;

import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.torneo.Competidor;
import com.thorplatform.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.text.Utilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class CompetidoresTorneoTreeTableModel extends DefaultTreeTableModel {

    private String[] columnNames = {"Institucion", "Cinturon" , "Sexo" , "Categoria Lucha" , "Categoria Forma" };
    private Class[] columnClasses = {AbstractTreeTableModel.hierarchicalColumnClass, String.class, String.class, String.class, String.class};
    
    public CompetidoresTorneoTreeTableModel() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        setRoot(rootNode);
    }
    
    public void rebuildModel(List<Competidor> competidores) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        buildModel(rootNode, competidores);
        setRoot(rootNode);
    }
    
    private DefaultMutableTreeNode buildModel(DefaultMutableTreeNode rootNode, List<Competidor> competidores) {
        Collections.sort(competidores, new Comparator<Competidor>() {
            public int compare(Competidor o1, Competidor o2) {
                return o1.getAlumno().getInstitucion().getNombre().compareToIgnoreCase(o2.getAlumno().getInstitucion().getNombre());
            }
        });
        
        int i = 0;
        while (i < competidores.size()) {
            Competidor competidor = competidores.get(i);
            Long idGrupo = competidor.getAlumno().getInstitucion().getId();
            String descripcionGrupo = competidor.getAlumno().getInstitucion().getNombre();
            DefaultMutableTreeNode grupoNode = new DefaultMutableTreeNode();
            grupoNode.setUserObject(descripcionGrupo);
            
            while (i < competidores.size() && idGrupo.equals(competidores.get(i).getAlumno().getInstitucion().getId())) {
                DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode();
                itemNode.setUserObject(competidores.get(i));
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
    
    public Object getValueAt(Object node, int column) {
        Object result = null;
        
        if (column == 0)
            result = node;
        else {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
            if (treeNode.getUserObject() instanceof Competidor) {
                Competidor competidor = (Competidor) treeNode.getUserObject();
                Alumno alumno = competidor.getAlumno();
                if (column == 1)
                    result = competidor.getCinturon().getDescripcion();
                else if (column == 2)
                    result = alumno.getPersonaFisica().getSexo().toString();
                else if (column == 3)
                    result = competidor.getCompetidorCategoriaLucha() != null ? competidor.getCompetidorCategoriaLucha().getDisplay() : "No inscripto";
                else if (column == 4)
                    result = competidor.getCompetidorCategoriaForma() != null ? competidor.getCompetidorCategoriaForma().getDisplay() : "No inscripto";
            }
        }
        
        return result;
    }
    
    public DefaultMutableTreeNode findNodeWithUserObject(Object userObject) {
        DefaultMutableTreeNode result = null;
        
        if (Competidor.class.isAssignableFrom(userObject.getClass())) {
            Competidor userObjectAsItemInbox = (Competidor) userObject;
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getRoot();
            Enumeration nodes = rootNode.preorderEnumeration();
            while (nodes.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
                Object nodeObject = node.getUserObject();
                if (nodeObject != null && Competidor.class.isAssignableFrom(nodeObject.getClass())) {
                    Competidor nodeObjectAsItemInbox = (Competidor) nodeObject;
                    if (userObjectAsItemInbox.getAlumno().getInstitucion().equals(nodeObjectAsItemInbox.getAlumno().getInstitucion()) &&
                            userObjectAsItemInbox.getAlumno().equals(nodeObjectAsItemInbox.getAlumno())) {
                        result = node;
                        break;
                    }
                }
            }
        }
        
        return result;
    }
    
    public List<Long> findExpandedIds(JXTreeTable treeTable) {
        List<Long> result = new ArrayList<Long>();
        
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getRoot();
        Enumeration nodes = rootNode.preorderEnumeration();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            Object nodeObject = node.getUserObject();
            if (nodeObject != null && Competidor.class.isAssignableFrom(nodeObject.getClass())) {
                Competidor nodeObjectAsItemInbox = (Competidor) nodeObject;
                DefaultMutableTreeNode groupNode = (DefaultMutableTreeNode) node.getParent();
                Long groupId = nodeObjectAsItemInbox.getAlumno().getInstitucion().getId();
                if (!result.contains(groupId)) {
                    if (treeTable.isExpanded(new TreePath(groupNode.getPath())))
                        result.add(groupId);
                }
            }
        }
        
        return result;
    }
    
    public void expandGroups(JXTreeTable treeTable, List<Long> groupIds) {
        List<Long> idsToExpand = new ArrayList<Long>();
        idsToExpand.addAll(groupIds);
        
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getRoot();
        Enumeration nodes = rootNode.preorderEnumeration();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            Object nodeObject = node.getUserObject();
            if (nodeObject != null && Competidor.class.isAssignableFrom(nodeObject.getClass())) {
                Competidor nodeObjectAsItemInbox = (Competidor) nodeObject;
                DefaultMutableTreeNode groupNode = (DefaultMutableTreeNode) node.getParent();
                Long groupId = nodeObjectAsItemInbox.getAlumno().getInstitucion().getId();
                if (idsToExpand.contains(groupId)) {
                    treeTable.expandPath(new TreePath(groupNode.getPath()));
                    idsToExpand.remove(groupId);
                }
            }
        }
    }

}
