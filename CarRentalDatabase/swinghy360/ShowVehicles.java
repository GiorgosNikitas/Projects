package swinghy360;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class ShowVehicles extends JFrame {
	
	public ShowVehicles() {
        // Set the layout manager for the frame
        setLayout(new GridLayout(2, 1)); // 2 rows, 1 column

        // Create buttons for the upper panel
        JButton button1 = new JButton("Cars");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new VehicleCondition("amaxi").setVisible(true);
                dispose();
            }
        });
        
        JButton button2 = new JButton("Motorcycles");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new VehicleCondition("mixani").setVisible(true);
                dispose();
            }
        });
        
        JButton button3 = new JButton("Bikes");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new VehicleCondition("podilato").setVisible(true);
                dispose();
            }
        });
        
        JButton button4 = new JButton("Scooters");
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new VehicleCondition("patini").setVisible(true);
                dispose();
            }
        });

        // Set a larger size for the upper buttons
        button1.setPreferredSize(new java.awt.Dimension(150, 75));
        button2.setPreferredSize(new java.awt.Dimension(150, 75));
        button3.setPreferredSize(new java.awt.Dimension(150, 75));
        button4.setPreferredSize(new java.awt.Dimension(150, 75));

        // Create the upper panel with GridLayout and gaps
        JPanel upperPanel = new JPanel(new GridLayout(2, 2, 30, 30)); // 10 pixels horizontal and vertical gap
        upperPanel.add(button1);
        upperPanel.add(button2);
        upperPanel.add(button3);
        upperPanel.add(button4);

        // Create a button for the lower panel
        JButton mainMenu = new JButton("main menu");

        // Set a larger size for the lower button
        mainMenu.setPreferredSize(new java.awt.Dimension(130, 60));
        mainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new MainMenuAdmin().setVisible(true);
                dispose();
            }
        });

        // Create the lower panel with FlowLayout to center the button
        JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lowerPanel.add(mainMenu);

        // Add panels to the frame
        add(upperPanel);
        add(lowerPanel);

        // Set frame properties
        setTitle("Vehicle Categories");
        setSize(400, 350); // Adjusted size to accommodate gaps and additional button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}