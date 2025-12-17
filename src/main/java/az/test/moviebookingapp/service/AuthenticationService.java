package az.test.moviebookingapp.service;

import az.test.moviebookingapp.entity.Role;
import az.test.moviebookingapp.entity.User;
import az.test.moviebookingapp.exception.EmailAlreadyExistException;
import az.test.moviebookingapp.exception.UserNameAlreadyExistException;
import az.test.moviebookingapp.exception.UserNotFoundException;
import az.test.moviebookingapp.jwt.JwtService;
import az.test.moviebookingapp.mapper.UserMapper;
import az.test.moviebookingapp.model.request.LoginRequest;
import az.test.moviebookingapp.model.request.RegisterRequest;
import az.test.moviebookingapp.model.view.LoginView;
import az.test.moviebookingapp.model.view.UserView;
import az.test.moviebookingapp.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    public UserView register(RegisterRequest registerRequest) {
        validateUserUniqueness(registerRequest);

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRoles(Set.of(Role.USER));

        User saved = userRepository.save(user);
        return userMapper.toUserView(saved);
    }


    public UserView adminRegister(RegisterRequest registerRequest) {
        validateUserUniqueness(registerRequest);

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRoles(Set.of(Role.USER, Role.ADMIN));

        User saved = userRepository.save(user);
        return userMapper.toUserView(saved);
    }

    public LoginView login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(UserNotFoundException::new);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()));

        String token = jwtService.generateToken(user);

        return LoginView.builder().jwtToken(token)
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }

    private void validateUserUniqueness(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new UserNameAlreadyExistException();
        }
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new EmailAlreadyExistException();
        }
    }

//    public LoginView login(LoginRequest loginRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.username(),
//                        loginRequest.password())
//        );
//
//        // Spring Security loads UserDetails for you
//        User user = (User) authentication.getPrincipal();
//
//        String token = jwtService.generateToken(user);
//
//        return LoginView.builder()
//                .jwtToken(token)
//                .username(user.getUsername())
//                .roles(user.getRoles())
//                .build();
//    }

}
