package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;

import java.util.List;

public interface Atm {
    List<Bill> getSumm(int summ);
    void putSumm(List<Bill> billList);
    int getBalance();
}
