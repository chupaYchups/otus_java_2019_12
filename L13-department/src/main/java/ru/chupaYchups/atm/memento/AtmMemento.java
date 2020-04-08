package ru.chupaYchups.atm.memento;

import ru.chupaYchups.atm.OldModelAtm;

public class AtmMemento {

    private OldModelAtm.State state;

    public AtmMemento(OldModelAtm.State state) {
        this.state = state;
    }

    public OldModelAtm.State getState() {
        return state;
    }
}
