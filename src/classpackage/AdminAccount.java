/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classpackage;

/**
 *
 * @author Desktop-Desk
 */
public class AdminAccount extends Person {

    private String password, securityQuestion, securityAnswer, address, username;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AdminAccount() {

    }

    public AdminAccount(int ID, String email, String firstName, String lastName, String username, int phoneNumber, String nationality, String identityCard, int nationalID, String address, String password, String securityQuestion, String securityAnswer) {
        this.setFirstName(firstName);
        this.setEmail(email);
        this.setLastName(lastName);
        this.setNationalID(nationalID);
        this.setNationality(nationality);
        this.setIdentityCard(identityCard);
        this.setPhoneNumber(phoneNumber);

        this.password = password;
        this.securityAnswer = securityAnswer;
        this.address = address;
        this.securityQuestion = securityQuestion;
        this.ID = ID;
        this.username = username;

    }

}
