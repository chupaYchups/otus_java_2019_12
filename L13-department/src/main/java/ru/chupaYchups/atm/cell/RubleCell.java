package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.bill.RubleBill;
import ru.chupaYchups.atm.cell.command.AtmCellChainCommand;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

public class RubleCell implements AtmCell {

    private List<Bill> billList;

    private BillNominal nominal;

    private WeakReference<NavigableMap<BillNominal, AtmCell>> cellByNominalMap;

    public RubleCell(BillNominal nominal) {
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
    public void putBills(List<Bill> billList) {
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
        return billList.size() < qty ? billList.size() : qty;
    }

    @Override
    public int getBalance() {
        return billList.size() * nominal.getNominal();
    }

    @Override
    public AtmCellCommandExecutor getNext() {
        Map.Entry<BillNominal, AtmCell> entry = cellByNominalMap.get().higherEntry(getNominal());
        return entry != null ? entry.getValue() : null;
    }

    @Override
    public void processInternal(AtmCellChainCommand command) {
        command.execute(this);
    }

    @Override
    public void setCellByNominalMap(NavigableMap<BillNominal, AtmCell> cellByNominalMap) {
        this.cellByNominalMap = new WeakReference(cellByNominalMap);
    }

    @Override
    public AtmCell doClone() {
        RubleCell cell = new RubleCell(this.nominal);
        cell.putBills(this.billList.stream().map(bill -> ((RubleBill)bill).clone()).collect(Collectors.toList()));
        return cell;
    }
}
