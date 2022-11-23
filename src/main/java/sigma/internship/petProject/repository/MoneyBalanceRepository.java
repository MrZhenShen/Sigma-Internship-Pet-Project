package sigma.internship.petProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.internship.petProject.entity.MoneyBalance;

import java.util.Optional;

@Repository
public interface MoneyBalanceRepository extends JpaRepository<MoneyBalance, Long> {

    Optional<MoneyBalance> findByUserId(Long userId);
}
