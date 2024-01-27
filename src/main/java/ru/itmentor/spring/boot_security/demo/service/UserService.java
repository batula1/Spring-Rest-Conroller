package ru.itmentor.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetails;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    void setUserRoles(Long userId, Set<Role> newRoles);
    void removeRoles(Long userId, Set<Role> rolesToRemove);
    UserDetails loadUserByUsername(String email);
    void deleteById(Long id);
    User getInfo(UserDetails currentUserDetails);
    List<User> listUsers ();
    void saveUser(User user);
    User getUser(Long id);
    void updateUser(User user);
}
