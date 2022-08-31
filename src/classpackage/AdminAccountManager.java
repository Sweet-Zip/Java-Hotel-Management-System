/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classpackage;

import java.awt.HeadlessException;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
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
import mypackage.Login_Register;
import mypackage.MainDashboard;

/**
 *
 * @author Desktop-Desk
 */
public class AdminAccountManager {

    private String query;
    private PreparedStatement pst, pstE;
    private Connection con;
    private ResultSet resultSet, resultSetE;
    private static final int PASSWORD_LENGTH = 8;
    ConnectionProvider connectionProvider = new ConnectionProvider();

    AdminAccount adminAccount = new AdminAccount();

    public void register(JTextField registerEmailField, JTextField registerFirstNameField, JTextField registerLastNameTextField,
            JTextField registerUsernameField, JTextField registerPhoneNumberTextField, JTextField registerNationalityTextField,
            JComboBox registerIdentityCardOptionComboBox, JTextField registerIdentityIDTextField, JTextField registerPasswordField,
            JTextField registerAddress, JComboBox registerSecurityQuestionComboBox, JTextField registerAnswerField) {

        query = "INSERT INTO `user_login`(`email`, `firstname`, `lastname`, `username`, `phone_number`, `nationality`, `identity_card`, `national_id`, `password`, `address`, `security_question`, `security_answer`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        adminAccount.setEmail(registerEmailField.getText());
        adminAccount.setFirstName(registerFirstNameField.getText());
        adminAccount.setLastName(registerLastNameTextField.getText());
        adminAccount.setUsername(registerUsernameField.getText());
        adminAccount.setPhoneNumber(Long.parseLong(registerPhoneNumberTextField.getText()));
        adminAccount.setIdentityCard(registerIdentityCardOptionComboBox.getSelectedItem().toString());
        adminAccount.setNationalID(Long.parseLong(registerIdentityIDTextField.getText()));
        adminAccount.setNationality(registerNationalityTextField.getText());
        adminAccount.setPassword(registerPasswordField.getText());
        adminAccount.setAddress(registerAddress.getText());
        adminAccount.setSecurityQuestion(registerSecurityQuestionComboBox.getSelectedItem().toString());
        adminAccount.setSecurityAnswer(registerAnswerField.getText());

        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, adminAccount.getEmail());
            pst.setString(2, adminAccount.getFirstName());
            pst.setString(3, adminAccount.getLastName());
            pst.setString(4, adminAccount.getUsername());
            pst.setLong(5, adminAccount.getPhoneNumber());
            pst.setString(6, adminAccount.getNationality());
            pst.setString(7, adminAccount.getIdentityCard());
            pst.setLong(8, adminAccount.getNationalID());
            pst.setString(9, adminAccount.getPassword());
            pst.setString(10, adminAccount.getAddress());
            pst.setString(11, adminAccount.getSecurityQuestion());
            pst.setString(12, adminAccount.getSecurityAnswer());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "User Created!");
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean login(String usernameOrEmailTextField, String passwordField) {
        boolean bool = false;
        query = "SELECT * FROM `user_login` WHERE `username`=? AND `password`=?";
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, usernameOrEmailTextField);
            pst.setString(2, passwordField);
            resultSet = pst.executeQuery();

            con = connectionProvider.getCon();
            pstE = con.prepareStatement("SELECT * FROM `user_login` WHERE `email`=? AND `password`=?");
            pstE.setString(1, usernameOrEmailTextField);
            pstE.setString(2, passwordField);
            resultSetE = pstE.executeQuery();
            if (resultSet.next()) {
                bool = true;
            } else if (resultSetE.next()) {
                bool = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }

    public boolean checkUsername(String username) {
        boolean checkUser = false;
        query = "SELECT * FROM `user_login` WHERE `username`=?";
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, username);

            resultSet = pst.executeQuery();

            if (resultSet.next()) {
                checkUser = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkUser;
    }

    public boolean checkEmail(String email) {
        boolean checkEmail = false;
        query = "SELECT * FROM `user_login` WHERE `email`=?";
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, email);

            resultSet = pst.executeQuery();

            if (resultSet.next()) {
                checkEmail = true;

            }
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
            ConnectionProvider connectionProvider = new ConnectionProvider();
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, indentityCard);
            pst.setString(2, identityID);

            resultSet = pst.executeQuery();

            if (resultSet.next()) {
                checkID = true;

            }
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
            query = "SELECT * FROM `user_login` WHERE `email` like '%" + emailOrUsername + "%' or `email` like'%" + emailOrUsername + "%' ";
            resultSet = connectionProvider.getResultSet(query);
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            if (jTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Input username or email");
            }
            model.setRowCount(0);
            while (resultSet.next()) {
                resultSet.getInt(1);
                resultSet.getString(2);
                resultSet.getString(3);
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

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public boolean checkEmail(JTextField emailField) {
        boolean bool = false;
        query = "SELECT * FROM `user_login` WHERE `email` =?";
        adminAccount.setEmail(emailField.getText());

        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, adminAccount.getEmail());
            resultSet = pst.executeQuery();

            if (resultSet.next()) {
                bool = true;
            } else {
                bool = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(forgetPasswordJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bool;
    }

    public void changePassword(JTextField emailField, JTextField securityQuestionTextField, JTextField securityAnswerTextField, JTextField passField) {
        query = "SELECT * FROM `user_login` WHERE `email` =? AND `security_question`=? AND `security_answer`=?";
        adminAccount.setEmail(emailField.getText());
        adminAccount.setSecurityQuestion(securityQuestionTextField.getText());
        adminAccount.setSecurityAnswer(securityAnswerTextField.getText());
        adminAccount.setPassword(passField.getText());

        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            pst.setString(1, adminAccount.getEmail());
            pst.setString(2, adminAccount.getSecurityQuestion());
            pst.setString(3, adminAccount.getSecurityAnswer());
            resultSet = pst.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "The answer is not correct");
            } else {
                pst = con.prepareStatement("UPDATE `user_login` Set `password`=? Where `email`=? ");
                pst.setString(1, adminAccount.getPassword());
                pst.setString(2, adminAccount.getEmail());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Password has been update");
            }
        } catch (SQLException ex) {
            Logger.getLogger(forgetPasswordJPanel.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

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
