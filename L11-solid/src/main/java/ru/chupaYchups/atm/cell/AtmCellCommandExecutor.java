package ru.chupaYchups.atm.cell;

import ru.chupaYchups.atm.cell.operation.AtmCellChainCommand;

interface AtmCellCommandExecutor {

    AtmCellCommandExecutor getNext();

    void processInternal(AtmCellChainCommand command);

    default void process(AtmCellChainCommand command) {
        processInternal(command);
        if (!command.isFinished() && getNext() != null) {
            getNext().process(command);
        }
    }
}
