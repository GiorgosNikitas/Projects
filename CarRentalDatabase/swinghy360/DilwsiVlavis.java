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

public class DilwsiVlavis extends JFrame {
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
    
    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");
    
    public DilwsiVlavis(Pelatis p, Enoikiasi e) {
        loggedIn = p;
        rental = e;
        setTitle("Report Malfunction");

        topLabel = new JLabel("Malfunction", SwingConstants.CENTER);
        middleLabel = new JLabel("Malfunction description");
        bigTextField = new JTextArea();
        belowBigTextFieldLabel = new JLabel("Your car will be repllaced with:");
        nonEditableTextField1 = new JTextField();
        nonEditableTextField1.setEditable(false);
        belowTextFieldsLabel = new JLabel();
        
        submit = new JButton("Submit and replace");
        back = new JButton("Back");
        
        current = DataBase.getOximaByCid(e.getCid());
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

            if (rowsAffected > 0) {
                System.out.println("Update successful. Rows affected: " + rowsAffected);
            } else {
                System.out.println("No rows updated.");
            }
	
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
		
		updateQuery = "UPDATE enoikiasi SET cid =" + next.getCid() + " WHERE cid =?";
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
        
		
		try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
			pstmt.setInt(1, current.getCid());
	
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Update successful. Rows affected: " + rowsAffected);
            } else {
                System.out.println("No rows updated.");
            }
	
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
		
		this.dispose();
		new MainMenu(loggedIn).setVisible(true);
		JOptionPane.showMessageDialog(new JButton(), "Malfunction reported successfully.\nYour vehicle has been replaced.");
    }
}