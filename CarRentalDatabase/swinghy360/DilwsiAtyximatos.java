package swinghy360;

import javax.swing.*;

import DataBaseCreateAndTests.DataBase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DilwsiAtyximatos extends JFrame {
	JLabel topLabel;
    JLabel middleLabel;
    JTextArea bigTextField;
    JLabel belowBigTextFieldLabel;
    JTextField nonEditableTextField1;
    JLabel belowTextFieldsLabel;
    JButton submit;
    JButton back;
    Pelatis loggedIn; 
    Enoikiasi rental;
    Oxima current;
    Oxima next;
    int extraCost;
    
    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");
    
    public DilwsiAtyximatos(Pelatis p, Enoikiasi e) {
        loggedIn = p;
        rental = e;
        setTitle("Report accident");

        topLabel = new JLabel("Report accident", SwingConstants.CENTER);
        middleLabel = new JLabel("Accident description");
        bigTextField = new JTextArea();
        belowBigTextFieldLabel = new JLabel("Your car will be repllaced with:");
        nonEditableTextField1 = new JTextField();
        nonEditableTextField1.setEditable(false);
        belowTextFieldsLabel = new JLabel();
        
        if (rental.getInsurance()) {
        	belowTextFieldsLabel.setText("Insurance has been paid, no aditional cost.");
        	extraCost = 0;
        } else {
        	belowTextFieldsLabel.setText("Insurance has not been paid, aditional cost " + rental.getCost()*2 + "€");
        	extraCost = rental.getCost()*2;
        }
        
        submit = new JButton("Submit and replace");
        back = new JButton("Back");
        
        current = DataBase.getOximaByCid(e.getCid());
        System.out.println(current.getMarka());
        belowBigTextFieldLabel.setText("Your vehicle (" + current.getMarka() + " " +
        current.getMontelo() + ") will be replaced with:");
    	
        next = DataBase.getOximaByCategory(current.getCid(), current.getEidos());
        
        nonEditableTextField1.setText(next.getMarka() + " " + next.getMontelo());

        setLayout(new GridLayout(8, 1, 10, 10));

        add(topLabel);
        add(middleLabel);
        add(bigTextField);
        add(belowBigTextFieldLabel);
        add(nonEditableTextField1);
        add(belowTextFieldsLabel);
        add(submit);
        add(back);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					actionSubmitPerformed(e);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(loggedIn).setVisible(true);
                dispose();
            }
        });
    }
    
    private void actionSubmitPerformed(ActionEvent evt) throws SQLException, ClassNotFoundException {
    	// Update oxima
    	String updateQuery = "UPDATE oxima SET vlavi = true WHERE cid =?";
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
			pstmt.setInt(1, current.getCid());
	
            int rowsAffected = pstmt.executeUpdate();	
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
		
		// Update enoikiasi
		updateQuery = "UPDATE enoikiasi SET cid =" + next.getCid() + ", poso =" + (rental.getCost() + extraCost) + " WHERE cid =?";
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
        
		try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
			pstmt.setInt(1, current.getCid());
            int rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
		
		// Insert atyxima
		updateQuery = "INSERT INTO atyxima (imerominia, description, cid, pid) " +
		        "VALUES (?, ?, ?, ?)";
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
        
		try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
			pstmt.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			pstmt.setString(2, bigTextField.getText());
			pstmt.setInt(3, current.getCid());
			pstmt.setInt(4, loggedIn.getPid());
            pstmt.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
		
		this.dispose();
		new MainMenu(loggedIn).setVisible(true);
		JOptionPane.showMessageDialog(new JButton(), "Accident reported successfully.\nYour vehicle has been replaced.");
    }
}
