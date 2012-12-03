 package com.thorplatform.jpa;
 
 public enum IsTransactional
 {
   TRANSACTIONAL("Tranasctional"), 
   NOT_TRANSACTIONAL("Is not Transactional");
 
   private String name;
 
   private IsTransactional(String name) { this.name = name; }
 
   public String getName()
   {
     return this.name;
   }
 }

