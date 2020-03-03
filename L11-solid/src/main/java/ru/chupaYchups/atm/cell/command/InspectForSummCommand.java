package ru.chupaYchups.atm.cell.command;

import ru.chupaYchups.atm.cell.AtmCell;
import java.util.HashMap;
import java.util.Map;

public class InspectForSummCommand implements AtmCellChainCommand {

    private int summToGet;
    private Map<AtmCell, Integer> resultMap = new HashMap<>();

    public InspectForSummCommand(int summToGet) {
        this.summToGet = summToGet;
    }

    @Override
    public void execute(AtmCell cell) {
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
    public Map<AtmCell, Integer> getResult() {
        return resultMap;
    }
}
