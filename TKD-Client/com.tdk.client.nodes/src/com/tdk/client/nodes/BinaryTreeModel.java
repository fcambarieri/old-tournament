/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.nodes;

/**
 *
 * @author fernando
 */
public interface BinaryTreeModel {

    void addBinaryTreeModelListener(BinaryTreeModelListener listener);

    void removeBinaryTreeModelListener(BinaryTreeModelListener listener);

    void addNode(Object object);

    void removeNode(BNode node);

    BNode getRoot();

    int size();
}
