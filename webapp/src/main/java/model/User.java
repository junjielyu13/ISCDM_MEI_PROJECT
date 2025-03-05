package model;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  

/**
 *
 * @author silviall
 */
public class User {

    public String queryTest(String user, String passwd) {
        String result = "El usuario existe";

        try (Connection c = DatabaseConfig.getConnection()) { // 使用 DatabaseConfig 获取连接
            String query = "SELECT COUNT(*) FROM usuarios WHERE id_usuario=? AND password=?";
            try (PreparedStatement statement = c.prepareStatement(query)) {
                statement.setString(1, user);
                statement.setString(2, passwd);

                try (ResultSet r = statement.executeQuery()) {
                    if (r.next() && r.getInt(1) == 0) {
                        result = "El usuario no existe";
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return result;
    }
    
}