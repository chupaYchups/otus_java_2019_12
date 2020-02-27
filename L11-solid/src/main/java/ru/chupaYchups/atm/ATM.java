package ru.chupaYchups.atm;

import ru.chupaYchups.bill.Bill;

import java.util.List;

public interface ATM {
    List<Bill> getSumm(int summ);
    void putSumm(List<Bill> billList);
    void getBalance();
}
