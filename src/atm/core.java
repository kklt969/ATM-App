/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package atm;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.time.format.DateTimeFormatter;
import java.time.*;
public class core extends javax.swing.JFrame {
        
     Connection con = null;
     Statement ste  = null;
     ResultSet rs = null;
      double amount_input ;
    LoginPage lg = new LoginPage();
    
    
            
    public core(String un) {
        initComponents();
        try{
        con = clsDBConnection1.getConnection1();
       appendToPane(jtp,"Welcome to Future INC!",StyleConstants.ALIGN_CENTER, 16);
       appendToPane(jtp,"/nPlease select your account type",StyleConstants.ALIGN_LEFT, 16);
       
       jtp.setEditable(false);
     
       LocalDate d =  LocalDate.now();
       DateTimeFormatter dateFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       String formattedDate = d.format(dateFormatter);
       date.setText(formattedDate);
       this.lg.un = un;
      
        
        fill_label(un);
     
       
                }
       
       catch(Exception e)
       {
           System.out.println(e);
       }
            
    }

    
  
    public void fill_label(String un)
    {
        try{
          System.out.println(un); 
        ste = con.createStatement();
        
        String sql = "SELECT u.name, ab.user_id " +
                     "FROM user_auth.account_balance AS ab " +
                     "JOIN user_auth.users AS u ON ab.user_id = u.id " +
                     "WHERE u.username = '"+un+"'";
        
        rs = ste.executeQuery(sql);
         while(rs.next())
        {
            String name = rs.getString(1);
            String i_d   = rs.getString(2);
            String display = "User ID = "+i_d+" | Name = "+name;
           
            dp.setText(display);
            fake.setText(i_d);
            System.out.println("ok");
           
        }
        }
        catch(SQLException sqle)
        {
            System.out.println(sqle);
        }
    }
    
   public void withdraw_balance()
   {
      
        try{
        //appendToPane(jtp,"\nTo deposit , Enter the desired amount below and click 'Enter'",StyleConstants.ALIGN_LEFT, 14);
        
        int id = Integer.parseInt(fake.getText().trim());
         ste = con.createStatement();
         
         double initial_amount = 0 ;
         
         String sql = "Select amount from user_auth.account_balance where user_id ="+id;
         rs = ste.executeQuery(sql);
         while(rs.next())
         {
             initial_amount = rs.getDouble(1);
         }
         
         amount_input = Double.parseDouble(JOptionPane.showInputDialog(this,"Enter amount"));
         
         double final_amount =  initial_amount - amount_input;
         
          sql = "Update user_auth.account_balance SET amount="+final_amount+"WHERE user_id ="+id;
         
         int result = ste.executeUpdate(sql);
         if(result == 1)
         {
             JOptionPane.showMessageDialog(this, "You have success fully withdraw "+amount_input+"$ from your account");
             moneyimg m = new moneyimg();
             m.show();
         }
         
         jtp.setText("");
         appendToPane(jtp,"\n\nPRESS 'BALANCES' to track balances of your account,\nPRESS 'TRANSFER' to transfer funds ,\nPRESS 'DEPOSIT' to deposit,\nPRESS 'WITHDRAW' to withdraw funds  ",StyleConstants.ALIGN_LEFT, 14);        
        
        }
        catch(SQLException sqle)
        {
            System.out.println(sqle);
        }
       
   }
   
