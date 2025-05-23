package model;

public class User {

    private int idUser;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username, String surname, String email) {
        this.username = username;
        this.surname = surname;
        this.email = email;
    }

    public User(String id, String username, String surname, String email) {
        this.idUser = Integer.parseInt(id);
        this.username = username;
        this.surname = surname;
        this.email = email;
    }

    public User(String username, String password, String email, String name, String surname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public User(String userId, String username, String password, String email, String name, String surname) {
        this.idUser = Integer.parseInt(userId);
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
