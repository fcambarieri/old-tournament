package com.thorplatform.jpa;

import java.lang.reflect.Method;

public interface JPAInterceptor
{
  public <T extends JPAService> Class<T> getInterceptedLocalServiceClass();

  public void preIntercept(JPAService paramJPAService, Method paramMethod, Object[] paramArrayOfObject);

  public void posIntercept(JPAService paramJPAService, Method paramMethod, Object[] paramArrayOfObject);
}

