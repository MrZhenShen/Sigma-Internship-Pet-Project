package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.internship.petProject.entity.Round;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

    List<Round> findByGameSession(long id);

}