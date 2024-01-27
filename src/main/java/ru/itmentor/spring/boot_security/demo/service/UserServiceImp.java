package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.DAO.UserDaoImp;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    private final UserDaoImp userDaoImp;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImp(UserDaoImp userDaoImp, PasswordEncoder passwordEncoder) {
        this.userDaoImp = userDaoImp;
        this.passwordEncoder= passwordEncoder;
    }

    @Override
    public void setUserRoles(Long userId, Set<Role> newRoles) {
        userDaoImp.setUserRoles(userId, newRoles);

    }
    public void removeRoles(Long userId, Set<Role> rolesToRemove){
        userDaoImp.removeRoles(userId, rolesToRemove);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userDaoImp.loadUserByUsername(email);
    }

    @Override
    public void deleteById(Long id) {
        userDaoImp.deleteById(id);
    }

    public User getInfo(UserDetails currentUserDetails){
        return userDaoImp.getInfo(currentUserDetails);
    }

    @Override
    public List<User> listUsers() {
        return userDaoImp.listUsers();
    }

    @Override
    public void saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDaoImp.saveUser(user);
    }

    @Override
    public User getUser(Long id) {

        return userDaoImp.getUser(id);
    }

    @Override
    public void updateUser(User user) {

        userDaoImp.updateUser(user);

    }
}