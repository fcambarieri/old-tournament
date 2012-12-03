 package com.thorplatform.login;
 
 import java.io.Serializable;
 
 /**
  
  */
 public class WorkStation  implements Serializable
 {
   private String ip;
   private String name;
   private String os;
   private String osVersion;
   private String workStationUser;
 
   public String getIp()
   {
     return this.ip;
   }
 
   public void setIp(String ip) {
     this.ip = ip;
   }
 
   public String getName() {
     return this.name;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public String getOS() {
     return this.os;
   }
 
   public void setOS(String os) {
     this.os = os;
   }
 
   public String getOSVersion() {
     return this.osVersion;
   }
 
   public void setOSVersion(String osVersion) {
     this.osVersion = osVersion;
   }
 
   public String getWorkStationUserName() {
     return this.workStationUser;
   }
 
   public void setWorkStationUserName(String workStationUser) {
     this.workStationUser = workStationUser;
   }
 }

