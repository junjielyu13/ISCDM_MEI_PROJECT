/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.DatabaseConfig to edit this template
 */
package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author alumne
 */
public class DatabaseConfig {
    
    private static final String URL = "jdbc:derby://localhost:1527/pr2";
    private static final String USER = "pr2";
    private static final String PASSWORD = "pr2";

    static {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver"); 
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading database driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
