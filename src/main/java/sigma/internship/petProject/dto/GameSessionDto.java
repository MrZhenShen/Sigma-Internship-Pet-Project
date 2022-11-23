package sigma.internship.petProject.dto;

import sigma.internship.petProject.entity.Game;
import sigma.internship.petProject.entity.Round;
import sigma.internship.petProject.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.GameSession} entity
 */
public record GameSessionDto(
        long id,
        @NotNull @NotBlank @NotEmpty Game game,
        @NotNull @NotBlank @NotEmpty User player,
        Set<Round> rounds) {
}