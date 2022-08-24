package com.example.swapi.controller;

import com.example.model.User;
import com.example.swapi.UserApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RestController()
@RequestMapping("/api/v1")
public class UserController implements UserApi {

    @Override
    public ResponseEntity<User> _createUser(@Valid User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> _createUsersWithListInput(@Valid List<User> user) {
        return null;
    }

    @Override
    public ResponseEntity<Void> _deleteUser(String username) {
        return null;
    }

    @Override
    public ResponseEntity<User> _getUserByName(String username) {
        User user1 = new User();
        user1.setUsername(username);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> _loginUser(@Valid String username, @Valid String password) {
        return null;
    }

    @Override
    public ResponseEntity<Void> _logoutUser() {
        return null;
    }

    @Override
    public ResponseEntity<Void> _updateUser(String username, @Valid User user) {
        return null;
    }

}
