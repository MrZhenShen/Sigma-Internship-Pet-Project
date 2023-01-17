package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.ResultDto;
import sigma.internship.petProject.entity.Game;
import sigma.internship.petProject.entity.GameSession;
import sigma.internship.petProject.entity.MoneyBalance;
import sigma.internship.petProject.entity.Result;
import sigma.internship.petProject.entity.ResultType;
import sigma.internship.petProject.entity.Round;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.ResultMapper;
import sigma.internship.petProject.repository.GameRepository;
import sigma.internship.petProject.repository.GameSessionRepository;
import sigma.internship.petProject.repository.MoneyBalanceRepository;
import sigma.internship.petProject.repository.ResultRepository;
import sigma.internship.petProject.repository.RoundRepository;
import sigma.internship.petProject.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final MoneyBalanceRepository moneyBalanceRepository;
    private final ResultRepository resultRepository;
    private final RoundRepository roundRepository;

    private final ResultMapper resultMapper;

    @Override
    public List<ResultDto> createGameSession(long gameId, int roundAmount) {
        log.info("Starting creating a new game session");

        Game game = getGame(gameId);
        User user = getUser();
        MoneyBalance moneyBalance = findMoneyBalance(user);

        validateMoneyBalance(moneyBalance, game.getCost(), roundAmount);

        List<Round> roundCollection = getRounds(game, roundAmount)
                .stream()
                .peek(round -> round.setGameSession(gameSessionRepository.save(GameSession
                        .builder()
                        .game(game)
                        .player(user)
                        .build()))
                )
                .toList();

        roundCollection = roundRepository.saveAll(roundCollection);

        moneyBalance.setAmount(moneyBalance
                .getAmount()
                .add(countMoneyResult(roundCollection))
                .subtract(game.getCost().multiply(BigDecimal.valueOf(roundAmount))));

        moneyBalanceRepository.save(moneyBalance);

        return roundCollection.stream().map(Round::getResult).map(resultMapper::resultToResultDto).toList();
    }

    private BigDecimal countMoneyResult(List<Round> roundCollection) {
        if (roundCollection.size() > 1) {
            return roundCollection
                    .stream()
                    .map(Round::getResult)
                    .map(Result::getAmount)
                    .reduce(BigDecimal::add)
                    .get();
        } else {
            return roundCollection.get(0).getResult().getAmount();
        }
    }

    private MoneyBalance findMoneyBalance(User user) {
        log.info("Starting retrieving user's money balance");
        Optional<MoneyBalance> moneyBalanceOptional = moneyBalanceRepository.findByPlayerId(user.getId());

        if (moneyBalanceOptional.isEmpty()) {
            log.error("Money balance for user with id \"{}\" is not found", user.getId());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving user's money balance");
        }

        log.info("Money balance is found");
        return moneyBalanceOptional.get();
    }

    private void validateMoneyBalance(MoneyBalance moneyBalance, BigDecimal gameCost, int roundAmount) {
        log.info("Starting validating money balance amount");
        BigDecimal costForGameSession = gameCost.multiply(BigDecimal.valueOf(roundAmount));

        if (compareMoneyBalanceAmountWithCostForGameSession(moneyBalance, costForGameSession)) {
            log.error("User \"{}\" money balance is not enough to create game session", moneyBalance.getPlayer().getUsername());
            throw new WebException(HttpStatus.PAYMENT_REQUIRED, "User is lack of money to start game session");
        }

        log.info("Money balance amount is valid");
    }


    private boolean compareMoneyBalanceAmountWithCostForGameSession(MoneyBalance moneyBalance, BigDecimal costForGameSession) {
        return 0 > moneyBalance.getAmount().compareTo(costForGameSession);
    }

    private Collection<Round> getRounds(Game game, int roundAmount) {
        if (game.isOneUserAction()) {
            if (roundAmount < 1) {
                log.error("Bad amount of rounds: {}", roundAmount);
                throw new WebException(HttpStatus.BAD_REQUEST, "Bad amount of rounds");
            }
            return generateRounds(game.getWinning(), roundAmount, game.getCost());
        } else {
            return generateRounds(game.getWinning(), 1, game.getCost());
        }
    }

    private User getUser() {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(authenticatedUser.getUsername());

        if (userOptional.isEmpty()) {
            log.error("User with username \"{}\" is not found", authenticatedUser.getUsername());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving user");
        }

        return userOptional.get();
    }

    private Game getGame(long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);

        if (gameOptional.isEmpty()) {
            log.error("Game with id \"{}\" is not found", gameId);
            throw new WebException(HttpStatus.BAD_REQUEST, "Game does not exist");
        }

        return gameOptional.get();
    }

    private Collection<Round> generateRounds(double winning, int roundAmount, BigDecimal bet) {
        return Stream
                .generate(() -> {
                    final BigDecimal resultMoney;
                    if (Math.random() <= winning) {
                        resultMoney = bet.multiply(BigDecimal.valueOf(1.5));
                    } else {
                        resultMoney = BigDecimal.ZERO;
                    }

                    return Round
                            .builder()
                            .playerBet(bet)
                            .result(resultRepository.save(Result
                                    .builder()
                                    .amount(resultMoney)
                                    .type(resultMoney.equals(BigDecimal.valueOf(0))
                                            ? ResultType.LOSE
                                            : ResultType.WIN)
                                    .build()))
                            .build();
                })
                .limit(roundAmount)
                .collect(Collectors.toSet());
    }
}
