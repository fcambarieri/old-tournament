package com.thorplatform.jpa;

import java.lang.reflect.Method;

public interface JPAInterceptorService
{
  public void preIntercept(JPAService paramJPAService, Method paramMethod, Object[] paramArrayOfObject);
}

