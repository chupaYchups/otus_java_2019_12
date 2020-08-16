package ru.chupaYchups.atm.command;

import ru.chupaYchups.atm.Atm;

public class ResetToInitialStateCommand implements AtmCommand {
    @Override
    public void execute(Atm atm) {
        atm.revertToInitalState();
    }
}
