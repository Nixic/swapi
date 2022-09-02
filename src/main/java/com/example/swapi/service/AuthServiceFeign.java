package com.example.swapi.service;

import com.example.swapi.dto.CurrentUserDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public  interface AuthServiceFeign {

    @GetMapping({"/api/v1/user/authorized"})
    CurrentUserDTO getAuthorizedUser() throws UsernameNotFoundException;

    @GetMapping({"/api/v1/user/authorized"})
    CurrentUserDTO getAuthorized(@RequestHeader("authorization") String var1) throws UsernameNotFoundException;

    @GetMapping({"/api/v1/user/anonymous"})
    CurrentUserDTO getAnonymousUser();

    @GetMapping({"/api/v1/version/service"})
    String getVersion();
}
