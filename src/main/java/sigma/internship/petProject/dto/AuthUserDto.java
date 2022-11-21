package sigma.internship.petProject.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record AuthUserDto(
        long id,
        @NotNull @NotBlank @NotEmpty String username,
        @NotNull @NotBlank @NotEmpty @Email String email,
        @NotNull @NotBlank @NotEmpty String password) {
}
