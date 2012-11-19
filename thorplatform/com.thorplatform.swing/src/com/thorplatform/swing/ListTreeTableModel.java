/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author fernando
 */
public class ListTreeTableModel<T> extends DefaultTreeTableModel {

    public String[] getColumnMethodNames() {
        return columnMethodNames;
    }

    public void setColumnMethodNames(String[] columnMethodNames) {
        this.columnMethodNames = columnMethodNames;
    }

    public interface CellValueProvider {

        Object getValueAt(Object node, int column);
    }

    public interface TreeNodeValueProvider<T> {

        DefaultMutableTreeNode createModel(DefaultMutableTreeNode rootNode, List<T> treeList);

        List createChilds(T treeListValu);
    }
    
    
    private List<T> treeList;
    private String columnNames[];
    private Class columnClasses[];
    private String columnMethodNames[];
    private DefaultMutableTreeNode treeRootNode;
    private CellValueProvider cellValueProvider;
    private TreeNodeValueProvider<T> treeNodeValueProvider;

    public ListTreeTableModel() {
        this(new DefaultMutableTreeNode(), new ArrayList<T>());
    }

    public ListTreeTableModel(DefaultMutableTreeNode treeRootNode, List<T> list) {
        this.treeRootNode = treeRootNode;
        setRoot(treeRootNode);
        this.treeList = list;
        
        installDefaultDependencies();
    }

    @Override
    public int getColumnCount() {
        return getColumnNames().length;
    }

    @Override
    public Class getColumnClass(int column) {
        return getColumnClasses()[column];
    }

    @Override
    public String getColumnName(int column) {
        return getColumnNames()[column];
    }

    public List<T> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<T> treeList) {
        this.treeList = treeList;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public Class[] getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(Class[] columnClasses) {
        this.columnClasses = columnClasses;
    }

    public DefaultMutableTreeNode getTreeRootNode() {
        return treeRootNode;
    }

    public void setTreeRootNode(DefaultMutableTreeNode treeRootNode) {
        this.treeRootNode = treeRootNode;
    }

    public CellValueProvider getCellValueProvider() {
        return cellValueProvider;
    }

    public void setCellValueProvider(CellValueProvider cellValueProvider) {
        this.cellValueProvider = cellValueProvider;
    }

    public TreeNodeValueProvider<T> getTreeNodeValueProvider() {
        return treeNodeValueProvider;
    }

    public void setTreeNodeValueProvider(TreeNodeValueProvider<T> treeNodeValueProvider) {
        this.treeNodeValueProvider = treeNodeValueProvider;
    }

    @Override
    public Object getValueAt(Object node, int column) {
        return getCellValueProvider().getValueAt(node, column);
    }

    public void assignTreeList(List<T> treeList) {
        if (treeList == null) {
            throw new NullPointerException("No se puede agregar una lista nula");
        }
        if (treeNodeValueProvider == null) {
            throw new NullPointerException("El TreeNodeValuProvider es nulo");
        }
        setTreeList(treeList);
        treeRootNode = new DefaultMutableTreeNode();
        buildTreeModel();
        setRoot(treeRootNode);
    }

    private void buildTreeModel() {
        Collections.sort(treeList, new Comparator<T>() {

            public int compare(T o1, T o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });

        for (int i = 0; i < treeList.size(); i++) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode();
            groupNode.setUserObject(treeList.get(i));

            List childs = treeNodeValueProvider.createChilds(treeList.get(i));
            for (Object e : childs) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                node.setUserObject(e);
                groupNode.add(node);
            }

            getTreeRootNode().add(groupNode);
        }
    }

    public void add(T object) {
        treeList.add(object);

        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode();
        groupNode.setUserObject(object);

        for (Object e : treeNodeValueProvider.createChilds(object)) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            node.setUserObject(e);
            groupNode.add(node);
        }

        getTreeRootNode().add(groupNode);
    }

    public void remove(T object) {
        treeList.remove(object);
        getTreeRootNode().remove(findNodeWithUserObject(object));

    }

    public void remove(int index) {
        treeList.remove(index);
        getTreeRootNode().remove(index);
    }

    public DefaultMutableTreeNode findNodeWithUserObject(T userObject) {
        DefaultMutableTreeNode result = null;

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getRoot();
        Enumeration nodes = rootNode.preorderEnumeration();
        boolean found = false;

        while (!found && nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            Object nodeObject = node.getUserObject();
            if (nodeObject != null && userObject.getClass().isAssignableFrom(nodeObject.getClass())) {
                T nodeObjectAsItemInbox = (T) nodeObject;
                if (nodeObjectAsItemInbox.equals(userObject)) {
                    result = node;
                    found = true;
                }
            }
        }

        return result;
    }
    
    protected Object formatValue(int columnIndex, Object value) {
        return value;
    }

    private void installDefaultDependencies() {
        /*setCellValueProvider(new CellValueProvider() {
            public Object getValueAt(Object node, int columnIndex) {
                Object result = null;
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
                
                if (columnIndex == 0)
                    return node;
                
                
                T item = (T) ((DefaultMutableTreeNode)node).getUserObject();
                
                String[] methods = columnMethodNames[columnIndex].split("\\.");
                try {
                    Method method = null; Object obj = item;
                    for (int i = 0; i < methods.length - 1; i++) {
                        method = obj.getClass().getMethod(methods[i], (Class[]) null);
                        obj = method.invoke(obj, (Object[]) null);
                    }
                    method = obj.getClass().getMethod(methods[methods.length - 1], (Class[]) null);
                    result = formatValue(columnIndex, method.invoke(obj, (Object[]) null));
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
                return result;
            }
            
        });*/
        setTreeNodeValueProvider(new TreeNodeValueProvider<T>() {

            public DefaultMutableTreeNode createModel(DefaultMutableTreeNode rootNode, List<T> treeList) {
                buildTreeModel();
                return rootNode;
            }

            public List createChilds(T treeListValu) {
                return new ArrayList();
            }
        });
        
    }
}
