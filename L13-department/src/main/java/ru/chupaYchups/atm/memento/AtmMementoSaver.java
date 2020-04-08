package ru.chupaYchups.atm.memento;

import ru.chupaYchups.atm.bill.Bill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface AtmMementoSaver {

    void saveMemento();

    void resetToInitialState();
}
