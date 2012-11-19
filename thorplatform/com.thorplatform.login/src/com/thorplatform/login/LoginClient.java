package com.thorplatform.login;

import java.util.List;

public abstract interface LoginClient
{
  public abstract void login(String paramString1, String paramString2, String paramString3, String paramString4);

  public abstract <T> T getService(Class<T> paramClass);

  public abstract <T> T getOperador(Class<T> paramClass);

  public abstract List getPermisionList(String paramString);
}

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-login.jar
 * Qualified Name:     com.thorplatform.login.LoginClient
 * JD-Core Version:    0.6.0
 */