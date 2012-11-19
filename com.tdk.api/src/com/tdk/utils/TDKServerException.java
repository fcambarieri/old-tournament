/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tdk.utils;

/**
 *
 * @author fernando
 */
public class TDKServerException extends RuntimeException {
    
    public TDKServerException(String msg) {
        super(msg);
    }
    
    public TDKServerException() {
        super();
    }
    
    public TDKServerException(String message, Throwable cause) {
        super(message, cause);
    }
    public TDKServerException(Throwable cause) {
        super(cause);
    }
    
}
