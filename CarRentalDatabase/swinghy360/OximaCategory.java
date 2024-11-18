package swinghy360;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class OximaCategory extends JFrame {
	
	public OximaCategory(Pelatis loggedIn) {
        setLayout(new GridLayout(2, 1));

        JButton button1 = new JButton("Cars");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ShowCars(loggedIn, "amaxi").setVisible(true);
                dispose();
            }
        });
        
        JButton button2 = new JButton("Motorcycles");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ShowCars(loggedIn, "mixani").setVisible(true);
                dispose();
            }
        });
        
        JButton button3 = new JButton("Bikes");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ShowCars(loggedIn, "podilato").setVisible(true);
                dispose();
            }
        });
        
        JButton button4 = new JButton("Scooters");
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ShowCars(loggedIn, "patini").setVisible(true);
                dispose();
            }
        });

        button1.setPreferredSize(new java.awt.Dimension(150, 75));
        button2.setPreferredSize(new java.awt.Dimension(150, 75));
        button3.setPreferredSize(new java.awt.Dimension(150, 75));
        button4.setPreferredSize(new java.awt.Dimension(150, 75));

        JPanel upperPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        upperPanel.add(button1);
        upperPanel.add(button2);
        upperPanel.add(button3);
        upperPanel.add(button4);

        JButton mainMenu = new JButton("main menu");

        mainMenu.setPreferredSize(new java.awt.Dimension(130, 60));
        mainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new MainMenu(loggedIn).setVisible(true);
                dispose();
            }
        });

        JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lowerPanel.add(mainMenu);

        add(upperPanel);
        add(lowerPanel);

        setTitle("Vehicle Categories");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
