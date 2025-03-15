package service;

import java.util.List;
import dao.UserDAO;
import model.User;

public class UserService {

    private final UserDAO userDao = new UserDAO();

    public boolean validUser(User user) {
        return userDao.validUser(user) != null;
    }
    
    public User getUserByID(String id) {
        return userDao.findByID(id);
    }
        
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
    
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean registerUser(User user) {
        if (userDao.findByEmail(user.getEmail()) != null) {
            return false; 
        }
        return userDao.save(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public boolean updatePassword(String username, String newPassword) {
        return userDao.updatePassword(username, newPassword);
    }

    public boolean deleteUser(String username) {
        return userDao.deleteUser(username);
    }
}
