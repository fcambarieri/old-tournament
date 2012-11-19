/*     */ package com.thorplatform.jpa;
/*     */ 
/*     */ import com.thorplatform.notifier.Notifier;
/*     */ import com.thorplatform.utils.DateTimeUtils;
/*     */ import com.thorplatform.utils.NumericUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import javax.persistence.EntityManager;
/*     */ 
/*     */ public class JPAService
/*     */ {
/*     */   private NumericUtils numericUtils;
/*     */   private DateTimeUtils dateTimeUtils;
/*     */   private Notifier notifier;
/*     */   protected static final String STARTING_WITH_WILDCARD = "%";
/*     */ 
/*     */   public void setContext(JPAService jpaService)
/*     */   {
/*  26 */     setNumericUtils(jpaService.getNumericUtils());
/*  27 */     setDateTimeUtils(jpaService.getDateTimeUtils());
/*  28 */     setNotifier(jpaService.getNotifier());
/*     */   }
/*     */ 
/*     */   public EntityManager getEntityManager() {
/*  32 */     return (EntityManager)getProperty("entityManager");
/*     */   }
/*     */ 
/*     */   public void setEntityManager(EntityManager entityManger) {
/*  36 */     setProperty("entityManager", entityManger);
/*     */   }
/*     */ 
/*     */   public NumericUtils getNumericUtils() {
/*  40 */     return this.numericUtils;
/*     */   }
/*     */ 
/*     */   public void setNumericUtils(NumericUtils numericUtils) {
/*  44 */     this.numericUtils = numericUtils;
/*     */   }
/*     */ 
/*     */   public DateTimeUtils getDateTimeUtils() {
/*  48 */     return this.dateTimeUtils;
/*     */   }
/*     */ 
/*     */   public void setDateTimeUtils(DateTimeUtils dateTimeUtils) {
/*  52 */     this.dateTimeUtils = dateTimeUtils;
/*     */   }
/*     */ 
/*     */   public void setProperty(String key, Object object) {
/*  56 */     JPAContext.getInstance().setProperty(key, object);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String key)
/*     */   {
/*  62 */     if (key == null)
/*  63 */       throw new IllegalArgumentException("No se puede recuperar una clase nula");
/*  64 */     return JPAContext.getInstance().getProperty(key);
/*     */   }
/*     */ 
/*     */   public Notifier getNotifier() {
/*  68 */     return this.notifier;
/*     */   }
/*     */ 
/*     */   public void setNotifier(Notifier notifier) {
/*  72 */     this.notifier = notifier;
/*     */   }
/*     */ 
/*     */   public void updateList(List attachedList, List dettachedList)
/*     */   {
/*  87 */     for (int i = 0; i < attachedList.size(); i++) {
/*  88 */       Object obj = attachedList.get(i);
/*  89 */       if (!dettachedList.contains(obj)) {
/*  90 */         attachedList.remove(obj);
/*     */         try {
/*  92 */           getEntityManager().remove(obj);
/*     */         } catch (Throwable ex) {
/*  94 */           System.err.println("Error al intentar elimnar " + obj.toString() + "\n");
/*  95 */           ex.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateList(List listToUpdate)
/*     */   {
/* 108 */     for (int i = 0; i < listToUpdate.size(); i++)
/* 109 */       getEntityManager().merge(listToUpdate.get(i));
/*     */   }
/*     */ 
/*     */   public void clearList(List listToRemove)
/*     */   {
/* 120 */     for (int i = 0; i < listToRemove.size(); i++)
/*     */       try {
/* 122 */         getEntityManager().remove(listToRemove.get(i));
/*     */       } catch (Throwable ex) {
/* 124 */         System.err.println("Error al intentar elimnar " + listToRemove.get(i).toString() + "\n");
/* 125 */         ex.printStackTrace();
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-jpa.jar
 * Qualified Name:     com.thorplatform.jpa.JPAService
 * JD-Core Version:    0.6.0
 */