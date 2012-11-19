/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing;

/**
 *
 * @author fernando
 */
public interface ValidatorFilter {
    /**
     *  Este método es usado para validar si el textfield de un @see ChoiceRootNode
     *  es valido y genera la acción para desplazar los nodos.
     *  @param text texto a validar
     *  @return boolean retorna si es valido
     */
    boolean validate(String text);
}
