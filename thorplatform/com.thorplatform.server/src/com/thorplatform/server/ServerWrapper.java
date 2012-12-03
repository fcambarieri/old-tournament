 package com.thorplatform.server;
 
 import org.openide.util.Lookup;
 import org.tanukisoftware.wrapper.WrapperListener;
 
 public class ServerWrapper
   implements WrapperListener
 {
   private ServerConfig serverConfig;
 
   public ServerWrapper()
   {
     this.serverConfig = ((ServerConfig)Lookup.getDefault().lookup(ServerConfig.class));
   }
 
   public Integer start(String[] arg0) {
     throw new UnsupportedOperationException("Not supported yet.");
   }
 
   public int stop(int arg0) {
     throw new UnsupportedOperationException("Not supported yet.");
   }
 
   public void controlEvent(int arg0) {
     throw new UnsupportedOperationException("Not supported yet.");
   }
 }

