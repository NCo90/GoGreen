package main.java.server.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/*This class is just made as an entity object, I'm not sure if the toString is even allowed in here by AOP standards.
 *
 * This class will be used by DI to create a new user to send to the database
 * Also this class will have to implement the hashing.
 */
public class User {
    private String username;
    private String password;
    private String email;

    //just a very general constructor.
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @JsonCreator
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
    }

    public User() {
    this.email = null;
    }

    //for now I have generated all getters/setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return "[username: " + username + " , password: " + password + " , email: " +email + ']';
    }

}


