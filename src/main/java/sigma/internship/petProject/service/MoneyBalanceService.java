package sigma.internship.petProject.service;

import sigma.internship.petProject.dto.MoneyBalanceDto;

public interface MoneyBalanceService {

    MoneyBalanceDto deposit(double amount);

    MoneyBalanceDto findMoneyBalance();
}
