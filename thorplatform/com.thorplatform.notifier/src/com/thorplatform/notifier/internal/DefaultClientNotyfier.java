package com.thorplatform.notifier.internal;

import com.thorplatform.notifier.NotifierListener;
import com.thorplatform.notifier.NotifierObject;
import com.thorplatform.notifier.client.ClientNotyfier;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultClientNotyfier implements ClientNotyfier {

    private Map<Class, List<NotifierListener>> listenerMap = new HashMap();

    public synchronized void addNotifierListener(Class className, NotifierListener listener) {
        List list = (List) this.listenerMap.get(className);
        if (list == null) {
            synchronized (this.listenerMap) {
                this.listenerMap.put(className, new ArrayList());
            }
        } else {
            synchronized (list) {
                list.add(listener);
            }
        }
    }

    public synchronized void removeNotifierListener(Class className, NotifierListener listener) {
        List list = (List) this.listenerMap.get(className);
        if (list != null) {
            synchronized (list) {
                list.remove(listener);
            }
        }
    }

    public synchronized void fireNotifierListener(NotifierObject notifierObject) {
        synchronized (this.listenerMap) {
            List listeners = (List) this.listenerMap.get(notifierObject.getClassName());
            if (listeners != null) {
                synchronized (listeners) {
                    for (Object listener : listeners) {
                        try {
                            ((NotifierListener)listener).onNotify(notifierObject);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void installDeamonNotyfier(String multicastAddress, int multicastPort) {
        /*String address = multicastAddress;
        int port = multicastPort;
        Thread t = new Thread(new Runnable(address, port) {

            public void run() {
                try {
                    DatagramSocket s;
                    try {
                        group = InetAddress.getByName(this.val$address);
                        s = new MulticastSocket(this.val$port);
                        ((MulticastSocket) s).joinGroup(group);
                        System.out.println("Cliente escuchando en MultiCast");
                    } catch (Exception e) {
                        InetAddress group = InetAddress.getByName("localhost");
                        s = new DatagramSocket(8031);
                        System.out.println("Cliente escuchando en LocalHost");
                    }
                    while (true) {
                        byte[] buf = new byte[1000];
                        DatagramPacket recv = new DatagramPacket(buf, buf.length);
                        s.receive(recv);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(recv.getData(), 0, recv.getLength()));
                        NotifierObject no = (NotifierObject) ois.readObject();
                        DefaultClientNotyfier.this.fireNotifierListener(no);
                        ois.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();*/
    }
}
