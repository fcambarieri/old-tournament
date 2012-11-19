/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.nodes;

/**
 *
 * @author fernando
 */
public abstract class AbstractBinaryNode implements Cloneable , BNode{

    private Object userObject;
    protected AbstractBinaryNode leftNode;
    protected AbstractBinaryNode rightNode;
    protected AbstractBinaryNode parent;
    private Long number;
    
    public AbstractBinaryNode() {
        this(null);
    }
    
    protected AbstractBinaryNode(Object userObj) {
        userObject = userObj;
    }
    
    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }
    
    public boolean isRoot() {
        return getParent() == null;
    }

    public BNode getLeftNode() {
        return leftNode;
    }

    public BNode getRightNode() {
        return rightNode;
    }

    public BNode getParent() {
        return parent;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public String toString() {
        return "(" + getNumber() + ") " + " Sin info ";
    }
    
    public void setLeftNode(AbstractBinaryNode node) {
        if (leftNode != null)
            deleteNode(leftNode);
        
        this.leftNode = node;
        if (this.leftNode != null)
            this.leftNode.setParent(this);
        
    }
    
    public void setRightNode(AbstractBinaryNode node) {
        if (rightNode != null)
            deleteNode(rightNode);
        
        this.rightNode = node;
        if (this.rightNode != null)
            this.rightNode.setParent(this);
    }

    public void setParent(AbstractBinaryNode parent) {
        this.parent = parent;
    }

    private void deleteNode(BNode node) {
        if (node != null) {
            deleteNode(node.getLeftNode());
            deleteNode(node.getRightNode());
            node = null;
        }
    }
    
    public int level() {
        if (parent == null)
            return 1;
        return getLevel(parent);
    }
    
    private int getLevel(BNode parent) {
        if (parent == null)
            return 0;
        return getLevel(parent.getParent()) + 1;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int compareTo(Object arg0) {
        if (arg0 == null)
            throw new IllegalArgumentException("The object can not be null.");
        if (arg0 instanceof AbstractBinaryNode)
            throw new ClassCastException("This object can not be compare.");
         
        AbstractBinaryNode n = (AbstractBinaryNode) arg0;
        return this.getNumber().compareTo(n.getNumber());
        
    }

    
}