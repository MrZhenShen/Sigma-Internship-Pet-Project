package sigma.internship.petProject.service;

import sigma.internship.petProject.dto.GameDto;

import java.util.List;

public interface RoundService {

    List<GameDto> findAll();

    List<GameDto> findAllByGameSessionId(long id);

    List<GameDto> findAllByUserId(long id);

    List<GameDto> findAllByGameId(long id);

    List<GameDto> findAllByResultType(long id);
}
