package sigma.internship.petProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.MoneyBalanceDto;
import sigma.internship.petProject.entity.MoneyBalance;
import sigma.internship.petProject.entity.MoneyBalanceMapper;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.repository.MoneyBalanceRepository;
import sigma.internship.petProject.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoneyBalanceServiceImpl implements MoneyBalanceService {

    private final MoneyBalanceRepository moneyBalanceRepository;
    private final UserRepository userRepository;
    private final MoneyBalanceMapper moneyBalanceMapper;

    @Override
    public MoneyBalanceDto deposit(double amount) {
        User user = getUser();

        MoneyBalance moneyBalance = getMoneyBalanceByUserId(user.getId());
        if (amount < 1) {
            log.info("Deposit is {} USD that is less than 0 from user: {}", amount, user.getUsername());
            throw new WebException(HttpStatus.PAYMENT_REQUIRED, "Issue with updating money balance");
        }

        moneyBalance.setAmount(moneyBalance.getAmount().add(BigDecimal.valueOf(amount)));

        MoneyBalance updatedMoneyBalance = moneyBalanceRepository.save(moneyBalance);

        if (!moneyBalance.getAmount().equals(updatedMoneyBalance.getAmount())) {
            log.error("Issue with updating money balance for user: \"{}\"", user.getUsername());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with updating money balance");
        }

        return moneyBalanceMapper.moneyBalanceToMoneyBalanceDto(updatedMoneyBalance);
    }

    private User getUser() {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(authenticatedUser.getUsername());
        if (userOptional.isEmpty()) {
            log.error("\"{}\" - user is not found", authenticatedUser.getUsername());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving user");
        }
        return userOptional.get();
    }

    private MoneyBalance getMoneyBalanceByUserId(long id) {
        log.info("Starting retrieving user's money balance: {}", id);
        Optional<MoneyBalance> moneyBalanceOptional = moneyBalanceRepository.findByPlayerId(id);
        if (moneyBalanceOptional.isEmpty()) {
            log.error("Money balance is not found");
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving money balance");
        }
        return moneyBalanceOptional.get();
    }
}
