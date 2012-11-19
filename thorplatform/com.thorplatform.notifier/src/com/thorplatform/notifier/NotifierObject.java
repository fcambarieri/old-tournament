/*    */ package com.thorplatform.notifier;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class NotifierObject
/*    */   implements Serializable
/*    */ {
/*    */   private String className;
/*    */   private NotifierEvent event;
/*    */   private Object object;
/*    */ 
/*    */   public NotifierObject()
/*    */   {
/* 20 */     this(null, null, null);
/*    */   }
/*    */ 
/*    */   public NotifierObject(NotifierEvent event, Class className, Object object) {
/* 24 */     setEvent(event);
/* 25 */     setObject(object);
/* 26 */     setClassName(className);
/*    */   }
/*    */ 
/*    */   public NotifierEvent getEvent()
/*    */   {
/* 31 */     return this.event;
/*    */   }
/*    */ 
/*    */   public void setEvent(NotifierEvent event) {
/* 35 */     this.event = event;
/*    */   }
/*    */ 
/*    */   public Object getObject() {
/* 39 */     return this.object;
/*    */   }
/*    */ 
/*    */   public void setObject(Object object) {
/* 43 */     this.object = object;
/*    */   }
/*    */ 
/*    */   public String getClassName() {
/* 47 */     return this.className;
/*    */   }
/*    */ 
/*    */   public void setClassName(Class className) {
/* 51 */     this.className = className.getName();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object arg0)
/*    */   {
/* 57 */     if (!(arg0 instanceof NotifierObject)) {
/* 58 */       return false;
/*    */     }
/* 60 */     NotifierObject other = (NotifierObject)arg0;
/*    */ 
/* 63 */     if (((getClassName() == null) && (other.getClassName() != null)) || ((getClassName() != null) && (!getClassName().equals(other.getClassName()))))
/*    */     {
/* 65 */       return false;
/*    */     }
/* 67 */     if ((this.object != null) && (other.getObject() != null)) {
/* 68 */       return this.object.equals(other.getObject());
/*    */     }
/*    */ 
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 76 */     int hash = 5;
/* 77 */     hash = 59 * hash + (this.className != null ? this.className.hashCode() : 0);
/* 78 */     hash = 59 * hash + (this.event != null ? this.event.hashCode() : 0);
/* 79 */     hash = 59 * hash + (this.object != null ? this.object.hashCode() : 0);
/* 80 */     return hash;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-notifier.jar
 * Qualified Name:     com.thorplatform.notifier.NotifierObject
 * JD-Core Version:    0.6.0
 */