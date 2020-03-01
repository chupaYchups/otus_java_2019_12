package ru.chupaYchups.atm.operation;

import ru.chupaYchups.atm.ATM;

public class GetSummCommand implements AtmCommand {

    private int summ;

    public GetSummCommand (int summ) {
        this.summ = summ;
    }

    @Override
    public void execute(ATM atm) {
        atm.getSumm(summ);
    }
}
