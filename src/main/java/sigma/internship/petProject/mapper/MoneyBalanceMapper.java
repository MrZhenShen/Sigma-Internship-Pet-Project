package sigma.internship.petProject.mapper;

import org.mapstruct.*;
import sigma.internship.petProject.dto.MoneyBalanceDto;
import sigma.internship.petProject.entity.MoneyBalance;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MoneyBalanceMapper {
    MoneyBalance moneyBalanceDtoToMoneyBalance(MoneyBalanceDto moneyBalanceDto);

    MoneyBalanceDto moneyBalanceToMoneyBalanceDto(MoneyBalance moneyBalance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MoneyBalance updateMoneyBalanceFromMoneyBalanceDto(MoneyBalanceDto moneyBalanceDto, @MappingTarget MoneyBalance moneyBalance);
}
