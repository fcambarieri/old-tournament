/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.test;

import com.thorplatform.swing.SwingControllerFactory;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.Lookup;

/**
 *
 * @author Fernando
 */
public class SwingMultiChoiceFieldActionTest extends AbstractAction {

    public SwingMultiChoiceFieldActionTest() {
        super("Multi choice field test");
    }
    
    public void actionPerformed(ActionEvent e) {
        SwingControllerFactory scf = Lookup.getDefault().lookup(SwingControllerFactory.class);
        TestNodesController controller = scf.createController(TestNodesController.class);
        controller.showModal();
    }

}
