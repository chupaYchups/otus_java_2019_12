package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATMCellImpl implements ATMCell {

    private List<Bill> billList;

    private BillNominal nominal;

    public ATMCellImpl(BillNominal nominal) {
        billList = new ArrayList<>();
        this.nominal = nominal;
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

//    @Override
//    public int getAvailableBillQty(int qty) {
//        return billList.size();
//    }

    @Override
    public BillNominal getNominal() {
        return nominal;
    }

    @Override
    public int getHowMuchHave(int qty) {
        return billList.size() >= qty ? qty : qty - billList.size();
    }

    @Override
    public int getBalance() {
        return billList.size() * nominal.getNominal();
    }
}
