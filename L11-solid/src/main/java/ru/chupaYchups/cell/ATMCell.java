package ru.chupaYchups.cell;

import ru.chupaYchups.bill.Bill;
import java.util.List;

public interface ATMCell {
    List<Bill> getBills(int quantity);
    void putBill(Bill bill);
    int getQuantity();
    boolean isEmpty();
}
