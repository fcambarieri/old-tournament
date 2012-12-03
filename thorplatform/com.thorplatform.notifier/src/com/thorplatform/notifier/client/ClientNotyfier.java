package com.thorplatform.notifier.client;

import com.thorplatform.notifier.NotifierListener;
import com.thorplatform.notifier.NotifierObject;

public  interface ClientNotyfier
{
  public  void addNotifierListener(Class paramClass, NotifierListener paramNotifierListener);

  public  void removeNotifierListener(Class paramClass, NotifierListener paramNotifierListener);

  public  void fireNotifierListener(NotifierObject paramNotifierObject);

  public  void installDeamonNotyfier(String paramString, int paramInt);
}

