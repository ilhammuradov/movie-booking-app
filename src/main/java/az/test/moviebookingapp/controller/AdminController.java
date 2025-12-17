package az.test.moviebookingapp.controller;

import az.test.moviebookingapp.model.request.RegisterRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
@Tag(name = "Admin", description = "Endpoints accessible only to users with ADMIN role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    AuthenticationService authenticationService;

    @Operation(summary = "Register a new admin user", description = "Only users with ADMIN role can access this endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin registered successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – not an admin"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<UserView> adminRegister(@RequestBody @Valid RegisterRequest registerRequest) {
        UserView created = authenticationService.adminRegister(registerRequest);
        URI location = URI.create("api/v1/admin/" + created.id());
        return ResponseEntity.created(location).body(created);
    }
}
