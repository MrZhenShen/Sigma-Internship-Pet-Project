package sigma.internship.petProject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.Assert;
import sigma.internship.petProject.dto.ResultDto;
import sigma.internship.petProject.entity.Result;
import sigma.internship.petProject.entity.Round;
import sigma.internship.petProject.mapper.ResultMapper;
import sigma.internship.petProject.repository.RoundRepository;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = {
                "/scripts/create/create-game.sql",
                "/scripts/create/create-user.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
        scripts = {
                "/scripts/delete/clear-game-session.sql",
                "/scripts/delete/clear-user.sql",
                "/scripts/delete/clear-game.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GameSessionServiceTest {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private ResultMapper resultMapper;

    private static final String MOCK_USER_WITH_MONEY = "userWithMoney";
    private static final String MOCK_USER_ROLE = "USER";

    @Test
    @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
    void Should_CreateOneRound_When_OneRoundWasRequested() {
        int rounds = 1;

        List<ResultDto> resultDtoList = gameSessionService.createGameSession(1, rounds);

        Assert.isTrue(resultDtoList.size() == rounds, "Requested amount of rounds were not created");
    }

    @Test
    @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
    void Should_CreateOneRound_When_GameIsManyActions() {
        int rounds = 2;

        List<ResultDto> resultDtoList = gameSessionService.createGameSession(1, rounds);

        Assert.isTrue(resultDtoList.size() == 1, "Requested amount of rounds were not created");
    }

    @Test
    @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
    void Should_CreateRounds_When_GameIsOneAction() {
        int rounds = 2;

        List<ResultDto> resultDtoList = gameSessionService.createGameSession(2, rounds);

        Assert.isTrue(resultDtoList.size() == rounds, "Requested amount of rounds were not created");
    }

    @Test
    @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
    void Should_CreateRoundsWithItsResult_When_GameIsOneAction() {
        int rounds = 2;

        List<ResultDto> resultDtoList = gameSessionService.createGameSession(2, rounds);
        List<Round> roundList = roundRepository.findAll();

        Assert.isTrue(resultDtoList.size() == roundList.size(), "Requested amount of rounds were not created");
    }

    @Test
    @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
    void Should_CreateOneGameSessionForAllRounds_When_GameSessionIsCreated() {
        int rounds = 2;

        Result createdResult = resultMapper
                .resultDtoToResult(gameSessionService
                        .createGameSession(2, rounds)
                        .stream()
                        .toList()
                        .get(0)
                );

        List<Round> roundList = roundRepository
                .findAll()
                .stream()
                .filter(result -> result.getResult().equals(createdResult))
                .toList();

        Assert.isTrue(roundList.stream().map(Round::getGameSession).distinct().count() <= 1, "Many game sessions were created instead of 1");
    }
}
