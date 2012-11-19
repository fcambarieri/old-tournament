/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.nodes;

import java.util.List;

/**
 *
 * @author fernando
 */
public class BinaryTree<T> extends AbstractBinaryTreeModel {

    public BinaryTree() {
        setBinaryNodeFactory(new BinaryNodeFactory<T>() {

            public BNode createBNode(T value) {
                return new BinaryNode<T>(value);
            }
        });
    }

    
    
    public void convertListToBinaryTree(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("La lista es nula");
        }
        if (root == null) {
            root = createBNode(null);
        }

        if (!list.isEmpty()) {
            root = insertNodeFromList(list,(BinaryNode<T>) root);
        }
        
        fireStructChange();
    }

    private BinaryNode<T> insertNodeFromList(List<T> list, BinaryNode<T> dad) {
        if (dad == null) {
            dad = (BinaryNode<T>) createBNode(null);
            if (list.size() == 1) {
                dad.setUserObject(list.get(0));
                return dad;
            }

        }

        if (list.size() == 1) {
            dad.setLeftNode(createBNode(list.get(0)));
        } else if (list.size() == 2) {
            dad.setLeftNode(createBNode(list.get(0)));
            dad.setRightNode(createBNode(list.get(1)));
        } else {
            int x1 = list.size() / 2;
            int x2 = list.size();

            dad.setLeftNode(insertNodeFromList(list.subList(0, x1), dad.getLeftNode()));
            dad.setRightNode(insertNodeFromList(list.subList(x1, x2), dad.getRightNode()));
        }

        return dad;
    }

    @Override
    public BinaryNode<T> findNodeFromUserObject(Object value) {
        return (BinaryNode<T>) super.findNodeFromUserObject(value);
    }
    
    
    @Override
    protected AbstractBinaryNode createBNode(Object o) {
        return (AbstractBinaryNode) getBinaryNodeFactory().createBNode(o);
    }

}
