package ru.chupaYchups.atm.cell.command;

import ru.chupaYchups.atm.cell.AtmCell;

public class ResetToInitialStateCommand implements AtmCellCommand {
    @Override
    public void execute(AtmCell cell) {
        cell.restoreToInitialState();
    }
}
