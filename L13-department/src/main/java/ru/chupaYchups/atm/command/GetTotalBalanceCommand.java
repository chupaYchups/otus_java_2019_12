package ru.chupaYchups.atm.command;

import ru.chupaYchups.atm.Atm;

public class GetTotalBalanceCommand implements AtmCommandResultable<Integer> {

    private int totalBalance;

    @Override
    public void execute(Atm atm) {
        totalBalance += atm.getBalance();
    }

    @Override
    public Integer getResult() {
        return totalBalance;
    }
}
