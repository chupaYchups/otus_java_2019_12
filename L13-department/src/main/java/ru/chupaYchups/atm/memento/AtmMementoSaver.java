package ru.chupaYchups.atm.memento;

public interface AtmMementoSaver {
    void applyMemento(AtmMemento memento);
    AtmMemento saveMemento();
    void revertToInitalState();
}
