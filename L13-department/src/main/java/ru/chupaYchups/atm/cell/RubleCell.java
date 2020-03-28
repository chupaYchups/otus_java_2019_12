package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.command.AtmCellChainCommand;
import ru.chupaYchups.atm.cell.memento.AtmCellMemento;
import ru.chupaYchups.atm.cell.memento.CellMementoSaver;
import java.lang.ref.WeakReference;
import java.util.*;

public class RubleCell implements AtmCell  {

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

    public void setCellByNominalMap(NavigableMap<BillNominal, AtmCell> cellByNominalMap) {
        this.cellByNominalMap = new WeakReference(cellByNominalMap);
    }

    @Override
    public void restore(AtmCellMemento memento) {
        State state = memento.getState();
        this.billList = state.getBillList();
    }

    @Override
    public AtmCellMemento saveMemento() {
        return new AtmCellMemento(new CellMementoSaver.State(billList));
    }

    @Override
    public void restoreToInitialState() {
        throw new UnsupportedOperationException();
    }
}
