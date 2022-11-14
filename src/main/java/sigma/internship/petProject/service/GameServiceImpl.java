package sigma.internship.petProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.GameDto;
import sigma.internship.petProject.dto.GameShortDto;
import sigma.internship.petProject.entity.Game;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.GameMapper;
import sigma.internship.petProject.repository.GameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public List<GameShortDto> findAllPreviews() {
        log.info("Started retrieving all game objects");
        List<Game> gameList = gameRepository.findAll();
        if (gameList.size() > 0) {
            log.info("{} game objects were retrieved from database", gameList.size());
            return gameList
                    .stream()
                    .map(gameMapper::toShortDto)
                    .toList();
        } else {
            log.error("Retrieving all game data: {}", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @Override
    public List<GameDto> findAll() {
        log.info("Started retrieving all game objects");
        List<Game> gameList = gameRepository.findAll();
        if (gameList.size() > 0) {
            log.info("{} game objects were retrieved from database", gameList.size());
            return gameList
                    .stream()
                    .map(gameMapper::toDto)
                    .toList();
        } else {
            log.error("Retrieving all game data: {}", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @Override
    public GameShortDto findById(long id) {
        log.info("Started retrieving game object with id: {}", id);
        return gameRepository
                .findById(id)
                .map(gameMapper::toShortDto)
                .orElseThrow(() -> {
                    log.error("Game object with id: {} was not found", id);
                    throw new WebException(HttpStatus.NOT_FOUND, "Game not found");
                });
    }
}
