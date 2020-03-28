package ru.chupaYchups.atm.cell.memento;

import ru.chupaYchups.atm.bill.Bill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface CellMementoSaver {

    void restore(AtmCellMemento memento);

    AtmCellMemento saveMemento();

    void restoreToInitialState();

    class State {

        private List<Bill> billListCopy;

        public State(List<Bill> billList) {
            billListCopy = new ArrayList<>(billList.size());
            Collections.copy(billListCopy, billList);
        }

        public List<Bill> getBillList() {
            return billListCopy;
        }
    }
}
