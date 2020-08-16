package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;

import java.util.List;
import java.util.NavigableMap;

public interface AtmCell extends AtmCellCommandExecutor {

    List<Bill> getBills(int quantity);

    void putBills(List<Bill> billList);

    int getHowMuchHave(int qty);

    BillNominal getNominal();

    int getBalance();

    void setCellByNominalMap(NavigableMap<BillNominal, AtmCell> cellByNominalMap);

    AtmCell doClone();
}
