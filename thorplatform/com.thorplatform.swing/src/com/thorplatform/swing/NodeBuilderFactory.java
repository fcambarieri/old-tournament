/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

/**
 * Está clase tiene un solo mètodo que devuelve las factorys correcpondientes
 * según la clase que se pase como parámetro. El paramentro tiene ser la clase
 * por el cual se registro la FactoryNode.
 * 
 * 
 * @author fernando
 */
public interface NodeBuilderFactory {
    
    /**
     *  El mètodo devuelve la factory para la clase que se esta buscando un
     *  nodo.
     *  @param Class del objeto que se busca un nodo
     *  @return NodeFactory retorna una instancia de la factory del nodo que se 
     *  registro para ese objeto en particular.
     */
     NodeFactory  createNodeFactory(Class className);
}
