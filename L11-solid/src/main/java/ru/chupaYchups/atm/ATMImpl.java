package ru.chupaYchups.atm;

import ru.chupaYchups.bill.Bill;
import ru.chupaYchups.bill.BillNominal;
import ru.chupaYchups.cell.ATMCell;
import ru.chupaYchups.cell.ATMCellImpl;
import java.util.*;

public class ATMImpl implements ATM {

    private NavigableMap<BillNominal, ATMCell> cellByNominalMap;

    ATMImpl(List<BillNominal> nominals) {
        cellByNominalMap = new TreeMap<>(Comparator.comparingInt(billNominal -> billNominal.getNominal()));
        nominals.forEach(billNominal -> cellByNominalMap.put(billNominal, new ATMCellImpl()));
    }

    @Override
    public List<Bill> getSumm(int summ) {
        if (summ % BillNominal.getMinimalNominal() != 0) {
            throw new IllegalArgumentException("Cannot get such summ : " + summ);
        }
        List<Bill> billList = new ArrayList<>();
        for (Map.Entry<BillNominal, ATMCell> entry : cellByNominalMap.descendingMap().entrySet()) {
            ATMCell atmCell = entry.getValue();
            if (!atmCell.isEmpty()) {
                int nominal = entry.getKey().getNominal();
                int qtyToGet = summ / nominal;
                List<Bill> existsBills = atmCell.getBills(qtyToGet);
                summ -= existsBills.size() * nominal;
                billList.addAll(existsBills);
            }
        }

        return billList;
    }

    @Override
    public void putSumm(List<Bill> billList) {
        billList.forEach(bill -> cellByNominalMap.get(bill.getNominal()).putBill(bill));
    }

    @Override
    public void getBalance() {
    }
}
