package sigma.internship.petProject.dto;

import java.math.BigDecimal;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.Round} entity
 */
public record RoundDto(long id, BigDecimal playerBet, ResultDto result) {
}