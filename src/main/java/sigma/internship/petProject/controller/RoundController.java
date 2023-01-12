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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.RoundDto;
import sigma.internship.petProject.service.RoundService;

@Tag(name = "Round")
@RestController
@RequiredArgsConstructor
@RequestMapping("/round")
public class RoundController {

    private final RoundService roundService;

    @Operation(summary = "Find all rounds")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of rounds")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public Page<RoundDto> getAllResults(@ParameterObject Pageable pageable) {
        return roundService.findAll(pageable);
    }

}
