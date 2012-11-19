/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author fcambarieri
 */
public class DelegatingTreeTableModel<K, V> extends DefaultTreeTableModel {

    public interface CellValueProvider {

        Object getValueAt(Object node, int columnIndex);
    }

    public interface NodeCellValueProvicer<K, V> {

        TreeNode createTreeTableNode(DefaultMutableTreeNode rootNode, Map<K, V> treeList);
    }
    private String[] columnNames;
    private Class[] columnClasses;
    private Map<K, List<V>> treeList;
    private NodeCellValueProvicer<K, List<V>> nodeCellValueProvicer;
    private CellValueProvider cellValueProvider;
    private boolean debug = false;

    /**
     *  Constructores
     */
    public DelegatingTreeTableModel() {
        this(null);
    }

    public DelegatingTreeTableModel(TreeNode treeNode) {
        this(treeNode, true);
    }

    public DelegatingTreeTableModel(TreeNode treeNode, boolean allowedsChildren) {
        super(treeNode, allowedsChildren);
        treeList = new HashMap<K, List<V>>();
        installDependences();
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

    public Map<K, List<V>> getTreeList() {
        return treeList;
    }

    public void setTreeList(Map<K, List<V>> treeList) {
        this.treeList = treeList;
    }

    public CellValueProvider getCellValueProvider() {
        return cellValueProvider;
    }

    public void setCellValueProvider(CellValueProvider cellValueProvider) {
        this.cellValueProvider = cellValueProvider;
    }

    public NodeCellValueProvicer<K, List<V>> getNodeCellValueProvicer() {
        return nodeCellValueProvicer;
    }

    public void setNodeCellValueProvicer(NodeCellValueProvicer<K, List<V>> nodeCellValueProvicer) {
        this.nodeCellValueProvicer = nodeCellValueProvicer;
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
    public Object getValueAt(Object node, int columnIndex) {
        return cellValueProvider.getValueAt(node, columnIndex);
    }

    private void installDependences() {
        setNodeCellValueProvicer(new NodeCellValueProvicer<K, List<V>>() {

            public TreeNode createTreeTableNode(DefaultMutableTreeNode rootNode, Map<K, List<V>> treeList) {
                for (Iterator<K> it = treeList.keySet().iterator(); it.hasNext();) {

                    K key = it.next();

                    DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(key);

                    info("add Key Node: " + key.toString());

                    for (V value : treeList.get(key)) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(value);
                        groupNode.add(node);

                        info("add Child: :" + value.toString() + "  to Key Node: " + key.toString());
                    }

                    rootNode.add(groupNode);
                }
                return rootNode;
            }
        });
    }

    private DefaultMutableTreeNode findUserObject(Object value) {
        if (value != null) {
            Enumeration<DefaultMutableTreeNode> childrens = ((DefaultMutableTreeNode) getRoot()).breadthFirstEnumeration();
            DefaultMutableTreeNode node = null;
            while (childrens.hasMoreElements()) {
                node = childrens.nextElement();
                Object userObject = node.getUserObject();
                if (userObject != null && userObject.equals(value)) {
                    return node;
                }
            }
        }

        return null;
    }

    public void fireBuildTreeTableModel() {
        info("firing Tree table model");
        
        TreeNode rootNode = nodeCellValueProvicer.createTreeTableNode(new DefaultMutableTreeNode(), getTreeList());
        setRoot(rootNode);
    }

    public void fireInsertNode(K userObject) {
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(userObject);
        for (V value : treeList.get(userObject)) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(value);
            groupNode.add(child);
        }
        ((DefaultMutableTreeNode) getRoot()).add(groupNode);
        //fireTreeNodesInserted(groupNode, groupNode.getPath(), getChildrensIndex(groupNode), getChildrens(groupNode));
        reload();
    }

    public void fireInsertChildToNode(K userObject, V child) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        DefaultMutableTreeNode groupNode = findUserObject(userObject);
        if (groupNode == null) {
            groupNode = new DefaultMutableTreeNode(userObject);
            ((DefaultMutableTreeNode) getRoot()).add(groupNode);
        }
        groupNode.add(childNode);

        info("Add child: "+ child.toString() + " to Key node:" +userObject.toString());
        
        reload(groupNode);
    }

    public void fireDeleteNode(K userObject) {
        DefaultMutableTreeNode groupNode = findUserObject(userObject);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getRoot();
        if (groupNode != null) {
            if (groupNode.getParent() != null && !groupNode.getParent().equals(getRoot())) {
                parent = (DefaultMutableTreeNode) groupNode.getParent();
            }
            parent.remove(groupNode);
            
            info("Delete Node: "+ userObject.toString());
            
            reload(parent);
        }

    }

    public DefaultMutableTreeNode[] getChildrens(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode[] childrens = new DefaultMutableTreeNode[node.getChildCount()];
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) node.getFirstChild();
        int i = 0;
        while (n != null && i < childrens.length) {
            childrens[i] = n;
            n = n.getNextNode();
            i++;
        }
        return childrens;
    }

    public int[] getChildrensIndex(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode[] childrens = getChildrens(node);
        int indexs[] = new int[childrens.length];
        for (int i = 0; i < indexs.length; i++) {
            indexs[i] = node.getIndex(childrens[i]);
        }

        return indexs;
    }
    
    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    private void info(String title) {
        if (debug) {
            System.out.println(title);
        }
    }
}
