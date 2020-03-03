package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.AtmCell;
import java.util.Map;

public interface AtmCellChainCommand extends AtmCellCommand{
    Map<AtmCell, Integer> getResult();
    boolean isFinished();
}
