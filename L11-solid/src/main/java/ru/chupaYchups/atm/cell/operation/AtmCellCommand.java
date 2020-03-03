package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.AtmCell;

public  interface AtmCellCommand {
    void execute(AtmCell cell);
}
