package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.command.AtmCellChainCommand;
import ru.chupaYchups.atm.cell.memento.AtmCellMemento;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NavigableMap;

/**
 * Прокси сохранения истории состояния ячейки
 */
public class AtmCellSaveStateProxy implements AtmCell {

    private AtmCell cell;

    private Deque<AtmCellMemento> history;

    public AtmCellSaveStateProxy(AtmCell cell) {
        this.cell = cell;
        history = new ArrayDeque();
        saveHistory();
    }

    @Override
    public List<Bill> getBills(int quantity) {
        List<Bill> billList = cell.getBills(quantity);
        saveHistory();
        return billList;
    }

    @Override
    public void putBills(List<Bill> billList) {
        cell.putBills(billList);
        saveHistory();
    }

    private void saveHistory() {
        AtmCellMemento memento = cell.saveMemento();
        history.push(memento);
    }

    @Override
    public int getHowMuchHave(int qty) {
        return cell.getHowMuchHave(qty);
    }

    @Override
    public BillNominal getNominal() {
        return cell.getNominal();
    }

    @Override
    public int getBalance() {
        return cell.getBalance();
    }

    @Override
    public AtmCellCommandExecutor getNext() {
        return cell.getNext();
    }

    @Override
    public void processInternal(AtmCellChainCommand command) {
        cell.processInternal(command);
    }

    @Override
    public void restore(AtmCellMemento memento) {
        cell.restore(memento);
    }

    @Override
    public AtmCellMemento saveMemento() {
        return cell.saveMemento();
    }

    @Override
    public void restoreToInitialState() {
        cell.restore(history.peek());
        history.clear();
    }

    @Override
    public AtmCell doClone() {
        return null;
    }

    @Override
    public void setCellByNominalMap(NavigableMap<BillNominal, AtmCell> cellByNominalMap) {

    }
}
