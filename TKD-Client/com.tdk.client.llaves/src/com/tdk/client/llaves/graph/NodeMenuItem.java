/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.llaves.graph;

import com.tdk.domain.torneo.competencia.Competencia;
import javax.swing.Action;

/**
 *
 * @author fernando
 */
public interface NodeMenuItem extends Action{

    void setCompetencia(Competencia c);
}
