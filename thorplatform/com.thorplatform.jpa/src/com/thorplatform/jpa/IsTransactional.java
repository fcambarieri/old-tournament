/*    */ package com.thorplatform.jpa;
/*    */ 
/*    */ public enum IsTransactional
/*    */ {
/* 14 */   TRANSACTIONAL("Tranasctional"), 
/* 15 */   NOT_TRANSACTIONAL("Is not Transactional");
/*    */ 
/*    */   private String name;
/*    */ 
/* 20 */   private IsTransactional(String name) { this.name = name; }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 24 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-jpa.jar
 * Qualified Name:     com.thorplatform.jpa.IsTransactional
 * JD-Core Version:    0.6.0
 */