 package com.thorplatform.notifier.internal;
 
 import com.thorplatform.notifier.PropertiesNotifer;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.PrintStream;
 import java.util.Properties;
 
 public class PropertiesNotifierLoader
   implements PropertiesNotifer
 {
   private Properties properties;
   private String path = "../conf/";
 
   public PropertiesNotifierLoader(String name) {
     File configFile = new File(this.path + name);
     if (!configFile.exists())
       configFile = new File(name);
     System.out.println("Configurando con: " + configFile.getAbsolutePath());
 
     this.properties = new Properties();
     try {
       InputStream is = new FileInputStream(configFile);
       this.properties.load(is);
       is.close();
     } catch (FileNotFoundException ex) {
       ex.printStackTrace();
     } catch (IOException ex) {
       ex.printStackTrace();
     }
   }
 
   public String getProperty(String key) {
     return this.properties.getProperty(key);
   }
 }

