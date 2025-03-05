package service;

import config.DatabaseConfig;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean validarUsuario(User usuario) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE id_usuario = ? AND password = ?";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement statement = c.prepareStatement(query)) {
             
            statement.setString(1, usuario.getIdUsuario());
            statement.setString(2, usuario.getPassword());

            try (ResultSet r = statement.executeQuery()) {
                return r.next() && r.getInt(1) > 0; // 如果返回的行数 > 0，则用户存在
            }

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
            return false;
        }
    }
}
