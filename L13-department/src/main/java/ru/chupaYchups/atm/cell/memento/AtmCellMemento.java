package ru.chupaYchups.atm.cell.memento;

public class AtmCellMemento {

    private CellMementoSaver.State state;

    public AtmCellMemento(CellMementoSaver.State state) {
        this.state = state;
    }

    public CellMementoSaver.State getState() {
        return state;
    }

    public void setState(CellMementoSaver.State state) {
        this.state = state;
    }
}
