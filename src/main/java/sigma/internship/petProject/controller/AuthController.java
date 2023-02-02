package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.LogInUserDto;
import sigma.internship.petProject.dto.SignUpUserDto;
import sigma.internship.petProject.dto.TokenDto;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.mapper.UserMapper;
import sigma.internship.petProject.security.TokenGenerator;

import javax.persistence.Transient;
import javax.validation.Valid;
import java.util.Collections;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @Autowired
    UserMapper userMapper;

    @Operation(summary = "Register new user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User is created"),
            @ApiResponse(responseCode = "500", description = "Issue with data"),
            @ApiResponse(responseCode = "409", description = "User with such credentials already exist")
    })
    @PostMapping("/register")
    @Transient
    public TokenDto register(@Valid @RequestBody SignUpUserDto signUpUserDto) {
        User user = userMapper.toEntity(signUpUserDto);
        userDetailsManager.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signUpUserDto.password(), Collections.emptyList());
        return tokenGenerator.createToken(authentication);
    }

    @Operation(summary = "Log In")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token is created"),
            @ApiResponse(responseCode = "400", description = "Authentication principal is not User entity format"),
            @ApiResponse(responseCode = "500", description = "User is not found")
    })
    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LogInUserDto logInUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(logInUserDto.username(), logInUserDto.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenGenerator.createToken(authentication);
    }

    @Operation(summary = "Get a new access token using refresh token")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token is refreshed"),
            @ApiResponse(responseCode = "400", description = "Principal is not of User type"),
            @ApiResponse(responseCode = "500", description = "Invalid token")
    })
    @PostMapping("/token")
    public TokenDto token(@RequestBody TokenDto tokenDto) {
        return tokenGenerator.createToken(refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDto.refreshToken())));
    }
}