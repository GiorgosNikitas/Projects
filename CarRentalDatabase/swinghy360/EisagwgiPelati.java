package swinghy360;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import javax.swing.*; 

public class EisagwgiPelati extends javax.swing.JFrame {

    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");

    
    public EisagwgiPelati() {
        initComponents();
    }

    private void initComponents() {

        jLabel16 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        promithia = new javax.swing.JLabel();
        
        Onoma = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        epitheto = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        email = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        Dieuthinsi = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        adia_odigisis = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        card_number = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        gennisi = new javax.swing.JLabel();
        
       
        jTextField8 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
       
      
        jLabel18 = new javax.swing.JLabel();
    
 
        jLabel19 = new javax.swing.JLabel();
       
        
      
        jLabel21 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
       
        jTextPane1 = new javax.swing.JTextPane();
        

        passwordLabel = new javax.swing.JLabel();
        passwordLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        passwordLabel.setText("Password *");

        jPasswordField1 = new javax.swing.JPasswordField();

      

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        promithia.setBackground(new java.awt.Color(255, 102, 102));
        promithia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        promithia.setText("Eggrafi Pelati");

        Onoma.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Onoma.setText("Onoma *");

        epitheto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        epitheto.setText("Epitheto *");

        email.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        email.setText("Email *");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        Dieuthinsi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Dieuthinsi.setText("Dieuthinsi *");

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        adia_odigisis.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        adia_odigisis.setText("Arithmos Diplomatos *");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        card_number.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        card_number.setText("Card Number *");

        jButton2.setText("Apothikeusi");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gennisi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        gennisi.setText("Imerominia Gennisis (YYYY-MM-DD)*");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(promithia)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jButton2))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Onoma)
                                .addComponent(epitheto)
                                .addComponent(passwordLabel)
                                .addComponent(email)
                                .addComponent(Dieuthinsi)
                                .addComponent(adia_odigisis)
                                .addComponent(card_number)
                                .addComponent(gennisi))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField8, GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            )))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(promithia)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Onoma)
                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(epitheto)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(email)
                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(Dieuthinsi)
                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(adia_odigisis)
                        .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(card_number)
                        .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(gennisi)
                        .addComponent(jTextField8, GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();

    }
    
    public static void registerUser(String firstName, String lastName, String passwordString, String email,
            String address, String birthDate, int age,
            int diplomaNumber, int cardNumber) throws SQLException, ClassNotFoundException {
    	
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection(
				url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
				
				String insertUserQuery = "INSERT INTO pelatis (firstName, lastName, password, email, dieuthinsi_spitiou, imerominia_genisis, age, adia_odigisis, card_number) " +
				        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				try (PreparedStatement pstmt = con.prepareStatement(insertUserQuery)) {
				pstmt.setString(1, firstName);
				pstmt.setString(2, lastName);
				pstmt.setString(3, passwordString);
				pstmt.setString(4, email);
				pstmt.setString(5, address);
				pstmt.setString(6, birthDate);
				pstmt.setInt(7, age);
				pstmt.setInt(8, diplomaNumber);
				pstmt.setInt(9, cardNumber);
				
				pstmt.executeUpdate();
				} finally {
				con.close();
		}
}
    

 // Apothikeusi
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
        
            // Retrieve user input from text fields
        	
            String firstName = jTextField1.getText();
            String lastName = jTextField2.getText();
            String email = jTextField3.getText();
            char[] passwordChars = jPasswordField1.getPassword();
            String password = String.valueOf(passwordChars);
            String address = jTextField4.getText();
            String birthdateString = jTextField8.getText(); 
            LocalDate birthdate = LocalDate.parse(birthdateString, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(birthdate, currentDate);
            int age = period.getYears();
            int diplomaNumber = Integer.parseInt(jTextField5.getText());
            int cardNumber = Integer.parseInt(jTextField6.getText());
           
            registerUser(firstName, lastName, password, email, address, birthdateString, age, diplomaNumber, cardNumber);

            JOptionPane.showMessageDialog(null, "User registered successfully!");
        } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error registering user. Please check your input.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed


    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed


    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EisagwgiPelati.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EisagwgiPelati.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EisagwgiPelati.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EisagwgiPelati.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EisagwgiPelati().setVisible(true);
            }
        });
    }
    public String Douleia;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel promithia;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
   
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel Onoma;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel epitheto;
    private javax.swing.JLabel email;
    private javax.swing.JLabel Dieuthinsi;
    private javax.swing.JLabel adia_odigisis;
    private javax.swing.JLabel card_number;
    private javax.swing.JLabel gennisi;
    private javax.swing.JLabel passwordLabel;

    private javax.swing.JTextField jTextField1;
    private javax.swing.JPasswordField jPasswordField1;
    
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    
    private javax.swing.JTextField jTextField8;
  
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}