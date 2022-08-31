/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classpackage;

import com.mysql.cj.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Desktop-Desk
 */
public class CustomerCheckIn {

    private String roomNumber;
    private String roomType;
    private String bedType;
    private String price;
    private ResultSet resultSet;
    private String query;
    private Connection con;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    Person person = new Person();
    private int ID;
    private String name, phoneNumber, nationality, email, nationID, checkInDate;
    private String totalPrice;
    private String checkOutDate;
    private String totalDay;
    private PreparedStatement pst;

    public void dateToday(JTextField jTextField) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Calendar cal = Calendar.getInstance();
        jTextField.setText(myFormat.format(cal.getTime()));
    }

    public void roomDetail(JComboBox roomT, JComboBox bedT, JComboBox roomNum, JTextField roomPrice) {

        bedType = (String) bedT.getSelectedItem();
        roomType = (String) roomT.getSelectedItem();
        try {

            query = "SELECT * FROM `room_manager` WHERE room_type='" + roomType + "' AND bed_type='" + bedType + "' AND status='Available'";
            resultSet = connectionProvider.getResultSet(query);
            while (resultSet.next()) {

                roomNum.addItem(resultSet.getInt(2));
            }
        } catch (Exception ex) {
            Logger.getLogger(CustomerCheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void priceRoom(JComboBox comboBox, JTextField jTextField) {
        //roomNumber = (String) ;
        query = "SELECT * FROM `room_manager` WHERE `room_no`=" + comboBox.getSelectedItem();
        //resultSet = connectionProvider.getResultSet(query);
        try {
            con = connectionProvider.getCon();
            PreparedStatement pst = con.prepareStatement(query);
            resultSet = pst.executeQuery(query);
            while (resultSet.next()) {
                jTextField.setText(resultSet.getString(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerCheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCheckIn(JTextField nameField, JTextField emailField, JTextField phoneNumberField, JTextField nationalityField, JComboBox indentityCard,
            JTextField nationalIDField, JTextField checkinDateField, JComboBox roomT, JComboBox bedT, JComboBox roomNo, JTextField priceField) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Calendar calendar = Calendar.getInstance();
        checkinDateField.setText(simpleDateFormat.format(calendar.getTime()));
        person.setFirstName(nameField.getText());
        person.setEmail(emailField.getText());
        person.setPhoneNumber(Long.parseLong(phoneNumberField.getText()));
        person.setNationality(nationalityField.getText());
        person.setIdentityCard(indentityCard.getSelectedItem().toString());
        person.setNationalID(Long.parseLong(nationalIDField.getText()));
        checkInDate = checkinDateField.getText();
        roomType = roomT.getSelectedItem().toString();
        bedType = bedT.getSelectedItem().toString();
        roomNumber = roomNo.getSelectedItem().toString();
        price = priceField.getText();
        totalPrice = null;
        totalDay = null;
        checkOutDate = null;

        try {
            if (!price.equals("")) {
                query = "update room_manager set `status`='Booked' WHERE room_no=" + roomNumber;
                con = connectionProvider.getCon();
                pst = con.prepareStatement(query);
                pst.executeUpdate();

                query = "INSERT INTO `customer`(`email`, `name`, `phone_number`, `nationality`, `identity_card`, `national_id`, `checkin_date`, `room_type`, `bed_type`, `room_no`, `price_per_day`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(query);
                pst.setString(1, person.getEmail());
                pst.setString(2, person.getFirstName());
                pst.setLong(3, person.getPhoneNumber());
                pst.setString(4, person.getNationality());
                pst.setString(5, person.getIdentityCard());
                pst.setLong(6, person.getNationalID());
                pst.setString(7, checkInDate);
                pst.setString(8, roomType);
                pst.setString(9, bedType);
                pst.setString(10, roomNumber);
                pst.setString(11, price);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Check-In Succeed!");

            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerCheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
