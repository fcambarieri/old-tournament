package com.thorplatform.swing;

import com.thorplatform.notifier.NotifierEvent;
import com.thorplatform.notifier.NotifierListener;
import com.thorplatform.notifier.NotifierObject;
import com.thorplatform.notifier.client.ClientNotyfier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Fernando
 */
public abstract class ChoiceRootNodeChildren<T> extends Children.Keys implements LookupListener, NotifierListener {

    protected List<T> keys;
    private NodeNotifier notifier;
    private Lookup.Result result;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private ClientNotyfier clientNotyfier = Lookup.getDefault().lookup(ClientNotyfier.class);

    public ChoiceRootNodeChildren() {
        installLookup();
    }

    @SuppressWarnings("unchecked")
    public void setKeys(List keys) {
        logger.info("Setting news Keys");
        super.setKeys(keys);
        this.keys = keys;
    }

    @Override
    protected void removeNotify() {
        logger.info("Removing keys");
        setKeys(Collections.EMPTY_LIST);
    }

    public void addKey(T key) {
        if (keys == null) {
            setKeys(new ArrayList<T>());
        }

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

    public void resultChanged(LookupEvent le) {
        logger.info("Starting resultChange");
        for(Object t : this.result.allInstances()) {
            logger.info(String.format("Key: %s", t.toString()));
            if (associateLookup() != null) {
                addKey((T)t);    
                logger.info("Adding new Key");
            }
        }
    }

    public Class associateLookup() {
        return null;
    }

    private void installLookup() {
        if (associateLookup() != null) {
            
            clientNotyfier.addNotifierListener(associateLookup(), this);
            
            
            logger.log(Level.INFO, "Installing lookup to {0}", associateLookup().getSimpleName());
//            notifier = Lookup.getDefault().lookup(NodeNotifier.class);
//            result = notifier.findLookup(associateLookup());
//            result.addLookupListener(this);
        }
    }
    
    @Override
    public void onNotify(NotifierObject paramObject) throws Exception {
        logger.log(Level.INFO, "Starting onNotify {0}", paramObject);
        if (NotifierEvent.ADD.equals(paramObject.getEvent())) {
            addKey((T)paramObject.getObject());
        } else if (NotifierEvent.DELETE.equals(paramObject.getEvent())) {
            removeKey((T)paramObject.getObject());
        } else if (NotifierEvent.UDPDATE.equals(paramObject.getEvent())) {
            
        }
    }
    
    
}
