package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sigma.internship.petProject.entity.MoneyBalance;

@Tag(name = "Money Balance controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/money-balance")
public class MoneyBalanceController {

    @Operation(summary = "Deposit money to authorized user")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Successful deposit"),
    })
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/deposit")
    public MoneyBalance deposit(@RequestParam(value = "amount") double amount) {
        return new MoneyBalance();
    }
}
