package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.internship.petProject.entity.GameSession;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
}
