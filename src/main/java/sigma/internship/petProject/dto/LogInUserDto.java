package sigma.internship.petProject.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.User} entity
 */
public record LogInUserDto(
        @NotNull @NotBlank @NotEmpty String username,
        @NotNull @NotBlank @NotEmpty String password
) {
}
