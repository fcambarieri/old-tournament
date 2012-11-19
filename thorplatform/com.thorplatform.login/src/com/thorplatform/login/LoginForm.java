/*    */ package com.thorplatform.login;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.GroupLayout;
/*    */ import javax.swing.GroupLayout.Alignment;
/*    */ import javax.swing.GroupLayout.ParallelGroup;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JLayeredPane;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JPasswordField;
/*    */ import javax.swing.JTextField;
/*    */ import org.openide.util.NbBundle;
/*    */ 
/*    */ public class LoginForm extends JPanel
/*    */ {
/*    */   public JLabel lblBackground;
/*    */   public JLabel lblFeedback;
/*    */   public JLabel lblIconKey;
/*    */   public JLabel lblPassword;
/*    */   public JLabel lblUsuario;
/*    */   public JPanel pnlBackground;
/*    */   public JPanel pnlFeedBack;
/*    */   public JLayeredPane pnlLayer;
/*    */   public JPasswordField txtPassword;
/*    */   public JTextField txtUserName;
/*    */ 
/*    */   public LoginForm()
/*    */   {
/* 16 */     initComponents();
/*    */   }
/*    */ 
/*    */   private void initComponents()
/*    */   {
/* 28 */     this.pnlLayer = new JLayeredPane();
/* 29 */     this.lblUsuario = new JLabel();
/* 30 */     this.lblPassword = new JLabel();
/* 31 */     this.txtUserName = new JTextField();
/* 32 */     this.txtPassword = new JPasswordField();
/* 33 */     this.pnlFeedBack = new JPanel();
/* 34 */     this.lblFeedback = new JLabel();
/* 35 */     this.lblIconKey = new JLabel();
/* 36 */     this.pnlBackground = new JPanel();
/* 37 */     this.lblBackground = new JLabel();
/*    */ 
/* 39 */     this.lblUsuario.setFont(new Font("DejaVu Sans", 1, 13));
/* 40 */     this.lblUsuario.setForeground(Color.lightGray);
/* 41 */     this.lblUsuario.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.lblUsuario.text"));
/* 42 */     this.lblUsuario.setBounds(20, 90, 70, 17);
/* 43 */     this.pnlLayer.add(this.lblUsuario, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 45 */     this.lblPassword.setFont(new Font("DejaVu Sans", 1, 13));
/* 46 */     this.lblPassword.setForeground(Color.lightGray);
/* 47 */     this.lblPassword.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.lblPassword.text"));
/* 48 */     this.lblPassword.setBounds(20, 125, 80, 17);
/* 49 */     this.pnlLayer.add(this.lblPassword, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 51 */     this.txtUserName.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.txtUserName.text"));
/* 52 */     this.txtUserName.setBounds(100, 80, 230, 27);
/* 53 */     this.pnlLayer.add(this.txtUserName, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 55 */     this.txtPassword.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.txtPassword.text"));
/* 56 */     this.txtPassword.addActionListener(new ActionListener() {
/*    */       public void actionPerformed(ActionEvent evt) {
/* 58 */         LoginForm.this.txtPasswordActionPerformed(evt);
/*    */       }
/*    */     });
/* 61 */     this.txtPassword.setBounds(100, 120, 230, 27);
/* 62 */     this.pnlLayer.add(this.txtPassword, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 64 */     this.pnlFeedBack.setLayout(new BorderLayout());
/*    */ 
/* 66 */     this.lblFeedback.setFont(new Font("DejaVu Sans", 1, 13));
/* 67 */     this.lblFeedback.setForeground(Color.red);
/* 68 */     this.lblFeedback.setHorizontalAlignment(0);
/* 69 */     this.lblFeedback.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.lblFeedback.text"));
/* 70 */     this.pnlFeedBack.add(this.lblFeedback, "Center");
/*    */ 
/* 72 */     this.pnlFeedBack.setBounds(10, 190, 340, 30);
/* 73 */     this.pnlLayer.add(this.pnlFeedBack, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 75 */     this.lblIconKey.setIcon(new ImageIcon(getClass().getResource("/com/thorplatform/login/password.png")));
/* 76 */     this.lblIconKey.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.lblIconKey.text"));
/* 77 */     this.lblIconKey.setBounds(10, 0, 64, 70);
/* 78 */     this.pnlLayer.add(this.lblIconKey, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 80 */     this.pnlBackground.setLayout(new BorderLayout());
/*    */ 
/* 82 */     this.lblBackground.setIcon(new ImageIcon(getClass().getResource("/com/thorplatform/login/background-chico.png")));
/* 83 */     this.lblBackground.setText(NbBundle.getMessage(LoginForm.class, "LoginForm.lblBackground.text"));
/* 84 */     this.pnlBackground.add(this.lblBackground, "Center");
/*    */ 
/* 86 */     this.pnlBackground.setBounds(0, 0, 370, 300);
/* 87 */     this.pnlLayer.add(this.pnlBackground, JLayeredPane.DEFAULT_LAYER);
/*    */ 
/* 89 */     GroupLayout layout = new GroupLayout(this);
/* 90 */     setLayout(layout);
/* 91 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pnlLayer, GroupLayout.Alignment.TRAILING, -1, 363, 32767));
/*    */ 
/* 95 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pnlLayer, -1, 241, 32767));
/*    */   }
/*    */ 
/*    */   private void txtPasswordActionPerformed(ActionEvent evt)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\Fernando\Escritorio\backup_disk_1T\Taekwondo\TDK-Project\lib\com-thorplatform-login.jar
 * Qualified Name:     com.thorplatform.login.LoginForm
 * JD-Core Version:    0.6.0
 */