package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.department.AtmCommandListener;

import java.util.List;

public interface Atm extends AtmCommandListener {
    List<Bill> getSumm(int summ);
    void putSumm(List<Bill> billList);
    int getBalance();
    void resetToInitialState();
}
