package sigma.internship.petProject.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sigma.internship.petProject.dto.ResultDto;
import sigma.internship.petProject.entity.Result;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ResultMapper {
    Result resultDtoToResult(ResultDto resultDto);

    ResultDto resultToResultDto(Result result);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Result updateResultFromResultDto(ResultDto resultDto, @MappingTarget Result result);
}
