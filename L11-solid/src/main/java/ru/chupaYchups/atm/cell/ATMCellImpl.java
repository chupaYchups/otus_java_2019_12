package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;

import java.util.ArrayList;
import java.util.List;

public class ATMCellImpl implements ATMCell {

    private List<Bill> billList;

    private BillNominal nominal;

    public ATMCellImpl() {
        billList = new ArrayList<>();
    }

    @Override
    public List<Bill> getBills(int quantity) {
        return null;
    }

    @Override
    public void putBills(List<Bill> billList) {
        billList.addAll(billList);
    }

    @Override
    public int getQuantity() {
        return billList.size();
    }
}
