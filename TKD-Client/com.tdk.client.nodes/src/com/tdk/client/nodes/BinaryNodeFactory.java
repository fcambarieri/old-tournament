/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.nodes;

/**
 *
 * @author fernando
 */
public interface BinaryNodeFactory<T> {

    /*
     * Este mètodo se utiliza para crear instancias 
     * que implementen BNode.
     * @param T value que ira a tomar el BNode
     * @return BNode que encapsula el valor T
     * 
     */
    BNode createBNode(T value);
}
