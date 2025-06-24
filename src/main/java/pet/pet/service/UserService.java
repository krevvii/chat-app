package pet.pet.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pet.pet.controller.RegistrationRequest; 
import pet.pet.model.User;
import pet.pet.repository.UserRepository;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(RegistrationRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new IllegalStateException("Пользователь с таким именем уже существует");
        });

        User newUser = new User();
        newUser.setUsername(request.getUsername());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(hashedPassword);

        newUser.setRoles(Set.of("ROLE_USER")); 

        userRepository.save(newUser);
    }
}