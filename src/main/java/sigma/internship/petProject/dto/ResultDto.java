package sigma.internship.petProject.dto;

import sigma.internship.petProject.entity.ResultType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.Result} entity
 */
public record ResultDto(long id, ResultType type, BigDecimal amount) implements Serializable {
}