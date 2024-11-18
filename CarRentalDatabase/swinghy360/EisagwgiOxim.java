/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swinghy360;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class EisagwgiOxim extends javax.swing.JFrame {

    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");

    public EisagwgiOxim() {
        initComponents();
    }

    private void initComponents() {
        promithia = new javax.swing.JLabel();
        kiriomenu = new javax.swing.JButton();
        marka = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField(); // marka
        montelo = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField(); // modelo
        xrwma = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField(); // xrwma
        autonomia = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField(); // arithmos kykloforias
        arkykloforias = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField(); // autonomia
        eidos = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField(); // eidos oximatos
        jButton2 = new javax.swing.JButton();
        typos = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField(); // epibates
        epibates = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField(); // typos oximatos
        kostos2 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField(); // kostos asfalisis
        jTextField13 = new javax.swing.JTextField(); // kostos enoikiasis
        jTextField13.setColumns(14);
        kostos1 = new javax.swing.JLabel();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        promithia.setBackground(new java.awt.Color(255, 102, 102));
        promithia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        promithia.setText("Add new vehicle");

        kiriomenu.setText("Main Menu");
        kiriomenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        marka.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        marka.setText("Brand *");

        montelo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        montelo.setText("Model *");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        xrwma.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        xrwma.setText("Color *");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        autonomia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        autonomia.setText("Range *");

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        arkykloforias.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        arkykloforias.setText("License plate number *");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        eidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        eidos.setText("Category *");

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButton2ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });

        typos.setFont(new java.awt.Font("Tahoma", 0, 14));
        typos.setText("Type *");

        epibates.setFont(new java.awt.Font("Tahoma", 0, 14));
        epibates.setText("Total passengers *");

        kostos2.setFont(new java.awt.Font("Tahoma", 0, 14));
        kostos2.setText("Insurance cost *");

        kostos1.setFont(new java.awt.Font("Tahoma", 0, 14));
        kostos1.setText("Rental cost *");
        
	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(eidos)
	                            .addComponent(marka))
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
	                                .addComponent(jButton2)
	                                .addGap(81, 81, 81))
	                            .addComponent(kostos1, javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(autonomia, javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(montelo, javax.swing.GroupLayout.Alignment.LEADING))
	                        .addGap(0, 0, Short.MAX_VALUE)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addGroup(layout.createSequentialGroup()
	                                    .addGap(94, 94, 94)
	                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING))
	                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING))))
	                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addGroup(layout.createSequentialGroup()
	                                        .addGap(18, 18, 18))))
	                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                                .addGroup(layout.createSequentialGroup()
	                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                    .addGap(18, 18, 18))
	                                .addComponent(kostos2)
	                                .addGroup(layout.createSequentialGroup()
	                                    .addGap(119, 119, 119))))
	                        .addGap(45, 45, 45))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addComponent(epibates)
	                                    .addComponent(typos)
	                                    .addComponent(xrwma, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                    .addComponent(arkykloforias, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                                    .addComponent(jTextField7)
	                                    .addComponent(jTextField8)
	                                    .addComponent(jTextField5)
	                                    .addComponent(jTextField3)
	                                    .addComponent(jTextField2)
	                                    .addComponent(jTextField1)
	                                    .addComponent(jTextField4)
	                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                                .addGap(0, 0, Short.MAX_VALUE)
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING))))
	                        .addGap(192, 192, 192)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGap(63, 63, 63)))
	                .addComponent(kiriomenu)
	                .addContainerGap())
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addComponent(promithia)
	                .addGap(237, 237, 237))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(promithia)
	                .addGap(22, 22, 22)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(marka)
	                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(montelo)
	                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(kostos2)
	                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(xrwma)
	                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(autonomia)
	                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(arkykloforias)
	                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(eidos)
	                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(17, 17, 17)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(typos)
	                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(epibates)
	                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(23, 23, 23)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                            .addComponent(kostos1)
	                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGap(18, 18, 18)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(kiriomenu)
	                    .addComponent(jButton2))
	                .addContainerGap())
	        );
	
	        pack();
	    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new MainMenuAdmin().setVisible(true);
        dispose();
    }

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		String eidos = jTextField6.getText();
		System.out.println(eidos);
		if (eidos.equals("car") || eidos.equals("Car")) {
			eidos = "amaxi";
		} else if (eidos.equals("motorbike") || eidos.equals("Motorbike") || eidos.equals("Motorcycle")) {
			eidos = "mixani";
		} else if (eidos.equals("bike") || eidos.equals("Bike") ) {
			eidos = "podilato";
		} else if (eidos.equals("Scooter")  || eidos.equals("scooter") ) {
			eidos = "patini";
		} else {
			JOptionPane.showMessageDialog(new JButton(), "Vehicle category has to be one of the following types:\nCar - Motorbike - Bike - Scooter");
			return;
		}
	    try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
	    Connection con = DriverManager.getConnection(
			        url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
	
	    String query = new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
	    				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
	    
	    PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    pst.setString(1, jTextField1.getText());
	    pst.setString(2, jTextField2.getText());
	    pst.setString(3, jTextField3.getText());
	    pst.setInt(4, Integer.parseInt(jTextField5.getText()));
	    pst.setString(5, jTextField4.getText());
	    pst.setString(6, eidos);
	    pst.setString(7, jTextField8.getText());
		pst.setInt(8, Integer.parseInt(jTextField7.getText()));
		pst.setInt(9, Integer.parseInt(jTextField13.getText()));
		pst.setInt(10, Integer.parseInt(jTextField11.getText()));
		pst.setBoolean(11, false);
		pst.setBoolean(12, true);
	
	    pst.executeUpdate();
	    JOptionPane.showMessageDialog(null, "Vehicle registered successfully!");
	    
        } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error registering user. Please check your input.");
        }
	}

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {

    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EisagwgiOxim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EisagwgiOxim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EisagwgiOxim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EisagwgiOxim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EisagwgiOxim().setVisible(true);
            }
        });
    }
    public String Douleia;

    private javax.swing.JButton kiriomenu;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel promithia;
    private javax.swing.JLabel kostos2;
    private javax.swing.JLabel marka;
    private javax.swing.JLabel kostos1;
    private javax.swing.JLabel montelo;
    private javax.swing.JLabel xrwma;
    private javax.swing.JLabel autonomia;
    private javax.swing.JLabel arkykloforias;
    private javax.swing.JLabel eidos;
    private javax.swing.JLabel typos;
    private javax.swing.JLabel epibates;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextPane jTextPane1;
}