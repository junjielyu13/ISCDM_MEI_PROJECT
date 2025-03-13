package dao;

import model.User;
import config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.PasswordHashUtil;

public class UserDAO {
    
    public User validUser(User user) {
        String sql = "SELECT * FROM USERS WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");


                if (PasswordHashUtil.verifyPassword(user.getPassword(), storedHash)) {
                    return new User(rs.getString("id"),
                                    rs.getString("username"),
                                    storedHash,
                                    rs.getString("email"),
                                    rs.getString("name"),
                                    rs.getString("surname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("id"),
                                 rs.getString("surname"),
                                  rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    public User findByEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("username"),
                                 rs.getString("surname"),
                                  rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public boolean save(User user) {
        String hashedPassword = PasswordHashUtil.hashPassword(user.getPassword());
        String sql = "INSERT INTO users (username, password, email, name, surname) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername() );
            stmt.setString(2, hashedPassword);  
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getName());  
            stmt.setString(5, user.getSurname());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(rs.getString("id"),
                                     rs.getString("surname"),
                                     rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM usuarios WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
