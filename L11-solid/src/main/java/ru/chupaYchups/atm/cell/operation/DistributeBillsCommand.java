package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.ATMCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DistributeBillsCommand implements AtmCellChainCommand{

    private List<Bill> billList;
    private Map<ATMCell, Integer> resultMap = new HashMap<>();

    public DistributeBillsCommand(List<Bill> billList) {
        this.billList = billList;
    }

    @Override
    public Map<ATMCell, Integer> getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFinished() {
        return billList.isEmpty();
    }

    @Override
    public void execute(ATMCell cell) {
        BillNominal cellNominal = cell.getNominal();
        List<Bill> billsToPut = billList.stream().filter(bill -> bill.getNominal() == cellNominal).collect(Collectors.toList());
        billList.removeAll(billsToPut);
        cell.putBillList(billsToPut);
        resultMap.put(cell, billsToPut.size());
    }
}
