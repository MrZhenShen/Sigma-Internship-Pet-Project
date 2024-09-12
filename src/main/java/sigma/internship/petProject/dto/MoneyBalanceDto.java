package sigma.internship.petProject.dto;

import java.math.BigDecimal;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.MoneyBalance} entity
 */
public record MoneyBalanceDto(BigDecimal amount) {
}