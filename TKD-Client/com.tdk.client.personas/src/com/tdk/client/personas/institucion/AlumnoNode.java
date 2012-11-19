/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.client.personas.institucion;

import com.tdk.domain.Alumno;
import com.thorplatform.swing.SwingNode;
import org.openide.nodes.Children;

/**
 *
 * @author fernando
 */
public class AlumnoNode extends SwingNode<Alumno>{

    public AlumnoNode(Alumno alumno) {
        super(Children.LEAF, alumno);
        setIconBaseWithExtension("com/tdk/client/personas/institucion/alumnos-16x16.png");
    }
    
    @Override
    public String getDisplay() {
        return getValue().getPersonaFisica().getDisplayName();
    }

}
