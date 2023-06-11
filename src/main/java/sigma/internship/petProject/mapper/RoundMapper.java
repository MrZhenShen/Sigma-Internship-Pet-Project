package sigma.internship.petProject.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sigma.internship.petProject.dto.RoundDto;
import sigma.internship.petProject.entity.Round;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoundMapper {
    Round roundDtoToRound(RoundDto roundDto);

    RoundDto roundToRoundDto(Round round);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Round updateRoundFromRoundDto(RoundDto roundDto, @MappingTarget Round round);
}
