package swinghy360;

import javax.swing.*;

import DataBaseCreateAndTests.DataBase;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaxMinAverage extends JFrame {
	JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    
    JLabel label5;
    JLabel label6;
    JLabel label7;
    JLabel label8;
    
    JLabel label9;
    JLabel label10;
    JLabel label11;
    JLabel label12;
    
    JLabel label13;
    JLabel label14;
    JLabel label15;
    JLabel label16;
    
    JLabel v1;
    JLabel v2;
    JLabel v3;
    JLabel v4;
    
    JButton back;
    
    ArrayList<Integer> duration;
    ArrayList<Integer> cids;
    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");
    
	public MaxMinAverage() {
		Oxima temp;
		duration = new ArrayList<>();
		cids = new ArrayList<>();
		
		back = new JButton("back");
        back.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new MainMenuAdmin().setVisible(true);
				dispose();
			}
        });
		
        setTitle("Max-Min-Average Rental");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int max = 0;
        int min = 10000;
        float average = 0;
        // For cars
        label1 = new JLabel("Cars");
        label2 = new JLabel("Min: ");
        label3 = new JLabel("Max: ");
        label4 = new JLabel("Average: ");
        getDurationByCategory("amaxi");
        for (int i = 0; i < duration.size(); i++) {
        	if (duration.get(i) > max) {
        		max = duration.get(i);
        	}
        	
        	if (duration.get(i) < min) {
        		min = duration.get(i);
        	}
        	
        	average += duration.get(i);
        }
        if (min == 10000) { min = 0; }
        if (average != 0) {
        	average = average/duration.size();
        }
        label2.setText(label2.getText() + min + " days");
        label3.setText(label3.getText() + max + " days");
        label4.setText(label4.getText() + average + " days");
        getCidByCategory("amaxi");
        int maxCid = 0;
        max = 0;
        for (int i = 0; i < cids.size(); i++) {
        	int count = 0;
        	for (int j = 0; j < cids.size(); j++) {
        		if (cids.get(j) == cids.get(i)) {
        			count++;
        		}
        	}
        	if (count > max) {
        		max = count;
        		maxCid = cids.get(i);
        	}
        }
        if (maxCid != 0) {
        	temp = DataBase.getOximaByCid(maxCid);
        	v1 = new JLabel("Most Popular: " + temp.getMarka() + " " + temp.getMontelo());
        } else {
        	v1 = new JLabel("Most Popular: -");
        }
        
        // For motorbikes
        max = 0;
        min = 10000;
        average = 0;
        
        label5 = new JLabel("Motorbikes");
        label6 = new JLabel("Min: ");
        label7 = new JLabel("Max: ");
        label8 = new JLabel("Average: ");
        getDurationByCategory("mixani");
        for (int i = 0; i < duration.size(); i++) {
        	if (duration.get(i) > max) {
        		max = duration.get(i);
        	}
        	
        	if (duration.get(i) < min) {
        		min = duration.get(i);
        	}
        	
        	average += duration.get(i);
        }
        if (min == 10000) { min = 0; }
        if (average != 0) {
        	average = average/duration.size();
        }
        label6.setText(label6.getText() + min + " days");
        label7.setText(label7.getText() + max + " days");
        label8.setText(label8.getText() + average + " days");
        getCidByCategory("mixani");
        maxCid = 0;
        max = 0;
        for (int i = 0; i < cids.size(); i++) {
        	int count = 0;
        	for (int j = 0; j < cids.size(); j++) {
        		if (cids.get(j) == cids.get(i)) {
        			count++;
        		}
        	}
        	if (count > max) {
        		max = count;
        		maxCid = cids.get(i);
        	}
        }
        if (maxCid != 0) {
        	temp = DataBase.getOximaByCid(maxCid);
        	v2 = new JLabel("Most Popular: " + temp.getMarka() + " " + temp.getMontelo());
        } else {
        	v2 = new JLabel("Most Popular: -");
        }
        
        
        // For Bikes
        max = 0;
        min = 10000;
        average = 0;
        
        label9 = new JLabel("Bikes");
        label10 = new JLabel("Min: ");
        label11 = new JLabel("Max: ");
        label12 = new JLabel("Average: ");
        getDurationByCategory("podilato");
        for (int i = 0; i < duration.size(); i++) {
        	if (duration.get(i) > max) {
        		max = duration.get(i);
        	}
        	
        	if (duration.get(i) < min) {
        		min = duration.get(i);
        	}
        	
        	average += duration.get(i);
        }
        if (min == 10000) { min = 0; }
        if (average != 0) {
        	average = average/duration.size();
        }
        label10.setText(label10.getText() + min + " days");
        label11.setText(label11.getText() + max + " days");
        label12.setText(label12.getText() + average + " days");
        getCidByCategory("podilato");
        maxCid = 0;
        max = 0;
        for (int i = 0; i < cids.size(); i++) {
        	int count = 0;
        	for (int j = 0; j < cids.size(); j++) {
        		if (cids.get(j) == cids.get(i)) {
        			count++;
        		}
        	}
        	if (count > max) {
        		max = count;
        		maxCid = cids.get(i);
        	}
        }
        if (maxCid != 0) {
        	temp = DataBase.getOximaByCid(maxCid);
        	v3 = new JLabel("Most Popular: " + temp.getMarka() + " " + temp.getMontelo());
        } else {
        	v3 = new JLabel("Most Popular: -");
        }
        
        // For Scooters
        max = 0;
        min = 10000;
        average = 0;
        
        label13 = new JLabel("Scooters");
        label14 = new JLabel("Min: ");
        label15 = new JLabel("Max: ");
        label16 = new JLabel("Average: ");
        getDurationByCategory("patini");
        for (int i = 0; i < duration.size(); i++) {
        	if (duration.get(i) > max) {
        		max = duration.get(i);
        	}
        	
        	if (duration.get(i) < min) {
        		min = duration.get(i);
        	}
        	
        	average += duration.get(i);
        }
        if (min == 10000) { min = 0; }
        if (average != 0) {
        	average = average/duration.size();
        }
        label14.setText(label14.getText() + min + " days");
        label15.setText(label15.getText() + max + " days");
        label16.setText(label16.getText() + average + " days");
        getCidByCategory("patini");
        maxCid = 0;
        max = 0;
        for (int i = 0; i < cids.size(); i++) {
        	int count = 0;
        	for (int j = 0; j < cids.size(); j++) {
        		if (cids.get(j) == cids.get(i)) {
        			count++;
        		}
        	}
        	if (count > max) {
        		max = count;
        		maxCid = cids.get(i);
        	}
        }
        if (maxCid != 0) {
        	temp = DataBase.getOximaByCid(maxCid);
        	v4 = new JLabel("Most Popular: " + temp.getMarka() + " " + temp.getMontelo());
        } else {
        	v4 = new JLabel("Most Popular: -");
        }

        setLayout(new GridLayout(25, 1));

        add(label1);
        add(label2);
        add(label3);
        add(label4);
        add(v1);
        add(new JLabel());

        add(label5);
        add(label6);
        add(label7);
        add(label8);
        add(v2);
        add(new JLabel());
        
        add(label9);
        add(label10);
        add(label11);
        add(label12);
        add(v3);
        add(new JLabel());
        
        add(label13);
        add(label14);
        add(label15);
        add(label16);
        add(v4);
        add(new JLabel());
        add(back);

        setSize(400, 600);

        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	void getDurationByCategory(String category) {
		duration.clear();
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                String query = "SELECT enoikiasi.diarkia_enoikiasis FROM enoikiasi JOIN oxima ON enoikiasi.cid = oxima.cid WHERE oxima.idos_oximatos ='" + category + "'";
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                    	while (rs.next()) {
                    		duration.add(rs.getInt("diarkia_enoikiasis"));
                    		System.out.println("Cid: " + rs.getInt("diarkia_enoikiasis"));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	void getCidByCategory(String category) {
		cids.clear();
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                String query = "SELECT enoikiasi.cid FROM enoikiasi JOIN oxima ON enoikiasi.cid = oxima.cid WHERE oxima.idos_oximatos ='" + category + "'";
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                    	while (rs.next()) {
                    		cids.add(rs.getInt("cid"));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
	}
	
    public static void main(String[] args) {
        new MaxMinAverage();
    }
}
