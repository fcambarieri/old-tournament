package com.thorplatform.notifier;

public interface Notifier
{
  public void mark(Class paramClass);

  public void mark(Class paramClass, Object paramObject);

  public void mark(Class paramClass, NotifierEvent paramNotifierEvent, Object paramObject);

  public void notifyAllListener();
}

