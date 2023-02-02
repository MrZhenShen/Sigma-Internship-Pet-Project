package sigma.internship.petProject.dto;

import sigma.internship.petProject.entity.RoleType;

import java.io.Serializable;

/**
 * A DTO for the {@link sigma.internship.petProject.entity.Role} entity
 */
public record RoleDto(RoleType type) implements Serializable {
}