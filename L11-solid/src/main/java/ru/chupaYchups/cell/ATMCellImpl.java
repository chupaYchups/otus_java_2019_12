package ru.chupaYchups.cell;

import ru.chupaYchups.bill.Bill;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATMCellImpl implements ATMCell {

    private List<Bill> billList;

    public ATMCellImpl() {
        billList = new ArrayList<>();
    }

    @Override
    public List<Bill> getBills(int quantity) {
        int availableQty = billList.size() - quantity;
        return Collections.unmodifiableList(billList.subList(0, billList.size() - availableQty));
    }

    @Override
    public void putBill(Bill bill) {
        billList.add(bill);
    }

    @Override
    public int getQuantity() {
        return billList.size();
    }

    @Override
    public boolean isEmpty() {
        return billList.isEmpty();
    }
}
