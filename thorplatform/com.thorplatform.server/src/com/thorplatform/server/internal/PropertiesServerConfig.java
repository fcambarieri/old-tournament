package com.thorplatform.server.internal;

import com.thorplatform.server.ServerConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesServerConfig implements ServerConfig {

    private File configFile;
    private Properties properties;
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public String get(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public void loadFileProperties(String filePropertiesPath) {
        this.configFile = new File(filePropertiesPath);
        if (!this.configFile.exists()) {
            this.configFile = new File("server.properties");
        }
        log.debug("\nCONFIGURACIÓN SERVER");
        log.debug("Configurando con archivo: " + this.configFile.getAbsolutePath());

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
        log.debug("Properties:");
        Iterator keys = this.properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = this.properties.getProperty(key);
            log.debug("    " + key + " : " + property);
        }
        log.debug("\n");
    }
}