   public void transfer_balance()
   {
       try{
           double balance = 0;
           int id = Integer.parseInt(fake.getText().trim());
           String sql ;
            ste = con.createStatement();
            int transfer_acc_id_unchecked =0;
             transfer_acc_id_unchecked = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter the ID number of the account you want to transfer"));
            
             int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to proceed with account ID: " + transfer_acc_id_unchecked+ "?", "Confirmation", JOptionPane.YES_NO_OPTION);
        int transfer_acc_id_checked=0;
        // Check the user's choice
        if (confirmation == JOptionPane.YES_OPTION) 
        
        {
          sql = "Select user_id from user_auth.account_balance";
          rs = ste.executeQuery(sql);
          boolean found = false;
          while(rs.next())
          {
                  transfer_acc_id_checked = transfer_acc_id_unchecked;
                  
              

          }
         
         
          
           double amount = Double.parseDouble(JOptionPane.showInputDialog(this,"Enter the amount of money, you want to transfer"));
           
           sql = "Select amount from user_auth.account_balance where user_id ="+id;
           rs = ste.executeQuery(sql);
           boolean status = false;
           while (rs.next() )
           {
               balance = rs.getDouble(1);
             
           }
           
           while(balance < amount)
           {
                   amount = Double.parseDouble(JOptionPane.showInputDialog(this,"Insufficient balance, Enter the amount again"));
               
           }
           
          double target_acc_balance = 0;
           
           if(balance >= amount)
               {
                   
                   sql = "Select amount from user_auth.account_balance where user_id ="+transfer_acc_id_checked;
                   rs = ste.executeQuery(sql);
                     while (rs.next() )
                                {
                                    target_acc_balance = rs.getDouble(1);

                                }
                   
                    sql = "Update user_auth.account_balance SET amount="+(amount+target_acc_balance)+"WHERE user_id ="+transfer_acc_id_checked;

                        int result = ste.executeUpdate(sql);
                        if(result == 1)
                        {
                            appendToPane(jtp,"\n\n\n\n\n\n\n\n You have successfully transfered "+amount+"$ to the account, id = "+transfer_acc_id_checked,StyleConstants.ALIGN_LEFT, 14);
                        }
                        
                         sql = "Update user_auth.account_balance SET amount="+(balance-amount)+"WHERE user_id ="+id;
         
                          result = ste.executeUpdate(sql);
                          if(result == 1)
                        {
                            System.out.println("Transfer complete");
                        }

                       } 
                      
             jtp.setText("");
         appendToPane(jtp,"\n\nPRESS 'BALANCES' to track balances of your account,\nPRESS 'TRANSFER' to transfer funds ,\nPRESS 'DEPOSIT' to deposit,\nPRESS 'WITHDRAW' to withdraw funds  ",StyleConstants.ALIGN_LEFT, 14);        
         }
         else 
         {

              JOptionPane.showMessageDialog(null, "Transfer canceled.");
         }
       }
       
       catch(SQLException sqle)
       {
           System.out.println(sqle);
       }
   }
   
    
    public void deposit_balance()
    {
        try{
        //appendToPane(jtp,"\nTo deposit , Enter the desired amount below and click 'Enter'",StyleConstants.ALIGN_LEFT, 14);
        
        int id = Integer.parseInt(fake.getText().trim());
         ste = con.createStatement();
         
         double initial_amount = 0 ;
         
         String sql = "Select amount from user_auth.account_balance where user_id ="+id;
         rs = ste.executeQuery(sql);
         while(rs.next())
         {
             initial_amount = rs.getDouble(1);
         }
         
         
         
         
         amount_input = Double.parseDouble(JOptionPane.showInputDialog(this,"Enter amount"));
         
         double final_amount = amount_input + initial_amount;
         
          sql = "Update user_auth.account_balance SET amount="+final_amount+"WHERE user_id ="+id;
         
         int result = ste.executeUpdate(sql);
         if(result == 1)
         {
             JOptionPane.showMessageDialog(this, "You have success fully deposited "+amount_input+"$ into your account");
         }
                 
         jtp.setText("");
         appendToPane(jtp,"\n\nPRESS 'BALANCES' to track balances of your account,\nPRESS 'TRANSFER' to transfer funds ,\nPRESS 'DEPOSIT' to deposit,\nPRESS 'WITHDRAW' to withdraw funds  ",StyleConstants.ALIGN_LEFT, 14);        
        }
        catch(SQLException sqle)
        {
            System.out.println(sqle);
        }
        
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtp = new javax.swing.JTextPane();
        panel1 = new java.awt.Panel();
        acctype = new javax.swing.JComboBox<>();
        ID = new javax.swing.JLabel();
        enter = new javax.swing.JButton();
        deposit = new javax.swing.JButton();
        transfer = new javax.swing.JButton();
        withdrawl = new javax.swing.JButton();
        check_balance = new javax.swing.JButton();
        date = new javax.swing.JLabel();
        dp = new javax.swing.JLabel();
        fake = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jtp.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(jtp);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel1.setBackground(new java.awt.Color(153, 153, 153));

        acctype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--------------Select--------------", "Savings Account" }));
        acctype.setName("acctype"); // NOI18N
        acctype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acctypeActionPerformed(evt);
            }
        });

        ID.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        ID.setForeground(new java.awt.Color(0, 0, 0));
        ID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ID.setText("Select Account Type ");

        enter.setBackground(new java.awt.Color(51, 51, 255));
        enter.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        enter.setText("Close");
        enter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterActionPerformed(evt);
            }
        });

        deposit.setBackground(new java.awt.Color(204, 204, 255));
        deposit.setFont(new java.awt.Font("Leelawadee UI", 1, 16)); // NOI18N
        deposit.setForeground(new java.awt.Color(0, 0, 0));
        deposit.setText("DEPOSIT");
        deposit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depositActionPerformed(evt);
            }
        });

        transfer.setBackground(new java.awt.Color(0, 255, 102));
        transfer.setFont(new java.awt.Font("Leelawadee UI", 1, 16)); // NOI18N
        transfer.setForeground(new java.awt.Color(0, 0, 0));
        transfer.setText("TRANSFER");
        transfer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        transfer.setName("transfer"); // NOI18N
        transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferActionPerformed(evt);
            }
        });

        withdrawl.setBackground(new java.awt.Color(255, 102, 102));
        withdrawl.setFont(new java.awt.Font("Leelawadee UI", 1, 15)); // NOI18N
        withdrawl.setForeground(new java.awt.Color(0, 0, 0));
        withdrawl.setText("WITHDRAW");
        withdrawl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        withdrawl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withdrawlActionPerformed(evt);
            }
        });

        check_balance.setBackground(new java.awt.Color(102, 204, 255));
        check_balance.setFont(new java.awt.Font("Leelawadee UI", 1, 16)); // NOI18N
        check_balance.setForeground(new java.awt.Color(0, 0, 0));
        check_balance.setText("BALANCES\n");
        check_balance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        check_balance.setName("check_balance"); // NOI18N
        check_balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_balanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(check_balance, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(withdrawl, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deposit, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(enter, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acctype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ID)
                    .addComponent(acctype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deposit, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(check_balance, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(withdrawl, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(enter)
                .addContainerGap())
        );

        dp.setBackground(new java.awt.Color(153, 255, 153));
        dp.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        dp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dp.setName("dp"); // NOI18N

        fake.setText("jLabel1");
        fake.setName("fake"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(fake, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                .addComponent(dp, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dp, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fake))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void check_balanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_balanceActionPerformed
            try
            {
                if(acctype.getSelectedIndex()==1)
                {
                    jtp.setText("");
                
               double balance = 0;
                ste = con.createStatement();
                int id = Integer.parseInt(fake.getText().trim());
                String sql = "SELECT amount from user_auth.account_balance WHERE user_id ="+id;
                rs = ste.executeQuery(sql);
                
                while(rs.next())
                    
                {
                    balance = rs.getDouble(1);
                    
                }
                
                 appendToPane(jtp,"Your balance is "+balance+"$",StyleConstants.ALIGN_CENTER, 16);
                }
            }
            catch(SQLException sqle)
            {
                System.out.println(sqle);
            }
    }//GEN-LAST:event_check_balanceActionPerformed

    private void acctypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acctypeActionPerformed
        if(acctype.getSelectedIndex() == 1)
        {
            jtp.setText("");
            
            appendToPane(jtp,"\n\nPRESS 'BALANCES' to track balances of your account,\nPRESS 'TRANSFER' to transfer funds ,\nPRESS 'DEPOSIT' to deposit,\nPRESS 'WITHDRAW' to withdraw funds  ",StyleConstants.ALIGN_LEFT, 14);
            
        }
    }//GEN-LAST:event_acctypeActionPerformed

    private void depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depositActionPerformed
        if(acctype.getSelectedIndex() == 1)
        {
        jtp.setText("");
        appendToPane(jtp,"\n\n\nTo deposit , Enter the desired amount below and click 'Enter'",StyleConstants.ALIGN_LEFT, 14);
        deposit_balance();
        }

    }//GEN-LAST:event_depositActionPerformed

    private void enterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterActionPerformed
         
         this.dispose();
    }//GEN-LAST:event_enterActionPerformed

    private void withdrawlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withdrawlActionPerformed
         if(acctype.getSelectedIndex() == 1)
        {
        jtp.setText("");
        appendToPane(jtp,"\n\n\nTo withdraw , Enter the desired amount below and click 'Enter'",StyleConstants.ALIGN_LEFT, 14);
        withdraw_balance();
        }
    }//GEN-LAST:event_withdrawlActionPerformed

    private void transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferActionPerformed
        if(acctype.getSelectedIndex() == 1)
        {
        jtp.setText("");
        
        transfer_balance();
        }
    }//GEN-LAST:event_transferActionPerformed

    /**
     * @param args the command line arguments
     */
    public  void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(core.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(core.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(core.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(core.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new core(lg.un).setVisible(true);
            }
        });
    }
    
     private static void appendToPane(JTextPane jtp, String text, Integer alignment, int fontSize) {
         StyledDocument doc = jtp.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        if (alignment != null) {
            // Create a paragraph element
            MutableAttributeSet paragraphStyle = new SimpleAttributeSet();
            StyleConstants.setAlignment(paragraphStyle, alignment);
            doc.setParagraphAttributes(doc.getLength(), 1, paragraphStyle, false);
        }
        StyleConstants.setFontSize(style, fontSize);
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    
    }
    
    


// Similarly define methods for other buttons...
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ID;
    private javax.swing.JComboBox<String> acctype;
    private javax.swing.JButton check_balance;
    private javax.swing.JLabel date;
    private javax.swing.JButton deposit;
    private javax.swing.JLabel dp;
    private javax.swing.JButton enter;
    private javax.swing.JLabel fake;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jtp;
    private java.awt.Panel panel1;
    private javax.swing.JButton transfer;
    private javax.swing.JButton withdrawl;
    // End of variables declaration//GEN-END:variables
}
