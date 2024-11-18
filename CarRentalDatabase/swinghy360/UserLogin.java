package swinghy360;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UserLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserLogin frame = new UserLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private Pelatis createPelatisFromResultSet(ResultSet rs) throws SQLException {
        return new Pelatis(
        	rs.getInt("pid"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("dieuthinsi_spitiou"),
            rs.getDate("imerominia_genisis"),
            rs.getInt("age"),
            rs.getInt("adia_odigisis"),
            rs.getInt("card_number"),
            rs.getBoolean("is_admin")
        );
    }

    /**
     * Create the frame.
     */
    public UserLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Email");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(545, 392, 162, 73);
        
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        registerButton.setBounds(350, 392, 162, 73);
        
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String email = textField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password_ = String.valueOf(passwordChars);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    try (Connection con = DriverManager.getConnection(
                        url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                        String query = "SELECT * FROM pelatis WHERE email=? AND password=?";
                        try (PreparedStatement st = con.prepareStatement(query)) {
                            st.setString(1, email);
                            st.setString(2, password_);
                            
                            try (ResultSet rs = st.executeQuery()) {
                                if (rs.next()) {
                                	Pelatis loggedInPelatis = createPelatisFromResultSet(rs);
                                    if (loggedInPelatis.isAdmin()) {
                                        // Open admin menu
                                        new MainMenuAdmin().setVisible(true);
                                    } else {
                                        // Open regular menu
                                        new MainMenu(loggedInPelatis).setVisible(true);
                                    }
                                    dispose();
                                    JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
                                } else {
                                    JOptionPane.showMessageDialog(btnNewButton, "Wrong Username & Password");
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	new EisagwgiPelati().setVisible(true);
            }
        });

        contentPane.add(btnNewButton);
        contentPane.add(registerButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }
}