package sigma.internship.petProject.dto;

import java.math.BigDecimal;

public record GameShortDto(
        long id,
        String title,
        String description,
        BigDecimal cost
) {
}
