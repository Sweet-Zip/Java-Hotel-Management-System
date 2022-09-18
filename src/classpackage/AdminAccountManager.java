/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classpackage;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import jpanelpackage.forgetPasswordJPanel;
import jframepackage.Login_Register;
import jframepackage.MainDashboard;

/**
 *
 * @author Desktop-Desk
 */
public class AdminAccountManager {

    private String query;
    private PreparedStatement pst;
    private Connection con;
    private ResultSet resultSet;
    private static final int PASSWORD_LENGTH = 8;
    private String password, securityQuestion, securityAnswer, address, username, email;
    private int ID;
    private String firstName, lastName, nationality, identityCard;
    private long phoneNumber, nationalID;
    ConnectionProvider connectionProvider = new ConnectionProvider();

    public void register(JTextField registerEmailField, JTextField registerFirstNameField, JTextField registerLastNameTextField,
            JTextField registerUsernameField, JTextField registerPhoneNumberTextField, JTextField registerNationalityTextField,
            JComboBox registerIdentityCardOptionComboBox, JTextField registerIdentityIDTextField, JTextField registerPasswordField,
            JTextField registerAddress, JComboBox registerSecurityQuestionComboBox, JTextField registerAnswerField) {

        query = "INSERT INTO `user_login`(`email`, `firstname`, `lastname`, `username`, `phone_number`, `nationality`, `identity_card`, `national_id`, `password`, `address`, `security_question`, `security_answer`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        email = registerEmailField.getText();
        firstName = registerFirstNameField.getText();
        lastName = registerLastNameTextField.getText();
        username = registerUsernameField.getText();
        phoneNumber = Long.parseLong(registerPhoneNumberTextField.getText());
        identityCard = registerIdentityCardOptionComboBox.getSelectedItem().toString();
        nationalID = Long.parseLong(registerIdentityIDTextField.getText());
        nationality = registerNationalityTextField.getText();
        password = registerPasswordField.getText();
        address = registerAddress.getText();
        securityQuestion = registerSecurityQuestionComboBox.getSelectedItem().toString();
        securityAnswer = registerAnswerField.getText();

        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, username);
            pst.setLong(5, phoneNumber);
            pst.setString(6, nationality);
            pst.setString(7, identityCard);
            pst.setLong(8, nationalID);
            pst.setString(9, password);
            pst.setString(10, address);
            pst.setString(11, securityQuestion);
            pst.setString(12, securityAnswer);
            pst.executeUpdate();
            pst.close();
            resultSet.close();
            JOptionPane.showMessageDialog(null, "User Created!");
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean login(String usernameOrEmailTextField, String passwordField) {
        boolean bool = false;
        query = "SELECT * FROM `user_login` WHERE ((Lower(`username`)=? OR Lower(`email`)=?) OR `username`=? OR `email`=?) AND `password`=?";
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, usernameOrEmailTextField);
            pst.setString(2, usernameOrEmailTextField);
            pst.setString(3, usernameOrEmailTextField);
            pst.setString(4, usernameOrEmailTextField);
            pst.setString(5, passwordField);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                bool = true;
            } else {
                bool = false;
            }
            pst.close();
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }

    public boolean checkUsername(String username) {
        boolean checkUser = false;
        query = "SELECT * FROM `user_login` WHERE `username`=?";
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, username);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                checkUser = true;
                resultSet.close();
                pst.close();
            }
            resultSet.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkUser;
    }

    public boolean checkEmail(String email) {
        boolean checkEmail = false;
        query = "SELECT * FROM `user_login` WHERE `email`=?";
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, email);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                checkEmail = true;
                resultSet.close();
                pst.close();
            }
            resultSet.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return checkEmail;
    }

    public boolean checkNationalID(String indentityCard, String identityID) {
        boolean checkID = false;
        query = "SELECT * FROM `user_login` WHERE `identity_card`=? AND `national_id`=?";
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, indentityCard);
            pst.setString(2, identityID);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                checkID = true;
                resultSet.close();
                pst.close();
            }
            resultSet.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return checkID;
    }

    public void getStaffDate(JTable jTable) {
        query = "SELECT * FROM `user_login`";
        resultSet = connectionProvider.getResultSet(query);
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getLong(6), resultSet.getString(7), resultSet.getString(8), resultSet.getLong(9), resultSet.getString(11)});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

    public void searchStaff(JTable jTable, JTextField jTextField) {
        try {
            String emailOrUsername = jTextField.getText();
            query = "SELECT * FROM `user_login` WHERE `email` like '%" + emailOrUsername + "%' OR `email` like'%" + emailOrUsername + "%' ";
            resultSet = connectionProvider.getResultSet(query);
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            if (jTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Input username or email");
            }
            model.setRowCount(0);
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getLong(6), resultSet.getString(7), resultSet.getString(8), resultSet.getLong(9), resultSet.getString(11)});
            }
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteStaff(JTable jTable) {
        int row = jTable.getSelectedRow();
        String cell = jTable.getModel().getValueAt(row, 0).toString();
        query = "DELETE FROM `user_login` where `ID`= " + cell;
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete this staff?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                pst.execute();
                JOptionPane.showMessageDialog(null, "Delete successfuly");
                pst.close();
            }
            pst.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public boolean checkEmail(JTextField emailField) {
        boolean bool = false;
        query = "SELECT * FROM `user_login` WHERE `email` =?";
        email = emailField.getText();
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, email);
            resultSet = pst.executeQuery();

            if (resultSet.next()) {
                bool = true;
                pst.close();
                resultSet.close();
            } else {
                bool = false;
                pst.close();
                resultSet.close();
            }
            resultSet.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(forgetPasswordJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bool;
    }

    public void changePassword(JTextField emailField, JTextField securityQuestionTextField, JTextField securityAnswerTextField, JTextField passField) {
        query = "SELECT * FROM `user_login` WHERE `email` =? AND `security_question`=? AND `security_answer`=?";
        email = emailField.getText();
        securityQuestion = securityQuestionTextField.getText();
        securityAnswer = securityAnswerTextField.getText();
        password = passField.getText();

        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, securityQuestion);
            pst.setString(3, securityAnswer);
            resultSet = pst.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "The answer is not correct");
            } else {
                pst = con.prepareStatement("UPDATE `user_login` Set `password`=? Where `email`=? ");
                pst.setString(1, password);
                pst.setString(2, email);
                pst.executeUpdate();
                pst.close();
                resultSet.close();
                JOptionPane.showMessageDialog(null, "Password has been update");
            }
            pst.close();
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(forgetPasswordJPanel.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

//    public  void checkValidEmail(JTextField emailField, JTextField securityQuestionField, JTextField securityAnswerField, JTextField passField){
//        email=emailField.getText();
//        securityQuestion=securityQuestionField.getText();
//        securityAnswer=securityAnswerField.getText();
//        password=passField.getText();
//        int check = 0;
//        query = "SELECT * FROM `user_login` WHERE `email` =?";
//        emailField.getText();
//        if (emailField.getText().equals(0) || emailField.getText().equals("example@email.com")) {
//            check = 1;
//            JOptionPane.showMessageDialog(null, "Please Enter your Email");
//        } else {
//            try {
//                con = connectionProvider.getCon();
//                pst = con.prepareStatement(query);
//                pst.setString(1, email);
//                resultSet = pst.executeQuery();
//
//                if (resultSet.next()) {
//                    check = 1;
//                    securityAnswerField.setEditable(true);
//                    securityQuestionField.setEditable(false);
//                    emailField.setEditable(false);
//                    securityQuestionField.setText(resultSet.getString(12));
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(forgetPasswordJPanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        if (check == 0) {
//            JOptionPane.showMessageDialog(null, "Incorrect Email");
//        }
//    }
    public static boolean is_Letter(char ch) {
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z');
    }

    public static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    public boolean isValidPass(String password) {
        if (password.length() < PASSWORD_LENGTH) {
            return false;
        }
        int charCount = 0;
        int numCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (is_Numeric(ch)) {
                numCount++;
            } else if (is_Letter(ch)) {
                charCount++;
            } else {
                return false;
            }
        }
        return (charCount >= 1 && numCount >= 1);
    }

}
