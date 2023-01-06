package sigma.internship.petProject.service;

import sigma.internship.petProject.dto.ResultDto;

import java.util.List;

public interface GameSessionService {
    List<ResultDto> createGameSession(long gameId, int roundAmount);
}
