package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;

import java.util.List;

public interface ATM {
    List<Bill> getSumm(int summ);
    void putSumm(List<Bill> billList);
    int getBalance();
}
