/*    */ package com.thorplatform.notifier.internal;
/*    */ 
/*    */ import com.thorplatform.notifier.Notifier;
/*    */ import com.thorplatform.notifier.NotifierEvent;
/*    */ import com.thorplatform.notifier.NotifierObject;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.MulticastSocket;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class DefaultNotyfier
/*    */   implements Notifier
/*    */ {
/* 28 */   private Logger logger = Logger.getLogger(getClass().getName());
/*    */   private InetAddress group;
/*    */   private DatagramSocket s;
/*    */   private PropertiesNotifierLoader properties;
/*    */   private int port;
/*    */   private boolean usaNotifier;
/* 35 */   private List<NotifierObject> notiferObjectsList = new ArrayList();
/*    */ 
/*    */   public DefaultNotyfier() {
/* 38 */     this.properties = new PropertiesNotifierLoader("server.properties");
/* 39 */     configureNotifier();
/*    */   }
/*    */ 
/*    */   public synchronized void mark(Class className) {
/* 43 */     mark(className, null);
/*    */   }
/*    */ 
/*    */   public synchronized void mark(Class className, Object object) {
/* 47 */     mark(className, NotifierEvent.ALL, object);
/*    */   }
/*    */ 
/*    */   public synchronized void mark(Class className, NotifierEvent event, Object object) {
/* 51 */     NotifierObject no = new NotifierObject(event, className, object);
/* 52 */     if (!this.notiferObjectsList.contains(no)) {
/* 53 */       this.notiferObjectsList.add(no);
/* 54 */       this.logger.info("Mark on:" + className.getName());
/*    */     }
/*    */   }
/*    */ 
/*    */   public synchronized void notifyAllListener() {
/* 59 */     if ((this.usaNotifier) && (this.notiferObjectsList.size() > 0))
/*    */     {
/* 62 */       for (NotifierObject no : this.notiferObjectsList) {
/*    */         try {
/* 64 */           this.logger.info("NOTIFIER: Abre Streams");
/* 65 */           ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
/* 66 */           ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
/* 67 */           os.flush();
/* 68 */           os.writeObject(no);
/* 69 */           os.flush();
/* 70 */           byte[] sendBuf = byteStream.toByteArray();
/* 71 */           DatagramPacket hi = new DatagramPacket(sendBuf, sendBuf.length, this.group, this.port);
/*    */           try {
/* 73 */             this.s.send(hi);
/*    */           } catch (IOException ex) {
/* 75 */             ex.printStackTrace();
/*    */           }
/* 77 */           os.close();
/* 78 */           byteStream.close();
/* 79 */           this.logger.info("NOTIFIER: Cierra Streams");
/*    */         } catch (Exception e) {
/* 81 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */ 
/* 85 */       this.notiferObjectsList.clear();
/*    */     }
/*    */   }
/*    */ 
/*    */   private void configureNotifier() {
/* 90 */     String multicastAddress = this.properties.getProperty("multicast_address");
/* 91 */     int multicastPort = Integer.parseInt(this.properties.getProperty("multicast_port"));
/* 92 */     this.usaNotifier = (!this.properties.getProperty("notifier").equals("no"));
/*    */ 
/* 94 */     if (this.usaNotifier)
/*    */       try {
/* 96 */         this.group = InetAddress.getByName(multicastAddress);
/* 97 */         this.port = multicastPort;
/* 98 */         this.s = new MulticastSocket(this.port);
/* 99 */         ((MulticastSocket)this.s).joinGroup(this.group);
/* 100 */         ((MulticastSocket)this.s).setTimeToLive(16);
/* 101 */         System.out.println("NOTIFIER: Server enviando a MultiCast");
/*    */       } catch (Exception ex) {
/*    */         try {
/* 104 */           this.group = InetAddress.getByName("localhost");
/* 105 */           this.port = 8031;
/* 106 */           this.s = new DatagramSocket();
/* 107 */           System.out.println("NOTIFIER: Server enviando a LocalHost");
/*    */         } catch (Exception ex1) {
/* 109 */           ex1.printStackTrace();
/*    */         }
/*    */       }
/*    */     else
/* 113 */       System.out.println("NOTIFIER: Notifier Deshabilitado");
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-notifier.jar
 * Qualified Name:     com.thorplatform.notifier.internal.DefaultNotyfier
 * JD-Core Version:    0.6.0
 */