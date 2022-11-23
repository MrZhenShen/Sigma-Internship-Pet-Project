package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.GameSessionDto;

import java.math.BigDecimal;

public interface GameSessionService {
    GameSessionDto createGameSession(long gameId, int roundAmount, BigDecimal bet);

    Page<GameSessionDto> findAll(Pageable pageable);
}
