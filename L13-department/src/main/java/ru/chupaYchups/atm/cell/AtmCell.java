package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.memento.CellMementoSaver;

import java.util.List;

public interface AtmCell extends AtmCellCommandExecutor, CellMementoSaver {

    List<Bill> getBills(int quantity);

    void putBills(List<Bill> billList);

    int getHowMuchHave(int qty);

    BillNominal getNominal();

    int getBalance();
}
