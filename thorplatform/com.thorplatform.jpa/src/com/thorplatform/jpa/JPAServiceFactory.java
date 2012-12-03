package com.thorplatform.jpa;

public interface JPAServiceFactory
{
  public <T> T getService(Class<T> paramClass);
}

