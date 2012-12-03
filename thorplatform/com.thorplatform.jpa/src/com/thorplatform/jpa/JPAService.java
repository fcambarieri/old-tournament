 package com.thorplatform.jpa;
 
 import com.thorplatform.notifier.Notifier;
 import com.thorplatform.utils.DateTimeUtils;
 import com.thorplatform.utils.NumericUtils;
 import java.util.List;
 import javax.persistence.EntityManager;
 
 public class JPAService
 {
   private NumericUtils numericUtils;
   private DateTimeUtils dateTimeUtils;
   private Notifier notifier;
   protected static final String STARTING_WITH_WILDCARD = "%";
 
   public void setContext(JPAService jpaService)
   {
     setNumericUtils(jpaService.getNumericUtils());
     setDateTimeUtils(jpaService.getDateTimeUtils());
     setNotifier(jpaService.getNotifier());
   }
 
   public EntityManager getEntityManager() {
     return (EntityManager)getProperty("entityManager");
   }
 
   public void setEntityManager(EntityManager entityManger) {
     setProperty("entityManager", entityManger);
   }
 
   public NumericUtils getNumericUtils() {
     return this.numericUtils;
   }
 
   public void setNumericUtils(NumericUtils numericUtils) {
     this.numericUtils = numericUtils;
   }
 
   public DateTimeUtils getDateTimeUtils() {
     return this.dateTimeUtils;
   }
 
   public void setDateTimeUtils(DateTimeUtils dateTimeUtils) {
     this.dateTimeUtils = dateTimeUtils;
   }
 
   public void setProperty(String key, Object object) {
     JPAContext.getInstance().setProperty(key, object);
   }
 
   public Object getProperty(String key)
   {
     if (key == null)
       throw new IllegalArgumentException("No se puede recuperar una clase nula");
     return JPAContext.getInstance().getProperty(key);
   }
 
   public Notifier getNotifier() {
     return this.notifier;
   }
 
   public void setNotifier(Notifier notifier) {
     this.notifier = notifier;
   }
 
   public void updateList(List attachedList, List dettachedList)
   {
     for (int i = 0; i < attachedList.size(); i++) {
       Object obj = attachedList.get(i);
       if (!dettachedList.contains(obj)) {
         attachedList.remove(obj);
         try {
           getEntityManager().remove(obj);
         } catch (Throwable ex) {
           System.err.println("Error al intentar elimnar " + obj.toString() + "\n");
           ex.printStackTrace();
         }
       }
     }
   }
 
   public void updateList(List listToUpdate)
   {
     for (int i = 0; i < listToUpdate.size(); i++)
       getEntityManager().merge(listToUpdate.get(i));
   }
 
   public void clearList(List listToRemove)
   {
     for (int i = 0; i < listToRemove.size(); i++)
       try {
         getEntityManager().remove(listToRemove.get(i));
       } catch (Throwable ex) {
         System.err.println("Error al intentar elimnar " + listToRemove.get(i).toString() + "\n");
         ex.printStackTrace();
       }
   }
 }

