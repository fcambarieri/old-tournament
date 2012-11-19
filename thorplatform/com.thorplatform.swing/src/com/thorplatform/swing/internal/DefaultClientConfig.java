/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.swing.internal;

import com.thorplatform.swing.ClientConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author fernando
 */
public class DefaultClientConfig implements ClientConfig {

    private Properties properties;

    public DefaultClientConfig() {
        if (!loadProperties("../conf/client.properties"));
            loadProperties("client.properties");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public boolean loadProperties(String path) {
        File configFile = new File(path);
        boolean exist = configFile.exists();
        if (exist) {
            System.out.println("\nCONFIGURACIÓN CLIENT");
            System.out.println("Configurando con archivo: " + configFile.getAbsolutePath());

            properties = new Properties();
            try {
                InputStream is = new FileInputStream(configFile);
                properties.load(is);
                printProperties();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                exist = false;
            } catch (IOException ex) {
                ex.printStackTrace();
                exist = false;
            }
        }
        return exist;
    }

    public void printProperties() {
        System.out.println("Properties:");
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println("    " + key + " : " + property);
        }
        System.out.println("");
    }
}
