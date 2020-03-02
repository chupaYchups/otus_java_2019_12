package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;

import java.util.List;

public interface ATMCell extends AtmCellCommandExecutor {

    List<Bill> getBills(int quantity);

    void putBill(Bill bill);

    void putBillList(List<Bill> billList);

    int getHowMuchHave(int qty);

    BillNominal getNominal();

    int getBalance();
}
