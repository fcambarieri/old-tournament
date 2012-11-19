/*    */ package com.thorplatform.notifier;
/*    */ 
/*    */ public enum NotifierEvent
/*    */ {
/* 14 */   ADD("Agregar"), 
/* 15 */   UDPDATE("Modificar"), 
/* 16 */   DELETE("Eliminar"), 
/* 17 */   ALL("Todos");
/*    */ 
/*    */   private String name;
/*    */ 
/* 22 */   private NotifierEvent(String name) { this.name = name;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 27 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-notifier.jar
 * Qualified Name:     com.thorplatform.notifier.NotifierEvent
 * JD-Core Version:    0.6.0
 */