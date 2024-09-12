package sigma.internship.petProject.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sigma.internship.petProject.dto.GameSessionDto;
import sigma.internship.petProject.entity.GameSession;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface GameSessionMapper {
    GameSession gameSessionDtoToGameSession(GameSessionDto gameSessionDto);

    GameSessionDto gameSessionToGameSessionDto(GameSession gameSession);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GameSession updateGameSessionFromGameSessionDto(GameSessionDto gameSessionDto, @MappingTarget GameSession gameSession);

    @AfterMapping
    default void linkRounds(@MappingTarget GameSession gameSession) {
        gameSession.getRounds().forEach(round -> round.setGameSession(gameSession));
    }
}
