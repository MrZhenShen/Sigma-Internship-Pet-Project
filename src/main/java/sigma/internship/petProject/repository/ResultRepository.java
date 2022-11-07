package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.internship.petProject.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
}
