package com.thorplatform.notifier;

import com.thorplatform.notifier.client.ClientNotyfier;
import com.thorplatform.notifier.internal.PropertiesNotifierLoader;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

public class Module extends ModuleInstall {

    public void restored() {
        super.restored();
        System.out.println("Inicializando Notifier Module...");
        PropertiesNotifierLoader loader = new PropertiesNotifierLoader("client.properties");
        String multicastAddress = loader.getProperty("multicast_address");
        int multicastPort = Integer.parseInt(loader.getProperty("multicast_port"));
        ClientNotyfier clientNotifier = (ClientNotyfier) Lookup.getDefault().lookup(ClientNotyfier.class);
        clientNotifier.installDeamonNotyfier(multicastAddress, multicastPort);
    }
}