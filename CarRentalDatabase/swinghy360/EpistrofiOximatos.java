package swinghy360;

import javax.swing.*;

import DataBaseCreateAndTests.DataBase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class EpistrofiOximatos extends JFrame {
	JLabel topLabel;
	JLabel middleLabel1;
	JLabel middleLabel2;
	JButton submit;
	JButton back;
	
	Pelatis loggedIn;
	Enoikiasi rental;
	Oxima oxima;
	
	int extraCost;
	
	static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");
	
    public EpistrofiOximatos(Pelatis p) {
    	loggedIn = p;
        setTitle("Return Vehicle");

        topLabel = new JLabel("Return your vehicle", SwingConstants.CENTER);
        middleLabel1 = new JLabel("Middle Label 1", SwingConstants.CENTER);
        middleLabel2 = new JLabel("Middle Label 2", SwingConstants.CENTER);
        submit = new JButton("Submit");
        back = new JButton("Back");
        
        rental = DataBase.getEnoikiasiFromPid(loggedIn.getPid());
        
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        
        if(currentHour > 12) {
        	middleLabel2.setText("You are about to return your vehicle " + (currentHour - 12) + " hours late. Aditional cost: " + (currentHour - 12)*5);
        	extraCost = (currentHour - 12)*5;
        } else {
        	middleLabel2.setText("You are about to return your vehicle in time. No aditional cost.");
        	extraCost = 0;
        }
        
        oxima = DataBase.getOximaByCid(rental.getCid());
        
        middleLabel1.setText("Your vehicle: " + oxima.getMarka() + " " + oxima.getMontelo());

        setLayout(new GridLayout(5, 1, 10, 10));
        
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	actionSubmitPerformed(evt);
            }
        });
        
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new MainMenu(loggedIn).setVisible(true);
                dispose();
            }
        });

        add(topLabel);
        add(middleLabel1);
        add(middleLabel2);
        add(submit);
        add(back);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }
    
    private void actionSubmitPerformed(ActionEvent evt) {
    	String updateQuery = "UPDATE enoikiasi SET poso = " + (rental.getCost() + extraCost) + " WHERE eid =?";
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
        
		Connection con = null;
		try {
			con = DriverManager.getConnection(
			url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
			pstmt.setInt(1, rental.getEid());
	
            pstmt.executeUpdate();	
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
		
		dispose();
		new MainMenu(loggedIn).setVisible(true);
		JOptionPane.showMessageDialog(new JButton(), "Vehicle returned successfully.\nThank you");
	}
}
