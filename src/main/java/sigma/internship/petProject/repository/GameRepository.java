package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.internship.petProject.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
