/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.nodes;

/**
 *
 * @author fernando
 */
public interface BinaryTreeModelListener {

   void fireNodeChange(BNode node);
   
   void fireNodeInsert(BNode node);
   
   void fireNodeDelete(BNode node);
   
   void fireStructChange(BNode node);
}
