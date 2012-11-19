/*    */ package com.thorplatform.notifier.internal;
/*    */ 
/*    */ import com.thorplatform.notifier.PropertiesNotifer;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class PropertiesNotifierLoader
/*    */   implements PropertiesNotifer
/*    */ {
/*    */   private Properties properties;
/* 23 */   private String path = "../conf/";
/*    */ 
/*    */   public PropertiesNotifierLoader(String name) {
/* 26 */     File configFile = new File(this.path + name);
/* 27 */     if (!configFile.exists())
/* 28 */       configFile = new File(name);
/* 29 */     System.out.println("Configurando con: " + configFile.getAbsolutePath());
/*    */ 
/* 31 */     this.properties = new Properties();
/*    */     try {
/* 33 */       InputStream is = new FileInputStream(configFile);
/* 34 */       this.properties.load(is);
/* 35 */       is.close();
/*    */     } catch (FileNotFoundException ex) {
/* 37 */       ex.printStackTrace();
/*    */     } catch (IOException ex) {
/* 39 */       ex.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getProperty(String key) {
/* 44 */     return this.properties.getProperty(key);
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-notifier.jar
 * Qualified Name:     com.thorplatform.notifier.internal.PropertiesNotifierLoader
 * JD-Core Version:    0.6.0
 */