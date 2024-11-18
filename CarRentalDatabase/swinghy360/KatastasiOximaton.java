package swinghy360;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KatastasiOximaton extends javax.swing.JFrame {

    static String url = "jdbc:mysql://localhost";
    static String databaseName = "project1";
    static int port = 3306;
    static String username = "root";
    static String password = "";
    private JButton episkeuiButton;
    JButton mainMenuButton;

    private JTable jTable;

    public KatastasiOximaton() {
        displayTable();
    }
    
    private void openMainMenuAdmin() {
        new MainMenuAdmin().setVisible(true);
        dispose();
    }

    private void displayTable() {
    	mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMainMenuAdmin();
            }
        });
        
    	
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);

            String selectQuery = "SELECT * FROM oxima";
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectQuery);
            String[] columnNames = {"Marka", "Montelo", "idos_oximatos", "Episkeui"};
            int rowCount = 0;
            Object[][] data = new Object[10][columnNames.length]; // Initial size, adjust as needed

            JFrame frame = new JFrame("Oxima Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(0, columnNames.length, 10, 10));

            while (resultSet.next()) {
                if (rowCount == data.length) {
                    // Resize the array if necessary
                    data = resizeArray(data, 2 * data.length);
                }
                int cid = resultSet.getInt("cid");
                String marka = resultSet.getString("marka");
                String montelo = resultSet.getString("montelo");
                Boolean vlavi = resultSet.getBoolean("vlavi");

                JButton episkeuiButton = new JButton("Episkeui");

                data[rowCount] = new Object[]{marka, montelo, vlavi, episkeuiButton, rowCount}; // Store the row information
                rowCount++;
                panel.add(mainMenuButton);

                // Add labels and button to the panel for each row
                for (int i = 0; i < columnNames.length; i++) {
                    if (i < 3) {
                        JLabel label = new JLabel(data[rowCount - 1][i].toString());
                        panel.add(label);
                    } else {
                        JButton button = (JButton) data[rowCount - 1][i];
                        panel.add(button);
                        if (!vlavi) {
                            episkeuiButton.setVisible(false);
                        }
                        // Set the row information as the action command for the button
                        button.setActionCommand(String.valueOf(rowCount - 1));
                        Object[][] finalData = data;

                        button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // Retrieve the row information from the action command
                                int clickedRow = Integer.parseInt(((AbstractButton) e.getSource()).getActionCommand());
                                JLabel labelToUpdate = (JLabel) panel.getComponent(clickedRow * columnNames.length + 2);
                                labelToUpdate.setText("false");
                                

                                // Perform update operation in the database
                                try (Connection con = DriverManager.getConnection(
                                        url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
                                    String updateQuery = "UPDATE oxima SET vlavi = 0 WHERE cid = ?";
                                    try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                                        pstmt.setInt(1, cid);
                                        pstmt.executeUpdate();
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                                episkeuiButton.setVisible(false);
                            }
                        });
                    }
                }
            }

            // Trim the array to the actual size
            data = resizeArray(data, rowCount);

            
            frame.add(panel, BorderLayout.CENTER); // Centered panel
            frame.add(mainMenuButton, BorderLayout.SOUTH);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null); // Center the frame
            frame.setVisible(true);

            resultSet.close();
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Unable to fetch data from the database.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Object[][] resizeArray(Object[][] array, int newSize) {
        Object[][] newArray = new Object[newSize][array[0].length];
        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, newSize));
        return newArray;
    }

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new KatastasiOximaton().setVisible(true);
//            }
//        });
//    }
}