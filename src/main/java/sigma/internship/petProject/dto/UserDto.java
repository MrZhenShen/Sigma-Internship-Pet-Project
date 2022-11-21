package sigma.internship.petProject.dto;

import sigma.internship.petProject.entity.Role;

public record UserDto(
        long id,
        String username,
        String email,
        Role role) {
}
