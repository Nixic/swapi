package com.example.swapi.service;

import com.example.swapi.dto.CurrentUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnonymousUserServiceImpl implements AnonymousUserService {

    // you can add you own some Auth feign service
//    private final AuthServiceFeign authServiceFeign;

    @Override
    public CurrentUserDTO getAnonymousUser() {
//        return authServiceFeign.getAnonymousUser();
        return new CurrentUserDTO(-1L, "anonymous", "password", "Anonymous", false);
    }


}
