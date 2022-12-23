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
import sigma.internship.petProject.dto.ResultDto;
import sigma.internship.petProject.service.ResultService;

@Tag(name = "Result controller")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/result")
public class ResultController {

    private final ResultService resultService;

    @Operation(summary = "Find all results")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of results")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public Page<ResultDto> getAllUsers(@ParameterObject Pageable pageable) {
        return resultService.findAll(pageable);
    }
}
