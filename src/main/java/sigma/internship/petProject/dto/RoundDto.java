package sigma.internship.petProject.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.Round} entity
 */
public record RoundDto(long id, GameSessionDto gameSession, BigDecimal playerBet,
                       ResultDto result) implements Serializable {
}