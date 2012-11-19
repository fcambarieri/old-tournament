/*    */ package com.thorplatform.login;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class EstacionDeTrabajo
/*    */   implements Serializable
/*    */ {
/*    */   private String ip;
/*    */   private String name;
/*    */   private String os;
/*    */   private String osVersion;
/*    */   private String workStationUser;
/*    */ 
/*    */   public String getIp()
/*    */   {
/* 23 */     return this.ip;
/*    */   }
/*    */ 
/*    */   public void setIp(String ip) {
/* 27 */     this.ip = ip;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 35 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getOS() {
/* 39 */     return this.os;
/*    */   }
/*    */ 
/*    */   public void setOS(String os) {
/* 43 */     this.os = os;
/*    */   }
/*    */ 
/*    */   public String getOSVersion() {
/* 47 */     return this.osVersion;
/*    */   }
/*    */ 
/*    */   public void setOSVersion(String osVersion) {
/* 51 */     this.osVersion = osVersion;
/*    */   }
/*    */ 
/*    */   public String getWorkStationUserName() {
/* 55 */     return this.workStationUser;
/*    */   }
/*    */ 
/*    */   public void setWorkStationUserName(String workStationUser) {
/* 59 */     this.workStationUser = workStationUser;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-login.jar
 * Qualified Name:     com.thorplatform.login.EstacionDeTrabajo
 * JD-Core Version:    0.6.0
 */