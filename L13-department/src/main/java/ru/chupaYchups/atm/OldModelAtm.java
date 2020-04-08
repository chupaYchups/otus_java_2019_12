package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.AtmCell;
import ru.chupaYchups.atm.cell.RubleCell;
import ru.chupaYchups.atm.cell.command.*;
import ru.chupaYchups.atm.command.AtmCommand;
import ru.chupaYchups.atm.exception.AtmException;
import ru.chupaYchups.atm.memento.AtmMemento;

import java.util.*;
import java.util.stream.Collectors;

public class OldModelAtm implements Atm {

    private NavigableMap<BillNominal, AtmCell> cellByNominalMap;

    private Deque<AtmMemento> history;

    OldModelAtm(final List<BillNominal> nominals) {
        if (nominals.isEmpty()) {
            throw new AtmException("List of nominals cannot be empty");
        }
        cellByNominalMap = new TreeMap<>(Comparator.<BillNominal>comparingInt(b -> b.getNominal()).reversed());
        nominals.forEach(billNominal -> {
            RubleCell cell = new RubleCell(billNominal);
            cell.setCellByNominalMap(cellByNominalMap);
            cellByNominalMap.put(billNominal, cell);
        });
        history = new ArrayDeque();
    }

    @Override
    public List<Bill> getSumm(final int summ) {
        if (summ > getBalance() || (summ % cellByNominalMap.lastKey().getNominal() != 0)) {
            throw new AtmException("Cannot get such summ : " + summ);
        }

        AtmCellChainCommand inspectCellsForMoneyCommand = new InspectForSummCommand(summ);
        //запускаем чейн
        cellByNominalMap.firstEntry().getValue().process(inspectCellsForMoneyCommand);

        if (!inspectCellsForMoneyCommand.isFinished()) {
            throw new AtmException("Summ cannot be returned : " + summ);
        }

        Map<AtmCell, Integer> result = inspectCellsForMoneyCommand.getResult();
        List<Bill> billList = result.
                entrySet().
                stream().
                map(entry -> entry.getKey().getBills(entry.getValue())).
                flatMap(bills -> bills.stream()).collect(Collectors.toList());

        saveMemento();
        return billList;
    }

    @Override
    public void putSumm(List<Bill> billList) {
        AtmCellChainCommand command = new DistributeBillsCommand(billList);
        cellByNominalMap.firstEntry().getValue().process(command);
        if (!command.isFinished()) {
            throw new AtmException("Cannot put bills in ATM, invalid nominals");
        }
        saveMemento();
    }

    @Override
    public int getBalance() {
        AtmCellChainCommand command = new GetBalanceCommand();
        cellByNominalMap.firstEntry().getValue().process(command);
        return command.getResult().entrySet().stream().collect(Collectors.summingInt(Map.Entry::getValue));
    }

    @Override
    public void onCommand(AtmCommand command) {
        command.execute(this);
    }

    @Override
    public void applyMemento(AtmMemento memento) {
        this.cellByNominalMap = new TreeMap<>(memento.getState().cellByNominalMap);
    }

    @Override
    public AtmMemento saveMemento() {
        return new AtmMemento(new State(cellByNominalMap));
    }

    @Override
    public void revertToInitalState() {
        throw new UnsupportedOperationException();
    }

    /**
     * Cостояние банкомата
     */
    public class State {

        private NavigableMap<BillNominal, AtmCell> cellByNominalMap;

        private State(NavigableMap<BillNominal, AtmCell> cellMap) {
            cellByNominalMap = new TreeMap<>();
            cellMap.forEach((billNominal, atmCell) -> {
                AtmCell cell = atmCell.doClone();
                cellByNominalMap.put(billNominal, cell);
                cell.setCellByNominalMap(cellByNominalMap);
            });
        }
    }
}
