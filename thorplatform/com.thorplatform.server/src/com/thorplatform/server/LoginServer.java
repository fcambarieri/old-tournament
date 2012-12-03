package com.thorplatform.server;

import com.thorplatform.login.WorkStation;

public interface LoginServer
{
  public Object login(String user, String password, String workPlace, WorkStation estacionDeTrabajo);

  public Object validate(String paramString1, String paramString2);

  public Object getWorkPlace(String paramString);
}

