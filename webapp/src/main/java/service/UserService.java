package service;

import java.util.List;
import dao.UserDAO;
import model.User;

public class UserService {

    private final UserDAO userDao = new UserDAO();

    public boolean validarUsuario(User user) {
        return userDao.findByUsername(user.getUsername()) != null;
    }
    
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public boolean registrarUsuario(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            return false; 
        }
        return userDao.save(user);
    }

    public List<User> obtenerTodosUsuarios() {
        return userDao.getAllUsers();
    }

    public boolean actualizarContraseña(String username, String newPassword) {
        return userDao.updatePassword(username, newPassword);
    }

    public boolean eliminarUsuario(String username) {
        return userDao.deleteUser(username);
    }
}
