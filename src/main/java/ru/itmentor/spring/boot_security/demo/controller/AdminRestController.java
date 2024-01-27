package ru.itmentor.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.*;



@RequestMapping("/admin")
@RestController
public class AdminRestController {

    private UserService userService;



    @Autowired
    public AdminRestController( UserService userService) {
        this.userService = userService;

    }
    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user){
        userService.saveUser(user);
        userService.setUserRoles(user.getId(), user.getRoles());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser (@PathVariable("id") Long id){
        Set<Role> rolesToRemove = new HashSet<>();
        userService.removeRoles(id, rolesToRemove);
        userService.deleteById(id);
        return new ResponseEntity<>("User with ID =" +id + "delete", HttpStatus.OK);
    }


}