package com.thorplatform.notifier.internal;

import com.thorplatform.notifier.Notifier;
import com.thorplatform.notifier.NotifierEvent;
import com.thorplatform.notifier.NotifierObject;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DefaultNotyfier implements Notifier {

    private Logger logger = Logger.getLogger(getClass().getName());
    private InetAddress group;
    private DatagramSocket s;
    private PropertiesNotifierLoader properties;
    private int port;
    private boolean usaNotifier;
    private List<NotifierObject> notiferObjectsList = new ArrayList();

    public DefaultNotyfier() {
        this.properties = new PropertiesNotifierLoader("server.properties");
        configureNotifier();
    }

    public synchronized void mark(Class className) {
        mark(className, null);
    }

    public synchronized void mark(Class className, Object object) {
        mark(className, NotifierEvent.ALL, object);
    }

    public synchronized void mark(Class className, NotifierEvent event, Object object) {
        NotifierObject no = new NotifierObject(event, className, object);
        if (!this.notiferObjectsList.contains(no)) {
            this.notiferObjectsList.add(no);
            this.logger.info("Mark on:" + className.getName());
        }
    }

    public synchronized void notifyAllListener() {
        if ((this.usaNotifier) && (this.notiferObjectsList.size() > 0)) {
            for (NotifierObject no : this.notiferObjectsList) {
                try {
                    this.logger.info("NOTIFIER: Abre Streams");
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                    os.flush();
                    os.writeObject(no);
                    os.flush();
                    byte[] sendBuf = byteStream.toByteArray();
                    DatagramPacket hi = new DatagramPacket(sendBuf, sendBuf.length, this.group, this.port);
                    try {
                        this.s.send(hi);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    os.close();
                    byteStream.close();
                    this.logger.info("NOTIFIER: Cierra Streams");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            this.notiferObjectsList.clear();
        }
    }

    private void configureNotifier() {
        String multicastAddress = this.properties.getProperty("multicast_address");
        int multicastPort = Integer.parseInt(this.properties.getProperty("multicast_port"));
        this.usaNotifier = (!this.properties.getProperty("notifier").equals("no"));

        if (this.usaNotifier) {
            try {
                this.group = InetAddress.getByName(multicastAddress);
                this.port = multicastPort;
                this.s = new MulticastSocket(this.port);
                ((MulticastSocket) this.s).joinGroup(this.group);
                ((MulticastSocket) this.s).setTimeToLive(16);
                System.out.println("NOTIFIER: Server enviando a MultiCast");
            } catch (Exception ex) {
                try {
                    this.group = InetAddress.getByName("localhost");
                    this.port = 8031;
                    this.s = new DatagramSocket();
                    System.out.println("NOTIFIER: Server enviando a LocalHost");
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
            }
        } else {
            System.out.println("NOTIFIER: Notifier Deshabilitado");
        }
    }
}
