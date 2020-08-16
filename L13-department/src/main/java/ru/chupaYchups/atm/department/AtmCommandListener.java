package ru.chupaYchups.atm.department;

import ru.chupaYchups.atm.command.AtmCommand;

public interface AtmCommandListener {
    void onCommand(AtmCommand command);
}
