package com.thorplatform.jpa;

public abstract interface JPAServiceFactory
{
  public abstract <T> T getService(Class<T> paramClass);
}

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-jpa.jar
 * Qualified Name:     com.thorplatform.jpa.JPAServiceFactory
 * JD-Core Version:    0.6.0
 */