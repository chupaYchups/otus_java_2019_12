package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;

import java.util.List;

public interface ATM {
    void getSumm(int summ);
    void putSumm(List<Bill> billList);
    void getBalance();
}
