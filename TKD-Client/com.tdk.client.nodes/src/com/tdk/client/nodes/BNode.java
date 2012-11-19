/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.nodes;

/**
 *
 * @author fernando
 */
public interface BNode extends Comparable {
    
    BNode getParent();

    BNode getLeftNode();
    
    BNode getRightNode();
    
    boolean isLeaf();
    
    boolean isRoot();
    
    Object getUserObject();
    
    int level();
    
    String getDisplay();
    
}
