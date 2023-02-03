package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.service.UserService;

@Tag(name = "User Controller")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get authenticated user's data")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User's data is found"),
            @ApiResponse(responseCode = "401", description = "User is  not authorized"),
            @ApiResponse(responseCode = "401", description = "Access is forbidden"),
            @ApiResponse(responseCode = "409", description = "User with such credentials already exist"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserDto user(@AuthenticationPrincipal User user) {
        return userService.findByUsername(user.getUsername());
    }

    @Operation(summary = "Find all users")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of users")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public Page<UserDto> getAllUsers(@ParameterObject Pageable pageable) {
        return userService.getAllUsers(pageable);
    }
}
