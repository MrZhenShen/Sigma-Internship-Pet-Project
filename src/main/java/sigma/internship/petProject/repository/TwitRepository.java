package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.internship.petProject.entity.Twit;

public interface TwitRepository extends JpaRepository<Twit, Long> {
}
