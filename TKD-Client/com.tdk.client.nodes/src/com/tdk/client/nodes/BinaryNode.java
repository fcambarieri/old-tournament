/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.nodes;

/**
 *
 * @author fernando
 */
public class BinaryNode<T> extends AbstractBinaryNode {

    public BinaryNode() {
        super();
    }

    public BinaryNode(T userObject) {
        super(userObject);
    }

    public String getDisplay() {
        return "(" + getNumber() + ")" + (getUserObject() != null ? getUserObject().toString() : "Sin info");
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getUserObject() {
        return (T) super.getUserObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BinaryNode<T> getLeftNode() {
        return (BinaryNode<T>) super.getLeftNode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BinaryNode<T> getRightNode() {
        return (BinaryNode<T>) super.getRightNode();
    }
}
