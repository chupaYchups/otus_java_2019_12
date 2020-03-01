package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;

import java.util.List;

public interface ATMCell {

    List<Bill> getBills(int quantity);

    void putBill(Bill bill);

    int getHowMuchHave(int qty);

    BillNominal getNominal();

    int getBalance();
}
