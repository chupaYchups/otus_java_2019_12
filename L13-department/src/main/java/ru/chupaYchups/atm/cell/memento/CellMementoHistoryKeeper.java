package ru.chupaYchups.atm.cell.memento;

public interface CellMementoHistoryKeeper {
    void saveToHistory(AtmCellMemento memento);
}
