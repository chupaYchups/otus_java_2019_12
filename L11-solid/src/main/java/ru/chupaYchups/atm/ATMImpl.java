package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.ATMCell;
import ru.chupaYchups.atm.cell.ATMCellImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {

    private Map<BillNominal, ATMCell> cellByNominalMap;

    public ATMImpl(List<BillNominal> nominals) {
        cellByNominalMap = new HashMap<>();
        nominals.forEach(billNominal -> cellByNominalMap.put(billNominal, new ATMCellImpl()));
    }

    @Override
    public void getSumm(int summ) {

    }

    @Override
    public void putSumm(List<Bill> billList) {

    }

    @Override
    public void getBalance() {

    }
}
