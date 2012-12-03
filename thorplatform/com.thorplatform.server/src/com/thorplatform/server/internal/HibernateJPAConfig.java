/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thorplatform.server.internal;

import com.thorplatform.server.JPAConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author sabon
 */
public class HibernateJPAConfig implements JPAConfig {

    private File configFile;
    private Properties properties;
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public Map getProperties() {
        if (properties != null) {
            Map map = new HashMap();
            Iterator keys = this.properties.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String property = this.properties.getProperty(key);
                map.put(key, property);
            }
            return map;
        }
        return null;
    }

    @Override
    public String get(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public void loadFileProperties(String filePropertiesPath) {
        this.configFile = new File(filePropertiesPath);
        if (!this.configFile.exists()) {
            this.configFile = new File("hibernate.properties");
        }
        log.info("\nCONFIGURACIÓN Hibernate");
        log.info("Configurando con archivo: " + this.configFile.getAbsolutePath());

        this.properties = new Properties();
        try {
            InputStream is = new FileInputStream(this.configFile);
            this.properties.load(is);
            printProperties();
        } catch (FileNotFoundException ex) {
            log.error("Loading configFile", ex);
        } catch (IOException ex) {
            log.error("Loading configFile", ex);
        }
    }

    private void printProperties() {
        log.info("Properties:");
        Iterator keys = this.properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = this.properties.getProperty(key);
            log.info("    " + key + " : " + property);
        }
        log.info("\n");
    }
}
