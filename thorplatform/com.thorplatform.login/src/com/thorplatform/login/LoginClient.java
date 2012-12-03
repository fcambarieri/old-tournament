package com.thorplatform.login;

import java.util.List;

public interface LoginClient
{
  public void login(String serverHost, String userName, String password, String workStation);

  public <T> T getService(Class<T> paramClass);

  public <T> T getOperador(Class<T> paramClass);

  public List getPermisionList(String paramString);
}

