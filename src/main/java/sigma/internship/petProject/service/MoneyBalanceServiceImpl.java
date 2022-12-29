package sigma.internship.petProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.MoneyBalanceDto;
import sigma.internship.petProject.entity.MoneyBalance;
import sigma.internship.petProject.mapper.MoneyBalanceMapper;
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

        if (amount < 1) {
            log.info("Deposit is {} USD that is less than 0 from user: {}", amount, user.getUsername());
            throw new WebException(HttpStatus.PAYMENT_REQUIRED, "Issue with updating money balance");
        }
        MoneyBalance moneyBalance = getMoneyBalanceByUserId(user.getId());

        moneyBalance.setAmount(moneyBalance.getAmount().add(BigDecimal.valueOf(amount)));

        MoneyBalance updatedMoneyBalance = moneyBalanceRepository.save(moneyBalance);

        if (!moneyBalance.getAmount().equals(updatedMoneyBalance.getAmount())) {
            log.error("Issue with updating money balance for user: \"{}\"", user.getUsername());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with updating money balance");
        }

        return moneyBalanceMapper.moneyBalanceToMoneyBalanceDto(updatedMoneyBalance);
    }

    @Override
    public MoneyBalanceDto withdraw(double amount) {
        log.info("Starting validating withdrawal amount");
        if (amount < 10.00) {
            log.error("Withdraw request is less than 10 USD: {}", amount);
            throw new WebException(HttpStatus.BAD_REQUEST, "Withdraw request must be more than 10 USD");
        }
        log.info("Withdraw amount is valid");

        User user = getUser();
        MoneyBalance moneyBalance = getMoneyBalanceByUserId(user.getId());
        BigDecimal amountBigDecimal = BigDecimal.valueOf(amount);

        log.info("Starting validating user's money balance");
        if (moneyBalance.getAmount().compareTo(amountBigDecimal) < 0) {
            log.error("User money is less than 11: {}", user.getUsername());
            throw new WebException(HttpStatus.BAD_REQUEST, "User balance cannot process the withdrawal");
        }
        log.info("User's money balance is valid");

        moneyBalance.setAmount(moneyBalance.getAmount().subtract(amountBigDecimal));

        MoneyBalance updatedMoneyBalance = moneyBalanceRepository.save(moneyBalance);

        if (!moneyBalance.getAmount().equals(updatedMoneyBalance.getAmount())) {
            log.error("Issue with updating money balance for user: \"{}\"", user.getUsername());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with updating money balance");
        }

        return moneyBalanceMapper.moneyBalanceToMoneyBalanceDto(updatedMoneyBalance);
    }

    @Override
    public MoneyBalanceDto findMoneyBalance() {
        return moneyBalanceMapper.moneyBalanceToMoneyBalanceDto(getMoneyBalanceByUserId(getUser().getId()));
    }

    private User getUser() {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        log.info("Starting retrieving user");
        Optional<User> userOptional = userRepository.findByUsername(authenticatedUser.getUsername());
        if (userOptional.isEmpty()) {
            log.error("\"{}\" - user is not found", authenticatedUser.getUsername());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving user");
        }
        return userOptional.get();
    }

    private MoneyBalance getMoneyBalanceByUserId(long id) {
        log.info("Starting retrieving user's money balance");
        Optional<MoneyBalance> moneyBalanceOptional = moneyBalanceRepository.findByPlayerId(id);
        if (moneyBalanceOptional.isEmpty()) {
            log.error("Money balance is not found for user with id: {}", id);
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving money balance");
        }
        return moneyBalanceOptional.get();
    }
}
