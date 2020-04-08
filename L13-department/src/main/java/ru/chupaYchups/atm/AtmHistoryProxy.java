package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.command.AtmCommand;
import ru.chupaYchups.atm.memento.AtmMemento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class AtmHistoryProxy implements Atm {

    private Deque<AtmMemento> history;
    private Atm atm;

    public AtmHistoryProxy(Atm atm) {
        this.history = new ArrayDeque<>();
        this.atm = atm;
    }

    @Override
    public List<Bill> getSumm(int summ) {
        List<Bill> billList = atm.getSumm(summ);
        doSnapshot();
        return billList;
    }

    @Override
    public void putSumm(List<Bill> billList) {
        atm.putSumm(billList);
        doSnapshot();
    }

    @Override
    public int getBalance() {
        return atm.getBalance();
    }

    @Override
    public void onCommand(AtmCommand command) {
        command.execute(this);
    }

    @Override
    public void applyMemento(AtmMemento memento) {
        atm.applyMemento(memento);
    }

    @Override
    public AtmMemento saveMemento() {
        return atm.saveMemento();
    }

    @Override
    public void revertToInitalState() {
        applyMemento(history.getLast());
        history.clear();
    }

    private void doSnapshot() {
        history.push(saveMemento());
    }
}
