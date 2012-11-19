/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

/**
 * Está interface se encarga de tener todos los métodos necesarios para fabricar
 * un nodo.
 * 
 * @author fernando
 */
public interface NodeFactory<T> {

    /**
     * Crea una instancia del node que fabrique 
     *  @param T es el objeto que contiene el nodo
     *  @return SwingNode<T> devuelve un SwingNode
     */    
    SwingNode<T> createNode(T o);
    
    /**
     * Crea un ChoiceRootNode para obtener los nodos del tipo SwingNode<T>
     * @return ChoiceRootNode
     */
    ChoiceRootNode createRootNode();
    
    /**
     * Crea un ChoiceRootNode para obtener los nodos del tipo SwingNode<T>
     * @param strategySearch es una clase para tederminar la búsqueda
     * @return ChoiceRootNode
     */
    ChoiceRootNode createRootNode(StrategySearchPattern<? extends T> strategySearch);
    
    /**
     *  Este mètodo devuelve el objeto que contiene el nodo
     *  @param node que contiene el objeto a buscar
     *  @return T un objeto contenido. Si el SwingNode que se evalua no es el mismo
     *  tipo que se contiene los objetos T devuelve nulo.
     */
    T getObject(SwingNode node);
    
    /**
     * Retorna la clase del nodo que fabrica esta Factory
     * @return Class del nodo.
     */
    Class getNodeSelectedClass();
    
    
    /**
     * Retorna la clase de la clase entididad para la cual se fabrican los nodos
     * @return Class de la clase que contiene el nodo.
     */
    Class getRegisterClass();
}
