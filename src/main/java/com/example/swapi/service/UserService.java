package com.example.swapi.service;

import com.example.swapi.model.User;
import com.example.swapi.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    public User createUser(User user) {
        if (userRepository.getUserByUsername(user.getUsername()) != null) {
            return null; // todo add error response
        }
        return userRepository.save(user);
    }

    public List<User> createUsersWithListInput(List<User> users) {
        boolean someUserAlreadyExist=false;
        for (User user : users) {
            if (userRepository.getUserByUsername(user.getUsername()) != null) {
                someUserAlreadyExist = true;
                break;
            }
        }
        if (!someUserAlreadyExist) {
            return userRepository.saveAll(users);
        } else {
            return Collections.emptyList(); // todo add error response
        }
    }

    public User getUserByUsername(String userName) {
        User userByUsername = userRepository.getUserByUsername(userName);
        if (userByUsername != null) {
            userByUsername.setPassword(null);
            return userByUsername;
        } else {
            return null; // todo add error response
        }
    }

    @Transactional
    public User updateUser(String userName, User user) {
        User userByUsername = userRepository.getUserByUsername(userName);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        if (userByUsername != null) {
            user.setId(userByUsername.getId());
            userByUsername = mapper.map(user, User.class);
            return saveOrUpdateUser(userByUsername);
        } else {
            return null; // todo add error response
        }
    }

    private User saveOrUpdateUser(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        return user;
    }

}
