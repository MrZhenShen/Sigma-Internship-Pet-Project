package sigma.internship.petProject.mapper;

import org.mapstruct.Mapper;
import sigma.internship.petProject.dto.GameSessionDto;
import sigma.internship.petProject.entity.GameSession;

@Mapper(componentModel = "spring")
public interface GameSessionMapper {

    GameSession gameSessionDtoToGameSession(GameSessionDto gameSessionDto);

    GameSessionDto gameSessionToGameSessionDto(GameSession gameSession);
}
