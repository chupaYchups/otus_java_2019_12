package ru.chupaYchups.atm.cell.operation;

import ru.chupaYchups.atm.cell.ATMCell;

public  interface AtmCellCommand {
    void execute(ATMCell cell);
}
