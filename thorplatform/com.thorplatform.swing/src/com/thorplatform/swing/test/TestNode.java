/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.swing.test;

import com.thorplatform.swing.SwingNode;

/**
 *
 * @author Fernando
 */
public class TestNode extends SwingNode<String>{

    public TestNode(String name) {
        super(name);
    }
    
    @Override
    public String getDisplay() {
        return getValue();
    }

}
