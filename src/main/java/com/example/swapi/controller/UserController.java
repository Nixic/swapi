package com.example.swapi.controller;


import com.example.swapi.UserApi;
import com.example.swapi.model.User;
import com.example.swapi.service.UserService;
import com.example.swapi.utils.JwtTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RestController()
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController implements UserApi {

    UserService userService;
    JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseEntity<User> _createUser(@Valid User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> _createUsersWithListInput(List<User> user) {
        return new ResponseEntity<>(userService.createUsersWithListInput(user), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> _deleteUser(String username) {
        return null;
    }

    @Override
    public ResponseEntity<User> _getUserByName(String userName) {
        return new ResponseEntity<>(userService.getUserByUsername(userName), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> _authUser(String username, String password) {
        return new ResponseEntity<>(jwtTokenUtil.generateToken(username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> _logoutUser() {
        return null;
    }

    @Override
    public ResponseEntity<Void> _updateUser(String username, @Valid User user) {
        userService.updateUser(username, user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
