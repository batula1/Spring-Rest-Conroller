package ru.itmentor.spring.boot_security.demo.DAO;



import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserDaoImp implements UserDetailsService {
    private UserDetails currentUserDetails;

    private final UserDao userDao;

    @Autowired
    public UserDaoImp(UserDao userDao) {
        this.userDao = userDao;

    }

    public void setUserRoles(Long user_id, Set<Role> newRoles) {
        userDao.findById(user_id).ifPresent(user -> {
            Hibernate.initialize(user.getRoles()); // Ленивая инициализация, если необходимо
            user.setRoles(newRoles);
            userDao.save(user);
        });
    }

    public void removeRoles(Long user_id, Set<Role> rolesToRemove) {
        userDao.findById(user_id).ifPresent(user -> {
            Hibernate.initialize(user.getRoles()); // Ленивая инициализация, если необходимо
            user.getRoles().removeAll(rolesToRemove);
            userDao.save(user);
        });
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Попытка загрузить пользователя по email: " + email);
        Optional<User> userOptional = userDao.findByEmail(email);

        if (userOptional.isEmpty()) {
            System.out.println("Пользователь не найден: " + email);
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        User user = userOptional.get();
        System.out.println("Пользователь найден: " + user.getEmail());

        // Принудительно инициализируем коллекцию ролей, если необходимо
        if (!Hibernate.isInitialized(user.getRoles())) {
            System.out.println("Инициализация коллекции ролей");
            user.setRoles(new HashSet<>(user.getRoles()));
        } else {
            System.out.println("Коллекция ролей уже инициализирована");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        // Логируем роли пользователя
        System.out.println("Роли пользователя: " + authorities);

        currentUserDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );

        return currentUserDetails;
    }

    public User getInfo(UserDetails currentUserDetails) {

        String userEmail = currentUserDetails.getUsername();
        Optional<User> userOptional = userDao.findByEmail(userEmail);
        User user = userOptional.get();
        return user;
    }

    public List<User> listUsers (){
        return userDao.findAll();
    }

    public void saveUser(User user){

        userDao.save(user);
    }


    public User getUser(Long id){
        return userDao.getOne(id);

    }
    public void updateUser(User user){
        userDao.saveAndFlush(user);
    }
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }
}
