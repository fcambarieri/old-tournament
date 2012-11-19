/*    */ package com.thorplatform.jpa.internal;
/*    */ 
/*    */ import com.thorplatform.jpa.JPAInterceptor;
/*    */ import com.thorplatform.jpa.JPAInterceptorService;
/*    */ import com.thorplatform.jpa.JPAService;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.logging.Logger;
/*    */ import org.openide.util.Lookup;
/*    */ import org.openide.util.Lookup.Result;
/*    */ import org.openide.util.Lookup.Template;
/*    */ 
/*    */ public class DefaultJPAInterceptorService
/*    */   implements JPAInterceptorService
/*    */ {
/*    */   private HashMap<Class<? extends JPAService>, JPAInterceptor> trackers;
/* 20 */   private static final Logger logger = Logger.getLogger(DefaultJPAInterceptorService.class.getName());
/*    */ 
/*    */   public DefaultJPAInterceptorService()
/*    */   {
/* 24 */     this.trackers = new HashMap();
/*    */ 
/* 26 */     Lookup.Template template = new Lookup.Template(JPAInterceptor.class);
/* 27 */     Lookup.Result result = Lookup.getDefault().lookup(template);
/* 28 */     Collection instances = result.allInstances();
/* 29 */     for (Iterator iterator = instances.iterator(); iterator.hasNext(); ) {
/* 30 */       JPAInterceptor tracker = (JPAInterceptor)iterator.next();
/* 31 */       this.trackers.put(tracker.getInterceptedLocalServiceClass(), tracker);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void preIntercept(JPAService localService, Method method, Object[] args) {
/* 36 */     JPAInterceptor tracker = (JPAInterceptor)this.trackers.get(localService.getClass());
/* 37 */     if (tracker != null)
/* 38 */       tracker.preIntercept(localService, method, args);
/*    */     else
/* 40 */       logger.info(localService.getClass().getSimpleName() + " " + method.getName());
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-jpa.jar
 * Qualified Name:     com.thorplatform.jpa.internal.DefaultJPAInterceptorService
 * JD-Core Version:    0.6.0
 */