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
        List<Bill> returnList = null;
        int availableQty = billList.size() - quantity;
        if (availableQty > 0) {
            returnList = billList.subList(0, billList.size() - availableQty);
        }
        return Collections.unmodifiableList(returnList != null ? returnList : Collections.emptyList());
    }

    @Override
    public void putBill(Bill bill) {
        billList.add(bill);
    }

    @Override
    public BillNominal getNominal() {
        return nominal;
    }

    @Override
    public int getHowMuchHave(int qty) {
        if (billList.isEmpty()) {
            return 0;
        }
        return billList.size() < qty ?  billList.size() : qty;
    }

    @Override
    public int getBalance() {
        return billList.size() * nominal.getNominal();
    }
}
