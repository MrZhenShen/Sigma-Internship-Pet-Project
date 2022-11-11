package sigma.internship.petProject.service;

import lombok.RequiredArgsConstructor;
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
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public List<GameShortDto> findAllPreviews() {
        List<Game> gameList = gameRepository.findAll();
        if (gameList.size() > 0) {
            return gameList
                    .stream()
                    .map(gameMapper::toShortDto)
                    .toList();
        } else {
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @Override
    public List<GameDto> findAll() {
        List<Game> gameList = gameRepository.findAll();
        if (gameList.size() > 0) {
            return gameList
                    .stream()
                    .map(gameMapper::toDto)
                    .toList();
        } else {
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @Override
    public GameShortDto findById(long id) {
        return gameRepository
                .findById(id)
                .map(gameMapper::toShortDto)
                .orElseThrow(() -> new WebException(HttpStatus.NOT_FOUND, "Game not found"));
    }
}
