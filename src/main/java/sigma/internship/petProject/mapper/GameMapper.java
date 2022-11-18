package sigma.internship.petProject.mapper;

import org.mapstruct.Mapper;
import sigma.internship.petProject.dto.GameDto;
import sigma.internship.petProject.dto.GameShortDto;
import sigma.internship.petProject.entity.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameDto toDto(Game game);

    GameShortDto toShortDto(Game game);

    Game toEntity(GameDto gameDto);
}
