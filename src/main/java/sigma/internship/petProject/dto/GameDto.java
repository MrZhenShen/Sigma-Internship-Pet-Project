package sigma.internship.petProject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GameDto(
        long id,
        String title,
        String description,
        boolean isOneUserAction,
        double winning,
        BigDecimal cost,
        LocalDateTime createDate
) {
}
