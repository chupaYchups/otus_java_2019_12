package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.ATMCell;
import java.util.Map;

public interface AtmCellGroupCommand extends AtmCellCommand{
    boolean isFinished();
    Map<ATMCell, ?> getResult();
}
