package sigma.internship.petProject.service;

import sigma.internship.petProject.dto.GameDto;
import sigma.internship.petProject.dto.GameShortDto;

import java.util.List;

public interface GameService {
    List<GameShortDto> findAllPreviews();

    List<GameDto> findAll();

    GameShortDto findById(long id);

}
