package swinghy360;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Esoda extends JFrame {
	 JLabel topLabel;
     JTextField textField;
     JButton ok;
     JButton back;

     JLabel label1;
     JLabel label2;
     JLabel label3;
     JLabel label4;
     
     static String url = new String("jdbc:mysql://localhost");
     static String databaseName = new String("project1");
     static int port = 3306;
     static String username = new String("root");
     static String password = new String("");
     
     int sum;
	
     public Esoda() {
		setTitle("Income");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        back = new JButton("back");
        back.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new MainMenuAdmin().setVisible(true);
				dispose();
			}
        });
        topLabel = new JLabel("Enter year:");
        textField = new JTextField(15);
        ok = new JButton("OK");
        ok.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					actionOkPerformed(evt);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
        });

        label1 = new JLabel("Cars total income: ");
        
        label2 = new JLabel("Motorbikes total income: ");
        label3 = new JLabel("Bikes total income: ");
        label4 = new JLabel("Scooters total income: ");

        setLayout(new GridLayout(6, 1, 10, 10));

        JPanel topRowPanel = new JPanel(new FlowLayout());
        topRowPanel.add(topLabel);
        topRowPanel.add(textField);
        topRowPanel.add(ok);

        add(topRowPanel);
        add(label1);
        add(label2);
        add(label3);
        add(label4);
        add(back);

        setSize(400, 300);

        setLocationRelativeTo(null);

        setVisible(true);
	}
     
     void getTotalCostByCategory(String category, int year) {
		sum = 0;
		try {
	         Class.forName("com.mysql.cj.jdbc.Driver");
			 try (Connection con = DriverManager.getConnection(
			     url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
			
			     String query = "SELECT enoikiasi.poso, enoikiasi.imerominia_enoikiasis FROM enoikiasi JOIN oxima ON enoikiasi.cid = oxima.cid WHERE oxima.idos_oximatos ='" + category + "'";
			     try (PreparedStatement st = con.prepareStatement(query)) {  
			         try (ResultSet rs = st.executeQuery()) {
			         	while (rs.next()) {
			         		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			         		try {
								java.util.Date date = sdf.parse(rs.getString("imerominia_enoikiasis"));
								int yearRental = getYearFromDate(date);
								if (year == yearRental) {
									sum += rs.getInt("poso");
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
			         
			            }
			         }
			     }
			 }
	     } catch (ClassNotFoundException | SQLException ex) {
	         ex.printStackTrace();
	     }
	}
     
     private  int getYearFromDate(java.util.Date date) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);

         return calendar.get(Calendar.YEAR);
     }
     
	 private void actionOkPerformed(ActionEvent evt) throws SQLException, ClassNotFoundException {
		 int year = Integer.parseInt(textField.getText());
		 getTotalCostByCategory("amaxi", year);
		 label1.setText("Cars total income: ");
		 label1.setText(label1.getText() + sum);
		 
		 getTotalCostByCategory("mixani", year);
		 label2.setText("Motorbikes total income: ");
		 label2.setText(label2.getText() + sum);
		 
		 getTotalCostByCategory("podilato", year);
		 label3.setText("Bikes total income: ");
		 label3.setText(label3.getText() + sum);
		 
		 getTotalCostByCategory("patini", year);
		 label4.setText("Scooters total income: ");
		 label4.setText(label4.getText() + sum);
		 
		 revalidate();
		 repaint();
	 }
     
    public static void main(String[] args) {
    	new Esoda();
    }
}
