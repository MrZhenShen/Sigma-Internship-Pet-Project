package sigma.internship.petProject.dto;

public record TokenDto(
        long userId,
        String accessToken,
        String refreshToken
) {
}
