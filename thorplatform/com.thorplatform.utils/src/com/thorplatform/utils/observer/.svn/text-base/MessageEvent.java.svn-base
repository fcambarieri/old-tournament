/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thorplatform.utils.observer;

import java.io.Serializable;

/**
 *
 * @author Fernando
 */
public class MessageEvent implements Serializable {

    public static final Integer INSERT = new Integer(1);
    public static final Integer DELETE = new Integer(2);
    public static final Integer UPDATE = new Integer(3);
    
    private Integer event;
    
    public MessageEvent(Integer event) {
        if (event == null)
            throw new IllegalArgumentException("The event is null.");
        if (event < 0)
            throw new IllegalArgumentException("The event can not be negative.");
        
        setEvent(event);
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }
}
