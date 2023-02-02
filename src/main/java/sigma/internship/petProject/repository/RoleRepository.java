package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.internship.petProject.entity.Role;
import sigma.internship.petProject.entity.RoleType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByType(RoleType name);
}
