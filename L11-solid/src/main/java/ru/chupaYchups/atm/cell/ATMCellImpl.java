package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.operation.AtmCellChainCommand;

import java.lang.ref.WeakReference;
import java.util.*;

public class ATMCellImpl implements ATMCell {

    private List<Bill> billList;

    private BillNominal nominal;

    private WeakReference<NavigableMap<BillNominal, ATMCell>> cellByNominalMap;

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
    public void putBillList(List<Bill> billList) {
        this.billList.addAll(billList);
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

    @Override
    public AtmCellCommandExecutor getNext() {
        Map.Entry<BillNominal, ATMCell> entry = cellByNominalMap.get().higherEntry(getNominal());
        return entry != null ? entry.getValue() : null;
    }

    @Override
    public void processInternal(AtmCellChainCommand command) {
        command.execute(this);
    }

    public void setCellByNominalMap(NavigableMap<BillNominal, ATMCell> cellByNominalMap) {
        this.cellByNominalMap = new WeakReference<NavigableMap<BillNominal, ATMCell>>(cellByNominalMap);
    }
}
