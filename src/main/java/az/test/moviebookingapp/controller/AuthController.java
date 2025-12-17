package az.test.moviebookingapp.controller;

import az.test.moviebookingapp.model.request.LoginRequest;
import az.test.moviebookingapp.model.request.RegisterRequest;
import az.test.moviebookingapp.model.view.LoginView;
import az.test.moviebookingapp.model.view.UserView;
import az.test.moviebookingapp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Creates a new user and returns the created user info.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserView> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserView created = authenticationService.register(registerRequest);
        URI location = URI.create("api/v1/auth/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Login an existing user", description = "Returns JWT and user info after successful authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginView> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
