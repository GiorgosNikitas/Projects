package model.Position;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Player.Player;

public class EventDice extends JFrame{
	private static final int EXIT_ON_CLOSE = 1;
	private JFrame frame;
	private JButton dice;
	private JPanel panel;
	public boolean enableDice = true;
	public ArrayList<Integer> diceThrows = new ArrayList<Integer>();
	public static Object o = new Object();
 	
	public EventDice(Object o) {
		this.o = o;
		this.setPreferredSize(new Dimension(200, 200));
		this.setBounds(400, 400, 200, 200);
		this.setLayout(null);
		createDice();
		this.add(panel);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void createDice() {
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(100, 100));
		panel.setLayout(null);
		panel.setBounds(0, 0, 200, 200);
		panel.setBackground(new Color(0, 150, 0));
		
		JLabel label = new JLabel("Roll the Event Dice");
		label.setBounds(40, 10, 150, 20);
		panel.add(label);
		
		Image img = null;
		try {
			img = ImageIO.read(new File("images/dice-1.jpg"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		img = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		dice = new JButton(new ImageIcon(img));
		dice.setBounds(45, 50, 100, 100);
		dice.setVisible(true);
		panel.add(dice);
		panel.setVisible(true);
		dice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random();
				int diceNumber = rand.nextInt(6) + 1;
				diceThrows.add(diceNumber);
				try {
					dice.setIcon(new ImageIcon(diceSide(diceNumber)));
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/diceRoll.wav"));
					Clip clip = AudioSystem.getClip();
		            clip.open(audioInputStream);
		            clip.loop(0);
		            clip.start();
				} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
				synchronized(o) {
					o.notify();
				}
			} 
	    });
	}
	
	public Image diceSide(int x) throws IOException {
		Image img; 
    	String imgName;
		switch (x) {
			case 1:
				imgName = "images/dice-1.jpg";
				break;
			case 2:
				imgName = "images/dice-2.jpg";
				break;
			case 3:
				imgName = "images/dice-3.jpg";
				break;
			case 4:
				imgName = "images/dice-4.jpg";
				break;
			case 5:
				imgName = "images/dice-5.jpg";
				break;
			default:
				imgName = "images/dice-6.jpg";
				break;
		}
		img = ImageIO.read(new File(imgName));
		img = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		return img;
	}
}
