package model;

public class User {
    private String idUsuario;
    private String password;

    public User() {}

    public User(String idUsuario, String password) {
        this.idUsuario = idUsuario;
        this.password = password;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario='" + idUsuario + "', password='" + password + "'}";
    }
}
