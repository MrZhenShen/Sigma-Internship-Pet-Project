package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.TwitDto;
import sigma.internship.petProject.service.TwitService;

@Tag(name = "Twits controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/twit")
public class TwitController {
    private final TwitService twitService;

    @Operation(summary = "Find all twits")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of twits")
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Page<TwitDto> findAll(@ParameterObject Pageable pageable) {
        return twitService.findAll(pageable);
    }

    @Operation(summary = "Find twit by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the twit"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Twit not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public TwitDto findById(@PathVariable long id) {
        return twitService.findById(id);
    }
}
