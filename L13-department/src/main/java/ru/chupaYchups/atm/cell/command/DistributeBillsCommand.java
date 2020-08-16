package ru.chupaYchups.atm.cell.command;

import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.AtmCell;
import ru.chupaYchups.atm.bill.Bill;

import java.util.*;
import java.util.stream.Collectors;

public class DistributeBillsCommand implements AtmCellChainCommand {

    private List<Bill> billList;
    private Map<AtmCell, Integer> resultMap = new HashMap<>();

    public DistributeBillsCommand(List<Bill> billList) {
        this.billList = new ArrayList<>(billList);
    }

    @Override
    public Map<AtmCell, Integer> getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFinished() {
        return billList.isEmpty();
    }

    @Override
    public void execute(AtmCell cell) {
        BillNominal cellNominal = cell.getNominal();
        List<Bill> billsToPut = billList.stream().filter(bill -> bill.getNominal() == cellNominal).collect(Collectors.toList());
        billList.removeAll(billsToPut);
        cell.putBills(billsToPut);
        resultMap.put(cell, billsToPut.size());
    }
}
