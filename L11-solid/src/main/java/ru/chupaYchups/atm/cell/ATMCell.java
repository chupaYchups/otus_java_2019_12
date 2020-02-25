package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import java.util.List;

public interface ATMCell {
    List<Bill> getBills(int quantity);
    void putBills(List<Bill> billList);
    int getQuantity();
}
