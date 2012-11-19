package com.thorplatform.jpa;

import java.util.HashMap;
import java.util.Map;

public class JPAContext {

    private static JPAContext instance = new JPAContext();
    private ThreadProperties threadProperties;

    private JPAContext() {
        this.threadProperties = new ThreadProperties();
    }

    public static JPAContext getInstance() {
        return instance;
    }

    public void setProperty(String name, Object value) {
        ((Map) this.threadProperties.get()).put(name, value);
    }

    public Object getProperty(String name) {
        return ((Map) this.threadProperties.get()).get(name);
    }

    private class ThreadProperties extends ThreadLocal<Map<String, Object>> {

        private ThreadProperties() {
        }

        protected Map<String, Object> initialValue() {
            return new HashMap();
        }
    }
}
