package ru.chupaYchups.atm.cell.command;

import ru.chupaYchups.atm.cell.AtmCell;

public  interface AtmCellCommand {
    void execute(AtmCell cell);
}
