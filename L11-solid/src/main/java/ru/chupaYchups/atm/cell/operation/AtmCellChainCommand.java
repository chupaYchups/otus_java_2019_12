package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.ATMCell;
import java.util.Map;

public interface AtmCellChainCommand extends AtmCellCommand{
    Map<ATMCell, Integer> getResult();
    boolean isFinished();
}
