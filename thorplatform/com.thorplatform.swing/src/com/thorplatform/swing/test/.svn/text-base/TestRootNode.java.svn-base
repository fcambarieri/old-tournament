/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.test;

import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.ChoiceRootNodeChildren;
import com.thorplatform.swing.ValidatorFilter;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Node;

/**
 *
 * @author Fernando
 */
public class TestRootNode extends ChoiceRootNode {

    public TestRootNode() {
        super(new TestRootNodeChildren());
        setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String text) {
                return true;
            }
        });
    }
    
    @Override
    protected List loadKeys(String pattern) {
        List<String> lst = new ArrayList<String>();
        lst.add("Hola");
        lst.add("Mundo");
        lst.add("Como");
        lst.add("andas.");
        return lst;
    }

}

class TestRootNodeChildren extends ChoiceRootNodeChildren {

    @Override
    protected Node[] createNodes(Object arg0) {
        return new Node[]{new TestNode((String) arg0)};
    }
    
}
