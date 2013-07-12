/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.utils.internal;

import com.thorplatform.utils.ServerConnectionProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.openide.util.Exceptions;

/**
 *
 * @author sabon
 */
public class ServerConnectionPropertiesImpl implements ServerConnectionProperties {

    private final static Map<Key, String> serverConnectionProp = new HashMap<Key, String>();
    private boolean isLoad = false;
    private final static String DEFAUL_PATH = "conf/client.properties";

    public Map<Key, String> getProperties() {
        if (!isLoad) {
            loadProperties();
        }
        return serverConnectionProp;
    }

    private void loadProperties() {
        loadProperties(System.getProperty("config.properties"));
    }

    private void loadProperties(String configPath) {
        if (configPath == null || "".equals(configPath)) {
            configPath = DEFAUL_PATH;
        }

        File configFile = new File(configPath);
        if (!configFile.exists()) {
            configFile = new File(DEFAUL_PATH);
        }

        Properties p = new Properties();
        try {
            InputStream is = new FileInputStream(configFile);
            p.load(is);
            Iterator keys = p.keySet().iterator();

            while (keys.hasNext()) {
                String name = (String) keys.next();
                Key key = Key.valueOf(name.toUpperCase());
                serverConnectionProp.put(key, p.getProperty(key.name()));
            }
            printProperties();
            
            isLoad = true;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("No se pudo encontrar el archivo de configuración", ex);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo leer el archivo de configuración", ex);
        }
    }

    private void printProperties() {
    }

    public String getServerConnection() {
        Map<Key, String> prop = getProperties();

        if (prop.containsKey(Key.CONNECTION) && !"".equals(prop.get(Key.CONNECTION))) {
            return prop.get(Key.CONNECTION);
        }

        StringBuilder sb = new StringBuilder("//");
        sb.append(prop.get(Key.HOST));
        sb.append(":");
        sb.append(prop.get(Key.PORT));
        sb.append("/");
        sb.append(prop.get(Key.SERVICE_NAME));

        return sb.toString();

    }

    public String get(Key k) {
        return getProperties().get(k);
    }

    public void set(Key k, String value) {
        serverConnectionProp.put(k, value);
    }

    public void save() {
        Properties prop = new Properties();
        Iterator<Key> it = serverConnectionProp.keySet().iterator();
        while(it.hasNext()) {
            Key key = it.next();
            String value = serverConnectionProp.get(key);
            if (value == null) {
                value = "";
            }
            prop.put(key.name(), value);
        }
        
        try {
            prop.store(new FileOutputStream(new File(DEFAUL_PATH)), "Last update ".concat(getNow()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private String getNow() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return sf.format(Calendar.getInstance().getTime());
    }
}
