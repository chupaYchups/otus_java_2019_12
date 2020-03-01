package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.ATMCell;
import ru.chupaYchups.atm.cell.ATMCellImpl;
import ru.chupaYchups.atm.cell.operation.AtmCellGroupCommand;
import ru.chupaYchups.atm.cell.operation.InspectCellsForMoneyCommand;

import java.util.*;

public class ATMImpl implements ATM {

    private NavigableMap<BillNominal, ATMCell> cellByNominalMap;

    private List<BillNominal> nominals;

    ATMImpl(final List<BillNominal> nominals) {
        if (nominals.isEmpty()) {
            throw new IllegalArgumentException("list of nominals cannot be empty");
        }
        this.nominals = nominals;
        cellByNominalMap = new TreeMap<>(Comparator.<BillNominal>comparingInt(b -> b.getNominal()).reversed());
        nominals.forEach(billNominal -> {cellByNominalMap.put(billNominal, new ATMCellImpl(billNominal));});
    }

    @Override
    public List<Bill> getSumm(final int summ) {
        if (summ > getBalance() && (summ % cellByNominalMap.lastKey().getNominal() != 0)) {
            throw new IllegalArgumentException("Cannot get such summ : " + summ);
        }
        List<Bill> billList = new ArrayList<>();
        AtmCellGroupCommand inspectCellsForMoneyCommand = new InspectCellsForMoneyCommand(summ);
        Iterator<Map.Entry<BillNominal, ATMCell>> iterator = cellByNominalMap.entrySet().iterator();
        while (!inspectCellsForMoneyCommand.isFinished() && iterator.hasNext()) {
            inspectCellsForMoneyCommand.execute(iterator.next().getValue());
        }
        if (!inspectCellsForMoneyCommand.isFinished()) {
            throw new IllegalArgumentException("Summ cannot be returned : " + summ);
        }
        Map<ATMCell, Integer> result = (Map<ATMCell, Integer>) inspectCellsForMoneyCommand.getResult();
        result.forEach((atmCell, i) -> {
            billList.addAll(atmCell.getBills(i));
        });
        return billList;
    }

    @Override
    public void putSumm(List<Bill> billList) {
        billList.forEach(bill -> cellByNominalMap.get(bill.getNominal()).putBill(bill));
    }

    @Override
    public int getBalance() {
        return cellByNominalMap.
            entrySet().
            stream().
            mapToInt(entry -> entry.getValue().getBalance()).
            sum();
    }
}
