package sigma.internship.petProject.dto;

public record AuthUserDto(
        long id,
        String username,
        String email,
        String password) {
}
