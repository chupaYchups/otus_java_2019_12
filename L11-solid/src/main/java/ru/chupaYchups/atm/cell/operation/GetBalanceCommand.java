package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.ATMCell;

import java.util.HashMap;
import java.util.Map;

public class GetBalanceCommand implements AtmCellChainCommand {

    private Map<ATMCell, Integer> resultMap = new HashMap<>();

    @Override
    public Map<ATMCell, Integer> getResult() {
        return resultMap;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void execute(ATMCell cell) {
        resultMap.put(cell, cell.getBalance());
    }
}
