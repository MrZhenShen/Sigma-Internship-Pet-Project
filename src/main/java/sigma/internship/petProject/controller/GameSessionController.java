package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.ResultDto;
import sigma.internship.petProject.service.GameSessionService;

import java.util.List;

@Tag(name = "Game Session Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/game-session")
public class GameSessionController {
    private final GameSessionService gameSessionService;

    @Operation(summary = "Create a new game session")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Game session is successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "402", description = "User is lack of money"),
            @ApiResponse(responseCode = "403", description = "Forbidden for the user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public List<ResultDto> createGameSession(
            @RequestParam(value = "game") long gameId,
            @RequestParam(value = "rounds", defaultValue = "1") int rounds) {
        return gameSessionService.createGameSession(gameId, rounds);
    }
}
