package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.ATMCell;
import java.util.HashMap;
import java.util.Map;

public class InspectCellsForMoneyCommand implements AtmCellGroupCommand {

    private int summToGet;
    private Map<ATMCell, Integer> resultMap = new HashMap<>();

    public InspectCellsForMoneyCommand(int summToGet) {
        this.summToGet = summToGet;
    }

    @Override
    public void execute(ATMCell cell) {
        int cellNominal = cell.getNominal().getNominal();
        int qtyToRequest = summToGet / cellNominal;
        int howMuchHave = cell.getHowMuchHave(qtyToRequest);
        resultMap.put(cell, howMuchHave);
        summToGet -= cellNominal * howMuchHave;
    }

    @Override
    public boolean isFinished() {
        return summToGet == 0;
    }

    @Override
    public Map<ATMCell, ?> getResult() {
        return resultMap;
    }
}
