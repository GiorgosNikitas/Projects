package swinghy360;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DataBaseCreateAndTests.DataBase;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class VehicleCondition extends JFrame {
	static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");
    static ArrayList<Integer> cidArray;
    static int index;
    
    Oxima oxima = null;
    int cid;
    JButton next;
    JButton prev;
    
    JPanel textFieldPanel;
    Pelatis loggedIn;
    String category;
    String vlaviValue;
    
    public VehicleCondition(String mode) {
    	category = mode;
    	cidArray = new ArrayList<>();
    	index = 0;
    	cid = 0;
    	
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                String query = "SELECT cid FROM oxima WHERE cid>" + cid  + " AND oxima.idos_oximatos = '" + category + "'";
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                    	while (rs.next()) {
                    		cidArray.add(rs.getInt("cid"));
                    		System.out.println("Cid: " + rs.getInt("cid"));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();  // Log the exception properly or display a user-friendly message
        }
    	
        // Set layout manager for the frame
        setLayout(new BorderLayout());
        
        oxima = getOximaByCid(cidArray.get(index));
        
        String labelNames[] = {"Brand", "Model", "Color", "Range", "Type", "Total Passengers"
                , "Condition", "Available"};

        String fieldNames[] = {oxima.getMarka(), oxima.getMontelo(), oxima.getXrwma(), String.valueOf(oxima.getAutonomia()),
                oxima.getTypos(), String.valueOf(oxima.getEpivates())};
        String vlaviValue = "N/A";
        Boolean isRented = false;
        String isRentedValue = "Rented";
        LocalDate today = LocalDate.now();
        String dateString = today.toString();
        
        isRented = DataBase.isAvailable(dateString, 0, oxima.getCid());
        System.out.println(isRented);
        if(isRented) {
        	isRentedValue = "Not Rented";
        }
        else {
        	isRentedValue = "Yes";
        }

        if (oxima.getVlavi()) {
            vlaviValue = "Damaged";
        } else {
            vlaviValue = "Excellent";
        }

        // Create panel for text fields and labels
        JPanel textFieldPanel = new JPanel(new GridLayout(9, 2, 10, 10)); // 9 rows, 2 columns, 10-pixel gaps
        for (int i = 0; i < 8; i++) {
            if (i < 6) {
                JTextField textField = new JTextField(fieldNames[i]);
                textField.setEditable(false);

                JLabel label = new JLabel(labelNames[i]);

                textFieldPanel.add(label);
                textFieldPanel.add(textField);
            } else {
                JLabel label;
                if (i == 6) {
                    label = new JLabel(vlaviValue);
                    JLabel label1 = new JLabel("Condition");
                    textFieldPanel.add(label1);
                } else {
                    label = new JLabel(isRentedValue);
                    JLabel label1 = new JLabel("Rented");
                    textFieldPanel.add(label1);
                }

                // Add the additional label
                textFieldPanel.add(label);
            }
        }


        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        prev = new JButton("<- Prev");
        if (cid == 1) {
        	prev.setEnabled(false);
        } else {
        	prev.setEnabled(true);
        }
        
        next = new JButton("Next ->");
        
        next.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionNextPerformed(evt);
			}
        });
        
        JButton back = new JButton("Back");
        back.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionBackPerformed(evt);
			}
        });
        
        buttonPanel.add(back);
        buttonPanel.add(prev);
        buttonPanel.add(next);

        // Add panels to the frame
        add(textFieldPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        setTitle("Cars");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
    
    public VehicleCondition(String mode, Oxima newOxima, int newCid) {
    	category = mode;
    	cid = newCid;
    	oxima = newOxima;
        
        String labelNames[] = {"Brand", "Model", "Color", "Range", "Type", "Total Passengers"
                , "Condition", "Available"};

        String fieldNames[] = {oxima.getMarka(), oxima.getMontelo(), oxima.getXrwma(), String.valueOf(oxima.getAutonomia()),
                oxima.getTypos(), String.valueOf(oxima.getEpivates())};
        String vlaviValue = "N/A";
        Boolean isRented = false;
        String isRentedValue = "Rented";
        LocalDate today = LocalDate.now();
        String dateString = today.toString();
        
        isRented = DataBase.isAvailable(dateString, 0, cid);
        System.out.println(isRented);
        if(isRented) {
        	isRentedValue = "Not Rented";
        }
        else {
        	isRentedValue = "Yes";
        }

        if (oxima.getVlavi()) {
            vlaviValue = "Damaged";
        } else {
            vlaviValue = "Excellent";
        }

        // Create panel for text fields and labels
        JPanel textFieldPanel = new JPanel(new GridLayout(9, 2, 10, 10)); // 9 rows, 2 columns, 10-pixel gaps
        for (int i = 0; i < 8; i++) {
            if (i < 6) {
                JTextField textField = new JTextField(fieldNames[i]);
                textField.setEditable(false);

                JLabel label = new JLabel(labelNames[i]);

                textFieldPanel.add(label);
                textFieldPanel.add(textField);
            } else {
                JLabel label;
                if (i == 6) {
                    label = new JLabel(vlaviValue);
                    JLabel label1 = new JLabel("Condition");
                    textFieldPanel.add(label1);
                } else {
                    label = new JLabel(isRentedValue);
                    JLabel label1 = new JLabel("Rented");
                    textFieldPanel.add(label1);
                }

                // Add the additional label
                textFieldPanel.add(label);
            }
        }

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton prev = new JButton("<- Prev");
        prev.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPrevPerformed(evt);
			}
        });
        
        JButton next = new JButton("Next ->");
        if (cid == cidArray.get(cidArray.size() - 1)) {
        	next.setEnabled(false);
        } else {
        	next.setEnabled(true);
        }
        next.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionNextPerformed(evt);
			}
        });
        
        JButton back = new JButton("Back");
        back.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionBackPerformed(evt);
			}
        });
        
        
        if (cid == cidArray.get(0)) {
        	prev.setEnabled(false);
        } else {
        	prev.setEnabled(true);
        }
        
       
        buttonPanel.add(back);
        buttonPanel.add(prev);
        buttonPanel.add(next);
        

        // Add panels to the frame
        add(textFieldPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        setTitle("Cars");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    	
    }
    
    public void actionNextPerformed(java.awt.event.ActionEvent evt) {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
            	
            	index++;
                String query = "SELECT * FROM oxima WHERE cid=" + cidArray.get(index) + " AND idos_oximatos = '" + category + "'";
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) {
                        	oxima = createOximaFromResultSet(rs);
                        	cid = oxima.getCid();
                        	new VehicleCondition(category, oxima, cid).setVisible(true);
//                        	new MainMenu(loggedInPelatis).setVisible(true);
                            dispose();
//                            JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
                        } else {
                            
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();  // Log the exception properly or display a user-friendly message
        }
    }
    
    public void actionPrevPerformed(java.awt.event.ActionEvent evt) {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
            	
            	index--;
                String query = "SELECT * FROM oxima WHERE cid=" + cidArray.get(index) + " AND idos_oximatos = '" + category + "'";
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) {
                        	oxima = createOximaFromResultSet(rs);
                        	cid = oxima.getCid();
                        	new VehicleCondition(category, oxima, cid).setVisible(true);
                            dispose();
//                            JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
                        } else {
                            
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();  // Log the exception properly or display a user-friendly message
        }
    }
    
    Oxima getOximaByCid(int id) {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                String query = "SELECT * FROM oxima WHERE cid=" + id + " AND idos_oximatos = '" + category + "'";
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) {
                        	return createOximaFromResultSet(rs);
                        } else {
                            return null;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();  // Log the exception properly or display a user-friendly message
        }
    	return null;
    }
    
    private void actionBackPerformed(ActionEvent evt) {
		new ShowVehicles().setVisible(true);
		dispose();
		
	}
    
    private Oxima createOximaFromResultSet(ResultSet rs) throws SQLException {
        return new Oxima(
        	rs.getInt("cid"),
            rs.getString("marka"),
            rs.getString("montelo"),
            rs.getString("xrwma"),
            rs.getInt("autonomia"),
            rs.getString("arithmo_kukloforias"),
            rs.getString("idos_oximatos"),
            rs.getString("tupos_oximatos"),
            rs.getInt("arithmos_epivatwn"),
            rs.getInt("kostos_enoikiasis"),
            rs.getInt("kostos_asfalias"),
            rs.getBoolean("vlavi"),
            rs.getBoolean("available")
        );
    }

    public static void main(String[] args) {
        // Create an instance of the TextFieldFrame class
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VehicleCondition("amaxi");
            }
        });
    }
}
