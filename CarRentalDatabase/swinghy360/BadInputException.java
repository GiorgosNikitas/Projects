/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swinghy360;

import javax.swing.JOptionPane;

/**
 *
 * @author GioChr
 */
public class BadInputException extends Exception {

    public BadInputException(String message) {
        JOptionPane.showMessageDialog(null, message);

    }
}
