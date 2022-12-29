package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.GameSessionDto;
import sigma.internship.petProject.entity.*;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.GameSessionMapper;
import sigma.internship.petProject.repository.GameRepository;
import sigma.internship.petProject.repository.GameSessionRepository;
import sigma.internship.petProject.repository.MoneyBalanceRepository;
import sigma.internship.petProject.repository.ResultRepository;
import sigma.internship.petProject.repository.RoundRepository;
import sigma.internship.petProject.repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final GameRepository gameRepository;
    private final GameSessionMapper gameSessionMapper;
    private final UserRepository userRepository;
    private final MoneyBalanceRepository moneyBalanceRepository;
    private final ResultRepository resultRepository;
    private final RoundRepository roundRepository;

    @Override
    public GameSessionDto createGameSession(long gameId, int roundAmount) {
        log.info("Starting creating a new game session");

        Game game = getGame(gameId);
        User user = getUser();

        validateMoneyBalance(game.getCost(), roundAmount, user);

        return gameSessionMapper.gameSessionToGameSessionDto(
                gameSessionRepository.save(
                        GameSession
                                .builder()
                                .game(game)
                                .player(user)
                                .rounds(getRounds(game, roundAmount))
                                .build()));
    }

    private void validateMoneyBalance(BigDecimal gameCost, int roundAmount, User user) {
        log.info("Starting validating user's money balance");
        Optional<MoneyBalance> moneyBalanceOptional = moneyBalanceRepository.findByPlayerId(user.getId());
        if (moneyBalanceOptional.isEmpty()) {
            log.error("Money balance for user with id \"{}\" is not found", user.getId());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with retrieving user's money balance");
        }
        if (0 > moneyBalanceOptional.get().getAmount().compareTo(gameCost.multiply(BigDecimal.valueOf(roundAmount)))) {
            log.error("User \"{}\" money balance is not enough to create game session", user.getUsername());
            throw new WebException(HttpStatus.PAYMENT_REQUIRED, "User is lack of money to start game session");
        }
    }

    private Set<Round> getRounds(Game game, int roundAmount) {
        Set<Round> rounds = new HashSet<>();

        if (game.isOneUserAction()) {
            if (roundAmount < 1) {
                log.error("Bad amount of rounds: {}", roundAmount);
                throw new WebException(HttpStatus.BAD_REQUEST, "Bad amount of rounds");
            }
            rounds.addAll(generateRounds(game.getWinning(), roundAmount, game.getCost()));
        } else {
            rounds.addAll(generateRounds(game.getWinning(), 1, game.getCost()));
        }

        return rounds;
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
        AtomicReference<BigDecimal> resultMoney = new AtomicReference<>();
        return Stream
                .generate(() -> {
                    resultMoney.set(
                            Math.random() <= winning
                                    ? bet.multiply(BigDecimal.valueOf(1.5))
                                    : BigDecimal.valueOf(0));

                    return roundRepository.save(Round
                            .builder()
                            .playerBet(bet)
                            .result(resultRepository.save(Result
                                    .builder()
                                    .amount(resultMoney.get())
                                    .type(resultMoney.get().equals(BigDecimal.valueOf(0))
                                            ? ResultType.LOSE
                                            : ResultType.WIN)
                                    .build()))
                            .build());
                })
                .limit(roundAmount)
                .collect(Collectors.toSet());
    }

    @Override
    public Page<GameSessionDto> findAll(Pageable pageable) {
        return gameSessionRepository.findAll(pageable).map(gameSessionMapper::gameSessionToGameSessionDto);
    }
}
