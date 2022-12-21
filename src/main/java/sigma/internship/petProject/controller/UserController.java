package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.service.UserService;

import javax.validation.Valid;

@Tag(name = "User controller")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Register new user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    public UserDto createUser(@Valid @RequestBody AuthUserDto user) {
        return userService.register(user);
    }

    @Operation(summary = "Authentication")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/login")
    public UserDto getAuthedUser() {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userService.getUserByUsername(authenticatedUser.getUsername());
    }

}
