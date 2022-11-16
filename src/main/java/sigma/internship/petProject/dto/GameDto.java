package sigma.internship.petProject.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GameDto(
        long id,
        @NotNull @NotBlank @NotEmpty String title,
        @NotNull @NotBlank @NotEmpty String description,
        @NotNull @NotBlank @NotEmpty boolean isOneUserAction,
        @NotNull @NotBlank @NotEmpty double winning,
        @NotNull @NotBlank @NotEmpty BigDecimal cost,
        LocalDateTime createDate
) {
}
