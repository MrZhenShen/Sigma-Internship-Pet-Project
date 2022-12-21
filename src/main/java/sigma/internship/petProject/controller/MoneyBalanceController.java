package sigma.internship.petProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.internship.petProject.dto.MoneyBalanceDto;
import sigma.internship.petProject.service.MoneyBalanceService;

@Tag(name = "Money Balance controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/money-balance")
public class MoneyBalanceController {

    private final MoneyBalanceService moneyBalanceService;

    @Operation(summary = "Deposit money to authorized user")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Successful deposit"),
    })
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/deposit")
    public MoneyBalanceDto deposit(@RequestParam(value = "amount") double amount) {
        return moneyBalanceService.deposit(amount);
    }


    @Operation(summary = "View own money balance")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful money balance retrieving"),
    })
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public MoneyBalanceDto findMoneyBalance() {
        return moneyBalanceService.findMoneyBalance();
    }

}
