package com.thorplatform.notifier;

import java.io.Serializable;

public class NotifierObject implements Serializable {

    private String className;
    private NotifierEvent event;
    private Object object;

    public NotifierObject() {
        this(null, null, null);
    }

    public NotifierObject(NotifierEvent event, Class className, Object object) {
        setEvent(event);
        setObject(object);
        setClassName(className);
    }

    public NotifierEvent getEvent() {
        return this.event;
    }

    public void setEvent(NotifierEvent event) {
        this.event = event;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(Class className) {
        this.className = className.getName();
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof NotifierObject)) {
            return false;
        }
        NotifierObject other = (NotifierObject) arg0;

        if (((getClassName() == null) && (other.getClassName() != null)) || ((getClassName() != null) && (!getClassName().equals(other.getClassName())))) {
            return false;
        }
        if ((this.object != null) && (other.getObject() != null)) {
            return this.object.equals(other.getObject());
        }

        return true;
    }

    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 59 * hash + (this.event != null ? this.event.hashCode() : 0);
        hash = 59 * hash + (this.object != null ? this.object.hashCode() : 0);
        return hash;
    }
}
