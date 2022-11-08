package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.internship.petProject.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
