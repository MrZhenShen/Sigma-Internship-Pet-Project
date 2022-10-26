package sigma.internship.petProject.mapper;

import org.mapstruct.Mapper;
import sigma.internship.petProject.dto.TwitDto;
import sigma.internship.petProject.entity.Twit;

@Mapper(componentModel = "spring")
public interface TwitMapper {
    TwitDto toDto(Twit twit);
}
