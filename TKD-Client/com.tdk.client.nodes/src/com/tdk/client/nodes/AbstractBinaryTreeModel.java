/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author fernando
 */
public abstract class AbstractBinaryTreeModel implements BinaryTreeModel {

    private List<BinaryTreeModelListener> treeModelListeners = new ArrayList<BinaryTreeModelListener>();
    private BinaryNodeFactory binaryNodeFactory;
    protected AbstractBinaryNode root;

    public AbstractBinaryTreeModel() {
        this(null);
    }

    public AbstractBinaryTreeModel(BNode root) {
        setRoot(root);
    }

    public BNode getRoot() {
        return root;
    }

    public void setRoot(BNode root) {
        if (this.root != null) {
            removeNode(root);
        }

        this.root = (AbstractBinaryNode) root;

        fireStructChange();
    }

    @SuppressWarnings("unchecked")
    public void addNode(Object obj) {
        BNode node = null;
        if (root == null) {
            root = createBNode(obj);
            node = root;
        } else {
            node = insertNode((AbstractBinaryNode) root,obj);
        }
        
        fireNodeInsert(node);
    }

    protected abstract AbstractBinaryNode createBNode(Object o);

    @SuppressWarnings("unchecked")
    protected BNode insertNode(AbstractBinaryNode node, Object object) {
        if (node.compareTo(object) < 0) {
            BNode l = node.getLeftNode();
            if (l == null) {
                node.setLeftNode(createBNode(object));
                return node.getLeftNode();
            } else {
                return insertNode(   (AbstractBinaryNode) l,object);
            }
        } else {
            BNode r = node.getRightNode();
            if (r == null) {
                node.setRightNode(createBNode(object));
                return node.getRightNode();
            } else {
                return insertNode((AbstractBinaryNode) r,object);
            }
        }
       
    }

    public void removeNode(BNode node) {
        if (node == null) {
            throw new IllegalArgumentException("El nodo es nulo.");
        }
        removeNode(node, root);
    }

    private void removeNode(BNode nodeToRemove, BNode node) {
        if (node == null || nodeToRemove == null) {
            return;
        }
        if (nodeToRemove.equals(node)) {
            removeNode(nodeToRemove.getLeftNode(), nodeToRemove.getLeftNode());
            removeNode(nodeToRemove.getRightNode(), nodeToRemove.getRightNode());
            fireNodeDelete(nodeToRemove);
            nodeToRemove = null;
        } else {
            removeNode(nodeToRemove, node.getLeftNode());
            removeNode(nodeToRemove, node.getRightNode());
        }

    }

    public BNode findNodeFromUserObject(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("EL objeto buscado es nulo");
        }
        return findNodeFromUserObject(value, root);

    }

    public BNode findNodeFromUserObject(Object value, BNode node) {
        if (node == null) {
            return null;
        }
        if (node != null && value.equals(node.getUserObject())) {
            return node;
        }
        BNode lresult = findNodeFromUserObject(value, node.getLeftNode());
        BNode rresult = findNodeFromUserObject(value, node.getRightNode());
        if (lresult != null) {
            return lresult;
        } else {
            return rresult;
        }
    }

    public int size() {
        return size(root);
    }

    public int size(BNode node) {
        if (node == null) {
            return 0;
        } else {
            return size(node.getLeftNode()) + 1 + size(node.getRightNode());
        }
    }

    public void addBinaryTreeModelListener(BinaryTreeModelListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("El listener no puede ser nulo.");
        }
        treeModelListeners.add(listener);
    }

    public void removeBinaryTreeModelListener(BinaryTreeModelListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("El listener no puede ser nulo.");
        }
        treeModelListeners.remove(listener);
    }

    public void fireNodeInsert(final BNode node) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < treeModelListeners.size(); i++) {
                    treeModelListeners.get(i).fireNodeInsert(node);
                }
            }
        });
    }

    public void fireNodeDelete(final BNode node) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < treeModelListeners.size(); i++) {
                    treeModelListeners.get(i).fireNodeDelete(node);
                }
            }
        });
    }
    
    public void fireNodeChange(final BNode node) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < treeModelListeners.size(); i++) {
                    treeModelListeners.get(i).fireNodeChange(node);
                }
            }
        });
    }

    public void fireStructChange() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < treeModelListeners.size(); i++) {
                    treeModelListeners.get(i).fireStructChange(root);
                }
            }
        });
    }

    public BinaryNodeFactory getBinaryNodeFactory() {
        return binaryNodeFactory;
    }

    public void setBinaryNodeFactory(BinaryNodeFactory binaryNodeFactory) {
        this.binaryNodeFactory = binaryNodeFactory;
    }
}
