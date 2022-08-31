/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classpackage;

import java.sql.*;
import javax.swing.*;

/**
 *
 * @author Desktop-Desk
 */
public class BookingTable {

    public static void main(String[] args) {
        
        Connection con = null;
        Statement st = null;
        try {

            //con=ConnectionProvider.getCon();
            st=con.createStatement();
            st.executeUpdate("create table user(name varchar(200), email varchar(200),password varchar(50),securityQuestion varchar(200), securityAnswer(200))");
            JOptionPane.showMessageDialog(null, "table created succed");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                con.close();
                st.close();
            } catch (Exception e) {

            }
        }
    }

}
