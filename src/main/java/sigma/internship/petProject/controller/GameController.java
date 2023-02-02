package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.GameDto;
import sigma.internship.petProject.dto.GameShortDto;
import sigma.internship.petProject.service.GameService;

import java.util.List;

@Tag(name = "Game Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    @Operation(summary = "Find all games with details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of games"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/detailed")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<GameDto> findAll() {
        return gameService.findAll();
    }

    @Operation(summary = "Find all games")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of games"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public List<GameShortDto> findAllPreviews() {
        return gameService.findAllPreviews();
    }

    @Operation(summary = "Find game by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The game is found"),
            @ApiResponse(responseCode = "404", description = "The game is not found")
    })
    @GetMapping("/{id}")
    public GameShortDto findById(@PathVariable long id) {
        return gameService.findById(id);
    }
}
