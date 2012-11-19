package com.thorplatform.notifier;

public abstract interface Notifier
{
  public abstract void mark(Class paramClass);

  public abstract void mark(Class paramClass, Object paramObject);

  public abstract void mark(Class paramClass, NotifierEvent paramNotifierEvent, Object paramObject);

  public abstract void notifyAllListener();
}

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-notifier.jar
 * Qualified Name:     com.thorplatform.notifier.Notifier
 * JD-Core Version:    0.6.0
 */