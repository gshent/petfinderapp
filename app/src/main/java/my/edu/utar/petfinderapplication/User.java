package my.edu.utar.petfinderapplication;

public class User {
    private String fullname, password, email, phone;

    // Default constructor (required for Firebase)
    public User() { };

    public User(String fullname, String password, String email, String phone) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return fullname;
    }

    public void setUsername(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}