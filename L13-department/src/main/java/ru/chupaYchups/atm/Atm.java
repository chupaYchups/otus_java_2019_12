package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.department.AtmCommandListener;
import ru.chupaYchups.atm.memento.AtmMemento;
import ru.chupaYchups.atm.memento.AtmMementoSaver;

import java.util.List;

public interface Atm extends AtmCommandListener, AtmMementoSaver {
    List<Bill> getSumm(int summ);
    void putSumm(List<Bill> billList);
    int getBalance();
}
