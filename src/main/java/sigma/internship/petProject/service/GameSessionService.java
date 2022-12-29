package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.GameSessionDto;
import sigma.internship.petProject.dto.ResultDto;

import java.util.List;

public interface GameSessionService {
    List<ResultDto> createGameSession(long gameId, int roundAmount);

    Page<GameSessionDto> findAll(Pageable pageable);
}
