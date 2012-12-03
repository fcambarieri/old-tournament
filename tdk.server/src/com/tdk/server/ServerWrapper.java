/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.server;

import java.net.UnknownHostException;
import org.openide.util.Exceptions;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 *
 * @author sabon
 */
public class ServerWrapper implements WrapperListener {

    private TDKServer server;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WrapperManager.start(new ServerWrapper(), args);
    }

    @Override
    public Integer start(String[] args) {
        server = new TDKServer();
        try {
            server.runServer(args);
        } catch (Throwable e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    @Override
    public int stop(int i) {
        try {
            server.runServer(new String[]{"stop"});
        } catch (NumberFormatException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnknownHostException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        return 0;
    }

    @Override
    public void controlEvent(int event) {
        System.out.println(String.format("TestWrapper: controlEvent({0})", new Integer(event)));

        if (event == 202) {
            if (WrapperManager.isLaunchedAsService()) {
                System.out.println(String.format("TestWrapper:   Ignoring logoff event"));
            } else {
                WrapperManager.stop(0);
            }
        } else if (event != 200) {
            WrapperManager.stop(0);
        }
    }
}
