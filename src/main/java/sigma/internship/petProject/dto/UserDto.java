package sigma.internship.petProject.dto;

public record UserDto(
        long id,
        String firstName,
        String secondName,
        String username,
        String email,
        RoleDto role) {
}
