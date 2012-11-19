package com.thorplatform.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Fernando
 */
public abstract class ChoiceRootNodeChildren<T> extends Children.Keys {

    protected List<T> keys;

    @SuppressWarnings("unchecked")
    public void setKeys(List keys) {
        super.setKeys(keys);
        this.keys = keys;
    }

    @Override
    protected void removeNotify() {
        setKeys(Collections.EMPTY_LIST);
    }

    public void addKey(T key) {
        if (keys == null) setKeys(new ArrayList<T>());

        synchronized (keys) {
            if (!keys.contains(key)) {
                keys.add(key);
                setKeys(keys);
            }
        }
    }

    public void removeKey(T key) {
        if (keys != null) {
            synchronized (keys) {
                if (keys.contains(key)) {
                    keys.remove(key);
                    setKeys(keys);
                }
            }
        }
    }
}
