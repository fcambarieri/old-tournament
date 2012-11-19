/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.server.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author fernando
 */
public class CategoriaTest {

    private static final int MAX = 3;
    
    public CategoriaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void categoriaEdades() {
         Integer v[][] = new Integer[][]{{4,7},{9, 12}};
         int y = 5;
         int z = 8;
         
         boolean error = false;
         int i = 0;
         while(!error && i < v.length) {
             error = (v[i][0] <= y && v[i][1] <= y) ||
                     (y <= v[i][0] && z >= v[i][0]) ||
                     (y <= v[i][1] &&  z >= v[i][1]);
             i++;
         }
         
         assertFalse(error);
     }

}