package com.thorplatform.jpa;

import java.lang.reflect.Method;

public abstract interface JPAInterceptor
{
  public abstract <T extends JPAService> Class<T> getInterceptedLocalServiceClass();

  public abstract void preIntercept(JPAService paramJPAService, Method paramMethod, Object[] paramArrayOfObject);

  public abstract void posIntercept(JPAService paramJPAService, Method paramMethod, Object[] paramArrayOfObject);
}

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-jpa.jar
 * Qualified Name:     com.thorplatform.jpa.JPAInterceptor
 * JD-Core Version:    0.6.0
 */