package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.ATMCell;
import java.util.HashMap;
import java.util.Map;

public class InspectForSummCommand implements AtmCellChainCommand {

    private int summToGet;
    private Map<ATMCell, Integer> resultMap = new HashMap<>();

    public InspectForSummCommand(int summToGet) {
        this.summToGet = summToGet;
    }

    @Override
    public void execute(ATMCell cell) {
        int cellNominal = cell.getNominal().getNominal();
        int qtyToRequest = summToGet / cellNominal;
        if (qtyToRequest > 0) {
            int howMuchHave = cell.getHowMuchHave(qtyToRequest);
            resultMap.put(cell, howMuchHave);
            summToGet -= cellNominal * howMuchHave;
        }
    }

    @Override
    public boolean isFinished() {
        return summToGet == 0;
    }

    @Override
    public Map<ATMCell, Integer> getResult() {
        return resultMap;
    }
}
