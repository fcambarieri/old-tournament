package com.thorplatform.server;

public interface ServerConfig
{
  public void loadFileProperties(String paramString);

  public String get(String paramString);
}
