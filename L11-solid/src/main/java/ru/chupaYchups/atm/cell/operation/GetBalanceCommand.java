package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.AtmCell;

import java.util.HashMap;
import java.util.Map;

public class GetBalanceCommand implements AtmCellChainCommand {

    private Map<AtmCell, Integer> resultMap = new HashMap<>();

    @Override
    public Map<AtmCell, Integer> getResult() {
        return resultMap;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void execute(AtmCell cell) {
        resultMap.put(cell, cell.getBalance());
    }
}
