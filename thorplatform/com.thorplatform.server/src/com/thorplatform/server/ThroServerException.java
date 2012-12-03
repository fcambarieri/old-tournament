/*    */ package com.thorplatform.server;
/*    */ 
/*    */ public class ThroServerException extends RuntimeException
/*    */ {
/*    */   public ThroServerException(String msg)
/*    */   {
/* 15 */     super(msg);
/*    */   }
/*    */ 
/*    */   public ThroServerException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ThroServerException(String message, Throwable cause) {
/* 23 */     super(message, cause);
/*    */   }
/*    */   public ThroServerException(Throwable cause) {
/* 26 */     super(cause);
/*    */   }
/*    */ }

/* Location:           F:\proyectos\TDK-Project\tdk.server\lib\com-thorplatform-server.jar
 * Qualified Name:     com.thorplatform.server.ThroServerException
 * JD-Core Version:    0.6.2
 */