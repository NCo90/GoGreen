package main.java.server.dao;

import org.springframework.stereotype.Repository;
import main.java.server.entity.User;

import java.security.*;
import java.sql.*;
import java.util.regex.*;

/*
*   This class will make the database connection and actually do the queries.
*   The class will be called by the UserService class which actually requests the info and then applies logic to it.
*   So in short this class is just to split logics and database access.
 */

@Repository
public class UserDao {

    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet rs;

    public static void main(String[] args) {
        con = DBConnect();
        stmt = null;
        rs = null;

        User testuser = new User("Tester5","Test123","test4@test.nl");

        //usernameCheck("Tester4");
        //emailCheck("test3@test.nl");
        DBRegister(testuser);
        //DBLogin("Tester","Test123");
    }
    //Connection with database
    public static Connection DBConnect() {
        Connection con = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://89.99.79.46:3306/gogreen","greengo", "gogreen");
        }catch(Exception e) {
            System.out.println("test");
            System.out.println(e.getMessage());
        }

        return con;
    }

    public static void DBRegister (User user) {
        String salt = generateSalt();
        String temp = user.getPassword() + salt;
        String temprepeat = user.getPassword() + salt;
        //String temprepeat = RepeatPassword + salt;
        String hashed = hashPassword(temp);
        String repeathashed = hashPassword(temprepeat);
        if(hashed.equals(repeathashed) == true) {
            if(usernameCheck(user.getUsername()) == true) {
                if(emailCheck(user.getEmail()) == true) {
                    try {
                        stmt = con.prepareStatement("INSERT INTO account (id,username,email,password,salt) VALUES (DEFAULT,?,?,?,?)");
                        stmt.setString(1, user.getUsername());
                        stmt.setString(2, user.getEmail());
                        stmt.setString(3, hashed);
                        stmt.setString(4, salt);
                        stmt.executeUpdate();
                        System.out.println("Registration succesfull!");
                    }catch(Exception e) {
                        System.out.println(e.getMessage());
                    }
                }else {
                    System.out.println("The email address already exists.");
                }
            }else {
                System.out.println("The username already exists.");
            }
        }else {
            System.out.println("The passwords do not match.");
        }
    }

    //this method will allow the user to login in the application
    public static void DBLogin(User user) {
        try {
            stmt = con.prepareStatement("SELECT password, salt FROM account WHERE username = '"+ user.getUsername() +"'");
            rs = stmt.executeQuery();
            rs.next();
            String hashedpassword = rs.getString(1);
            String salt = rs.getString(2);
            String temp = user.getPassword() + salt;
            String hashed = hashPassword(temp);
            if(hashed.equals(hashedpassword) == true) {
                System.out.println("Login is successful!");
            } else {
                System.out.println("Password is incorrect!");
            }
            //System.out.println(hashedpassword);
            //System.out.println(salt);
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Checks if the username already exists
    //when it exists it returns false
    private static boolean usernameCheck(String Username) {
        try {
            stmt = con.prepareStatement("SELECT username FROM account WHERE username = '"+ Username +"'");
            rs = stmt.executeQuery();
            if(rs.next()) {
                //System.out.println("Found username in database!");
                return false;
            }else {
                System.out.println("Not found username in database!");
                return true;
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    //Checks if the email already exists
    //when it exists it returns false
    private static boolean emailCheck(String Email) {
        try {
            stmt = con.prepareStatement("SELECT email FROM account WHERE email = '"+ Email +"'");
            rs = stmt.executeQuery();
            if(rs.next()) {
                //System.out.println("Found email in database!");
                return false;
            }else {
                //System.out.println("Not found email in database!");
                return true;
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    //this method will check if the email is in a valid email address
    public static boolean emailSyntax(String Email) {
        Pattern emailpattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher testemail = emailpattern.matcher(Email);
        if(testemail.matches()) {
            //System.out.println("This email has a valid format");
            return true;
        }else {
            //System.out.println("This email has a invalid format");
            return false;
        }
    }

    //this method will check the length of the email address
    public static boolean emailLength(String Email) {
        if(Email.length() <= 250) {
            //System.out.println("This email is not to long");
            return true;
        }else {
            //System.out.println("This email is to long");
            return false;
        }
    }

    //this method will hash the password
    private static String hashPassword(String Password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Password.getBytes("UTF-8"));
            StringBuffer hashedPassword = new StringBuffer();
            for(int j = 0; j < hash.length; j++) {
                hashedPassword.append(Integer.toString((hash[j] & 0xff) + 0x100, 16).substring(1));
            }
            return hashedPassword.toString();
        }catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //this method will generate a salt to make hashing more secure
    private static String generateSalt() {
        String possibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int index = (int)(possibleCharacters.length() * Math.random());
            sb.append(possibleCharacters.charAt(index));
        }
        return sb.toString();
    }

}
