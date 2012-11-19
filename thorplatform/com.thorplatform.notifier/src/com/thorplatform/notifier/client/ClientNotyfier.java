package com.thorplatform.notifier.client;

import com.thorplatform.notifier.NotifierListener;
import com.thorplatform.notifier.NotifierObject;

public abstract interface ClientNotyfier
{
  public abstract void addNotifierListener(Class paramClass, NotifierListener paramNotifierListener);

  public abstract void removeNotifierListener(Class paramClass, NotifierListener paramNotifierListener);

  public abstract void fireNotifierListener(NotifierObject paramNotifierObject);

  public abstract void installDeamonNotyfier(String paramString, int paramInt);
}

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-notifier.jar
 * Qualified Name:     com.thorplatform.notifier.client.ClientNotyfier
 * JD-Core Version:    0.6.0
 */