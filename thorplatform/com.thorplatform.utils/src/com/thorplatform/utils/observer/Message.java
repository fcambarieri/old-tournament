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
public class Message implements Serializable {

    private MessageEvent messageEvent;
    private Class className;
    private Object object;

    public Message(Class className, Object o, MessageEvent event) {
        this.className = className;
        this.object = o;
        this.messageEvent = event;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public void setMessageEvent(MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }
}
