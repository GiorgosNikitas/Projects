package swinghy360;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import DataBaseCreateAndTests.DataBase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Rent extends JFrame {
    private JTextField date;
    private JTextField days;
    private JTextField dynamicTextField;
    private Pelatis loggedIn;
    private Oxima oxima;
    private JTextField checkBoxTextField1;
    JCheckBox checkBox1;
    JCheckBox checkBox2;
    JLabel cost; 
    private int payment;
    private boolean paymentFlag = false;
    
    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");

    public Rent(Pelatis p, Oxima current) {
    	payment = 0;
    	loggedIn = p;
    	oxima = current;
    	cost = new JLabel();
        setLayout(new GridLayout(9, 2, 10, 10));

        JLabel topLabel = new JLabel(current.getMarka() + " " + current.getMontelo(), SwingConstants.CENTER);
        add(topLabel);
        add(new JLabel());

        JLabel label1 = new JLabel("Rental date *");
        JLabel label2 = new JLabel("Total days *");

        date = new JTextField();
        add(createPanel(label1, date));

        days = new JTextField();
        add(createPanel(label2, days));

        checkBox1 = new JCheckBox("Will you not be the driver?");
        JLabel labelCheck = new JLabel("Enter other's driver licence number");
        labelCheck.setVisible(false);
        checkBoxTextField1 = new JTextField();
        checkBoxTextField1.setVisible(false);
        
        JButton button1 = new JButton("Pay");

        checkBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBoxTextField1.setVisible(checkBox1.isSelected());
                labelCheck.setVisible(checkBox1.isSelected());
                revalidate();
                repaint();
            }
        });        

        add(createPanel(checkBox1, checkBoxTextField1));
        
        checkBox2 = new JCheckBox("Pay insurance");
        checkBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!paymentFlag)
                    payment += oxima.getKostos_asf();
                else
                    payment -= oxima.getKostos_asf();
                button1.setText("Pay " + payment + "");
                paymentFlag = !paymentFlag;
                revalidate();
                repaint();
            }
        });

        dynamicTextField = new JTextField();
        dynamicTextField.setVisible(false);

        add(createPanel(checkBox2, dynamicTextField));
        
        button1.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					actionPayPerformed(evt);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
        });
        
        days.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextChange();
            }

            private void handleTextChange() {
                payment = oxima.getKostos_en() * Integer.parseInt(days.getText());
                button1.setText("Pay " + payment + "€");
                revalidate();
                repaint();
            }
        });
        
    
        JButton back = new JButton("Back");
        back.addActionListener(new java.awt.event.ActionListener() { 
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionBackPerformed(evt);
			}
        });

        add(button1);
        add(back);

        setTitle("Rent");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void actionBackPerformed(ActionEvent evt) {
		new ShowCars(loggedIn, oxima.getEidos()).setVisible(true);
		dispose();
		
	}
    
    private boolean check() {
    	if (oxima.getEidos() == "amaxi" || oxima.getEidos() == "mixani") {
    		if (loggedIn.getAge() < 18) {
    			JOptionPane.showMessageDialog(new JButton(), "Failed. You have to be over 18 years old.");
    			return false;
    		} 
    		
    		if (loggedIn.getAdiaOdigisis() == 0 && dynamicTextField.getText().isEmpty()) {
    			JOptionPane.showMessageDialog(new JButton(), "Failed. This type of vehicle requires driving license.");
    			return false;
    		}
    	} else {
    		if (loggedIn.getAge() < 16) {
    			JOptionPane.showMessageDialog(new JButton(), "Failed. You have to be over 16 years old.");
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    private void actionPayPerformed(ActionEvent evt) throws SQLException, ClassNotFoundException {
    	if (!check()) {
    		return;
    	}
    	
    	if (date.getText().isEmpty() || days.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(new JButton(), "Fill the fields with *");
    		return;
    	}
    	if (!DataBase.isAvailable(LocalDate.parse(date.getText(), DateTimeFormatter.ofPattern("yyyy-M-d")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Integer.parseInt(days.getText()), oxima.getCid())) {
    		JOptionPane.showMessageDialog(new JButton(), "Vehicle is not available for the selected date");
    		return;
    	}
    	
    	DataBase.isAvailable(date.getText(), Integer.parseInt(days.getText()), oxima.getCid());
    	
		Class.forName("com.mysql.cj.jdbc.Driver");
		payment = oxima.getKostos_en() * Integer.parseInt(days.getText());
		Connection con = DriverManager.getConnection(
		url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
		
		String insertUserQuery = "INSERT INTO enoikiasi (imerominia_enoikiasis, diarkia_enoikiasis, aithmos_diplwmatos, poso, asfaleia, cid, pid) " +
		        "VALUES (?, ?, ?, ?, ?, ?, ?)";
	
		try (PreparedStatement pstmt = con.prepareStatement(insertUserQuery)) {
			pstmt.setString(1, date.getText());
			pstmt.setInt(2, Integer.parseInt(days.getText()));
			pstmt.setString(3, checkBoxTextField1.getText());
			pstmt.setInt(4, payment);
			pstmt.setBoolean(5, checkBox2.isSelected());
			pstmt.setInt(6, oxima.getCid());
			pstmt.setInt(7, loggedIn.getPid());
			
			pstmt.executeUpdate();
			this.dispose();
			new MainMenu(loggedIn).setVisible(true);
			JOptionPane.showMessageDialog(new JButton(), "Rental completed successfully");
		} finally {
			con.close();
		}
		
	}

    private JPanel createPanel(JComponent component1, JComponent component2) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component1, BorderLayout.WEST);
        panel.add(component2, BorderLayout.CENTER);
        return panel;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new Rent(null, null));
//    }
}
