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
import jframepackage.MainDashboard;

/**
 *
 * @author Desktop-Desk
 */
public class RoomManager {

    private int roomNumber, price;
    private int roomID;
    private String status;
    private String roomType, bedType;
    private ResultSet resultSet;
    private DefaultTableModel model;
    private String query;
    private Connection con;
    private PreparedStatement pst;

    ConnectionProvider connectionProvider = new ConnectionProvider();

    public void addRoom(JTextField roomNum, JComboBox roomT, JComboBox bedT, JTextField priceField) {
        query = "SELECT * FROM `room_manager` WHERE `room_no`='" + roomNum.getText() + "'";
        resultSet = connectionProvider.getResultSet(query);
        try {
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Room" + roomNum.getText() + " already exist");
                resultSet.close();
            } else {
                resultSet.close();
                query = "insert into `room_manager`(room_no, room_type, bed_type, price, status)values(?,?,?,?,?)";
                roomNumber = Integer.parseInt(roomNum.getText());
                roomType = roomT.getSelectedItem().toString();
                bedType = bedT.getSelectedItem().toString();
                price = Integer.parseInt(priceField.getText());
                try {
                    con = connectionProvider.getCon();
                    pst = con.prepareStatement("insert into `room_manager`(room_no, room_type, bed_type, price, status)values(?,?,?,?,?)");
                    pst.setInt(1, roomNumber);
                    pst.setString(2, roomType);
                    pst.setString(3, bedType);
                    pst.setInt(4, price);
                    pst.setString(5, "Available");
                    pst.executeUpdate();
                    pst.close();
                    JOptionPane.showMessageDialog(null, "Room has beed added");
                    roomNum.setText("");
                    roomT.setSelectedIndex(0);
                    bedT.setSelectedIndex(0);
                    priceField.setText("");
                } catch (SQLException ex) {
                    Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void searchRoom(JTextField searchRoomTextField, JTable jTable) {
        try {
            String roomNoSearch = searchRoomTextField.getText();
            query = "SELECT * FROM `room_manager` WHERE `room_no` = '" + roomNoSearch + "' ";
            resultSet = connectionProvider.getResultSet(query);
            model = (DefaultTableModel) jTable.getModel();
            if (searchRoomTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Input room number");
            }
            model.setRowCount(0);
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6)});
            }
            resultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editRoom(JTable jTable, JTextField roomNum, JComboBox roomT, JComboBox bedT, JTextField priceField) {
        int row = jTable.getSelectedRow();
        String cell = jTable.getModel().getValueAt(row, 0).toString();
        query = "UPDATE `room_manager` set room_no=?, room_type=?, bed_type=?, price=? WHERE `ID`= " + cell;
        roomNumber = Integer.parseInt(roomNum.getText());
        price = Integer.parseInt(priceField.getText());
        roomType = roomT.getSelectedItem().toString();
        bedType = bedT.getSelectedItem().toString();
        try {
            con = connectionProvider.getCon();
            int a = JOptionPane.showConfirmDialog(null, "Do you really want to update this room?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                pst = con.prepareStatement(query);
                pst.setInt(1, roomNumber);
                pst.setString(2, roomType);
                pst.setString(3, bedType);
                pst.setInt(4, price);
                pst.executeUpdate();
                pst.close();
                JOptionPane.showMessageDialog(null, "Room has been updated");
            }
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRoom(JTable jTable) {
        int row = jTable.getSelectedRow();
        String cell = jTable.getModel().getValueAt(row, 0).toString();
        query = "DELETE FROM `room_manager` where `ID`= " + cell;
        try {
            con = connectionProvider.getCon();
            pst = con.prepareStatement(query);
            int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete this room?", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                pst.execute();
                JOptionPane.showMessageDialog(null, "Delete successfuly");
            }
            pst.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void availableRoomTable(JTable jTable) {
        query = "SELECT * FROM `room_manager` Where `status`= 'Available'";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {
                roomNumber = resultSet.getInt(2);
                roomType = resultSet.getString(3);
                bedType = resultSet.getString(4);
                price = resultSet.getInt(5);
                model.addRow(new Object[]{roomNumber, roomType, bedType, price});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

    public void notAvailableRoomTable(JTable jTable) {
        query = "SELECT * FROM `room_manager` Where `status`= 'Booked'";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {
                roomNumber = resultSet.getInt(2);
                roomType = resultSet.getString(3);
                bedType = resultSet.getString(4);
                price = resultSet.getInt(5);
                model.addRow(new Object[]{roomNumber, roomType, bedType, price});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

    public void showAllRoom(JTable jTable) {

        query = "SELECT * FROM `room_manager`";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {
                roomID = resultSet.getInt(1);
                roomNumber = resultSet.getInt(2);
                roomType = resultSet.getString(3);
                bedType = resultSet.getString(4);
                price = resultSet.getInt(5);
                status = resultSet.getString(6);
                model.addRow(new Object[]{roomID, roomNumber, roomType, bedType, price, status});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

    public void filterRoom(JTable jTable, JComboBox jComboBox) {
        query = "SELECT * FROM `room_manager` WHERE (`room_type`='" + jComboBox.getSelectedItem().toString() + "' OR `bed_type`='" + jComboBox.getSelectedItem().toString() + "') AND `status`= 'Available'";
        resultSet = connectionProvider.getResultSet(query);
        model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);
        try {
            while (resultSet.next()) {
                roomNumber = resultSet.getInt(2);
                roomType = resultSet.getString(3);
                bedType = resultSet.getString(4);
                price = resultSet.getInt(5);
                model.addRow(new Object[]{roomNumber, roomType, bedType, price});
            }
            resultSet.close();
        } catch (SQLException e) {
        }
    }

}
