package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.GameSessionDto;

public interface GameSessionService {
    GameSessionDto createGameSession(long gameId, int roundAmount);

    Page<GameSessionDto> findAll(Pageable pageable);
}
