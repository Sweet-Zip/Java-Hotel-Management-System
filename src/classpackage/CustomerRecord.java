/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classpackage;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.sql.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Desktop-Desk
 */
public class CustomerRecord {

    private String name, roomNumber, roomType, bedType, price, checkInDate;
    private ResultSet resultSet;
    private String query;
    private Connection con;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    Person person = new Person();
    private int ID, totalPrice, getMoney, totalDay, change;
    private String checkOutDate;
    private PreparedStatement pst;
    private DefaultTableModel model;

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
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void priceRoom(JComboBox comboBox, JTextField jTextField) {
        query = "SELECT * FROM `room_manager` WHERE `room_no`=" + comboBox.getSelectedItem();
        try {
            con = connectionProvider.getCon();
            PreparedStatement pst = con.prepareStatement(query);
            resultSet = pst.executeQuery(query);
            while (resultSet.next()) {
                jTextField.setText(resultSet.getString(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCheckIn(JTextField nameField, JTextField emailField, JTextField phoneNumberField, JTextField nationalityField, JComboBox indentityCard,
            JTextField nationalIDField, JTextField checkinDateField, JComboBox roomT, JComboBox bedT, JComboBox roomNo, JTextField priceField) {

        //dateToday(checkinDateField);
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
        try {
            if (!price.equals("")) {
                query = "update room_manager set `status`='Booked' WHERE room_no='" + roomNumber + "'";
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
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkCustomer(JTextField roomNoField, JTextField nameField, JTextField emailField, JTextField phoneNumberField, JTextField checkInDateField, JTextField checkOutDateField,
            JTextField pricePerDayField, JTextField totalDayField, JTextField changeField, JTextField totalPriceField) {

        roomNumber = roomNoField.getText();
        query = "SELECT * FROM `customer` WHERE `room_no`='" + roomNumber + "' AND `checkout_date` IS NULL";
        resultSet = connectionProvider.getResultSet(query);

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Calendar cal = Calendar.getInstance();
            if (resultSet.next()) {
                emailField.setText(resultSet.getString(2));
                nameField.setText(resultSet.getString(3));
                phoneNumberField.setText(resultSet.getString(4));
                checkInDateField.setText(resultSet.getString(8));
                checkInDate = resultSet.getString(8);
                java.util.Date dateCheckIn = myFormat.parse(checkInDate);

                checkOutDateField.setText(myFormat.format(cal.getTime()));
                checkOutDate = myFormat.format(cal.getTime());
                java.util.Date dateCheckOut = myFormat.parse(checkOutDate);
                Long difference = dateCheckOut.getTime() - dateCheckIn.getTime();

                totalDay = (int) (difference / 86400000);
                if (totalDay == 0) {
                    totalDay = 1;
                }

                pricePerDayField.setText(resultSet.getString(12));
                int price = Integer.parseInt(pricePerDayField.getText());
                totalDayField.setText(String.valueOf(totalDay));

                totalPrice = totalDay * price;

                totalPriceField.setText(String.valueOf(totalPrice));
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect room number or room is not booked yet");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void calChange(JTextField getFromCustomerField, JTextField totalPriceField, JTextField changeField) {
        totalPrice = Integer.parseInt(totalPriceField.getText());
        getMoney = Integer.parseInt(getFromCustomerField.getText());

        if (getMoney >= totalPrice) {
            changeField.setText(String.valueOf(getMoney - totalPrice));
        } else {
            JOptionPane.showMessageDialog(null, "The ammount you get from customer is less than total price you need " + (totalPrice - getMoney) + " More");
        }
    }

    public void checkOut(JTextField roomNoField, JTextField checkOutDateField, JTextField totalDayField, JTextField getFromCustomerField, JTextField changeField, JTextField totalPriceField) {
        roomNumber = roomNoField.getText();
        checkOutDate = checkOutDateField.getText();
        totalDay = Integer.parseInt(totalDayField.getText());
        getMoney = Integer.parseInt(getFromCustomerField.getText());
        change = Integer.parseInt(changeField.getText());
        totalPrice = Integer.parseInt(totalPriceField.getText());

        query = "SELECT * FROM `customer` WHERE `room_no`=" + roomNumber + " AND `checkout_date` is NULL";
        resultSet = connectionProvider.getResultSet(query);
        try {
            if (resultSet.next()) {
                ID = resultSet.getInt(1);
                query = "UPDATE `customer` SET `checkout_date`=?, `total_day`=?, `get_money`=?, `change_money`=?, `total_price`=? WHERE `room_no`=" + roomNumber;
                con = connectionProvider.getCon();
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, checkOutDate);
                    pst.setInt(2, totalDay);
                    pst.setInt(3, getMoney);
                    pst.setInt(4, change);
                    pst.setInt(5, totalPrice);
                    pst.executeUpdate();
                    query = "UPDATE `room_manager` SET `status`= 'Available' WHERE `room_no`=" + roomNumber;
                    pst = con.prepareStatement(query);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Customer checkout succed");

                } catch (SQLException ex) {
                    Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void invoid(JTextField roomNoField, JTextField nameField, JTextField emailField, JTextField phoneNumberField, JTextField checkInDateField, JTextField checkOutDateField,
            JTextField pricePerDayField, JTextField totalDayField, JTextField getMoneyField, JTextField changeField, JTextField totalPriceField) {

        //roomNumber = roomNoField.getText();
        String patch = "C:\\Users\\Desktop-Desk\\Documents\\NetBeansProjects\\HotelManagement\\invoid\\";
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(patch + nameField.getText() + " " + checkOutDateField.getText() + ".pdf"));
            doc.open();
            Paragraph paragraph1 = new Paragraph("                                                        Hotel Management System");
            doc.add(paragraph1);
            Paragraph paragraph2 = new Paragraph("*********************************************************************************************************");
            doc.add(paragraph2);
            Paragraph paragraph3 = new Paragraph("Customer Detail:::\n\tName: " + nameField.getText() + "\n\tEmail: " + emailField.getText() + "\n\tPhone Number: " + phoneNumberField.getText());
            doc.add(paragraph3);
            doc.add(paragraph2);
            Paragraph paragraph5 = new Paragraph("Room Detail:::\n\tRoom Number: " + roomNoField.getText() /*+ "\n\tRoom Type: " + resultSet.getString(9) + "\n\tBed Type: " + resultSet.getString(10)*/ + "\n\tPrice per day: " + pricePerDayField.getText());
            doc.add(paragraph5);
            doc.add(paragraph2);
            final PdfPTable tb = new PdfPTable(6);
            tb.addCell("Checkin Date: " + checkInDateField.getText());
            tb.addCell("Checkout Date: " + checkOutDateField.getText());
            tb.addCell("Total days of stay: " + totalDayField.getText());
            tb.addCell("Total money get from customer: " + getMoneyField.getText());
            tb.addCell("Change: " + changeField.getText());
            tb.addCell("Total paid: " + totalPriceField.getText());
            doc.add(tb);
            doc.add(paragraph2);
            Paragraph paragraph6 = new Paragraph("Thank You Please Visit Us Again Next Time");
            doc.add(paragraph6);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.close();
        int a = JOptionPane.showConfirmDialog(null, "Do you want to print the bill invoid?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {

            if ((new File(patch + nameField.getText() + " " + checkOutDateField.getText() + ".pdf")).exists()) {
                try {
                    Process process = Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + patch + nameField.getText() + " " + checkOutDateField.getText() + ".pdf");
                } catch (IOException ex) {
                    Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("File is not Exits");
            }
        }
    }

    public void showCustomerInfo(JTable jTable) {
        query = "SELECT * FROM `customer` WHERE `total_price` IS NULL";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {

                model.addRow(new Object[]{resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getLong(4), resultSet.getString(5),
                    resultSet.getString(6), resultSet.getLong(7), resultSet.getString(8), resultSet.getInt(11), resultSet.getString(9), resultSet.getString(10), resultSet.getInt(12)});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

    public void searchCustomer(JTextField jTextField, JTable jTable) {
        query = "SELECT * FROM `customer` WHERE `name` like '%" + jTextField.getText() + "%' AND `total_price` IS NULL";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        
        if (jTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Input customer name to search...");
        } else {
            try {
                model.setRowCount(0);
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getLong(4), resultSet.getInt(11),
                    resultSet.getString(9), resultSet.getString(10), resultSet.getInt(12), resultSet.getString(8), resultSet.getString(13), resultSet.getInt(14),
                    resultSet.getInt(16), resultSet.getInt(17), resultSet.getInt(15)});
                }
                resultSet.close();
            } catch (SQLException e) {
            }
        }

    }

    public void showCustomerHistory(JTable jTable) {
        query = "SELECT * FROM `customer` WHERE `total_price` IS NOT NULL";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getLong(4), resultSet.getInt(11),
                    resultSet.getString(9), resultSet.getString(10), resultSet.getInt(12), resultSet.getString(8), resultSet.getString(13), resultSet.getInt(14),
                    resultSet.getInt(16), resultSet.getInt(17), resultSet.getInt(15)});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

    public void searchCustomerHistory(JTextField jTextField, JTable jTable) {
        query = "SELECT * FROM `customer` WHERE `name` like '%" + jTextField.getText() + "%' AND `total_price` IS NOT NULL";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        
        if (jTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Input customer name to search...");
        } else {
            model.setRowCount(0);
            try {
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getString(1), resultSet.getString(3), resultSet.getString(2), resultSet.getString(4), resultSet.getString(11),
                    resultSet.getString(9), resultSet.getString(10), resultSet.getString(12), resultSet.getString(8), resultSet.getString(13), resultSet.getString(14),
                    resultSet.getString(16), resultSet.getString(17), resultSet.getString(15)});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
        }
        
    }

    public void printInvoid(JTable jTable) {
        String patch = "C:\\Users\\Desktop-Desk\\Documents\\NetBeansProjects\\HotelManagement\\invoid\\";
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        int row = jTable.getSelectedRow();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(patch + jTable.getModel().getValueAt(row, 0).toString() + " " + jTable.getModel().getValueAt(row, 1).toString() + ".pdf"));
            doc.open();
            Paragraph paragraph1 = new Paragraph("                                                        Hotel Management System");
            doc.add(paragraph1);
            Paragraph paragraph2 = new Paragraph("*********************************************************************************************************");
            doc.add(paragraph2);
            Paragraph paragraph3 = new Paragraph("Customer ID: " + jTable.getModel().getValueAt(row, 0).toString() + "\nCustomer Detail:::\n\tName: " + jTable.getModel().getValueAt(row, 1).toString() + "\n\tEmail: " + jTable.getModel().getValueAt(row, 2).toString() + "\n\tPhone Number: " + jTable.getModel().getValueAt(row, 3).toString());
            doc.add(paragraph3);
            doc.add(paragraph2);
            Paragraph paragraph5 = new Paragraph("Room Detail:::\n\tRoom Number: " + jTable.getModel().getValueAt(row, 4).toString() + "\n\tRoom Type: " + jTable.getModel().getValueAt(row, 5).toString() + "\n\tBed Type: " + jTable.getModel().getValueAt(row, 6).toString() + "\n\tPrice per day: " + jTable.getModel().getValueAt(row, 7).toString());
            doc.add(paragraph5);
            doc.add(paragraph2);
            final PdfPTable tb = new PdfPTable(6);
            tb.addCell("Checkin Date: " + jTable.getModel().getValueAt(row, 8).toString());
            tb.addCell("Checkout Date: " + jTable.getModel().getValueAt(row, 9).toString());
            tb.addCell("Total days of stay: " + jTable.getModel().getValueAt(row, 10).toString());
            tb.addCell("Total money get from customer: " + jTable.getModel().getValueAt(row, 11).toString());
            tb.addCell("Change: " + jTable.getModel().getValueAt(row, 12).toString());
            tb.addCell("Total paid: " + jTable.getModel().getValueAt(row, 13).toString());
            doc.add(tb);
            doc.add(paragraph2);
            Paragraph paragraph6 = new Paragraph("Thank You Please Visit Us Again Next Time");
            doc.add(paragraph6);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.close();
        int a = JOptionPane.showConfirmDialog(null, "Do you want to print the bill invoid?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {

            if ((new File(patch + jTable.getModel().getValueAt(row, 0).toString() + " " + jTable.getModel().getValueAt(row, 1).toString() + ".pdf")).exists()) {
                try {
                    Process process = Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + patch + jTable.getModel().getValueAt(row, 0).toString() + " " + jTable.getModel().getValueAt(row, 1).toString() + ".pdf");
                } catch (IOException ex) {
                    Logger.getLogger(CustomerRecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("File is not Exits");
            }
        }
    }
}
