package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.cell.ATMCell;
import ru.chupaYchups.atm.cell.ATMCellImpl;
import ru.chupaYchups.atm.cell.operation.AtmCellChainCommand;
import ru.chupaYchups.atm.cell.operation.DistributeBillsCommand;
import ru.chupaYchups.atm.cell.operation.GetBalanceCommand;
import ru.chupaYchups.atm.cell.operation.InspectForSummCommand;
import ru.chupaYchups.atm.exception.AtmException;
import java.util.*;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {

    private NavigableMap<BillNominal, ATMCell> cellByNominalMap;

    ATMImpl(final List<BillNominal> nominals) {
        if (nominals.isEmpty()) {
            throw new AtmException("List of nominals cannot be empty");
        }
        cellByNominalMap = new TreeMap<>(Comparator.<BillNominal>comparingInt(b -> b.getNominal()).reversed());
        nominals.forEach(billNominal -> {
            ATMCellImpl cell = new ATMCellImpl(billNominal);
            cell.setCellByNominalMap(cellByNominalMap);
            cellByNominalMap.put(billNominal, cell);
        });
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

        Map<ATMCell, Integer> result = (Map<ATMCell, Integer>) inspectCellsForMoneyCommand.getResult();
        List<Bill> billList = result.
            entrySet().
            stream().
            map(entry -> entry.getKey().getBills(entry.getValue())).
            flatMap(bills -> bills.stream()).collect(Collectors.toList());

        return billList;
    }

    @Override
    public void putSumm(List<Bill> billList) {
        AtmCellChainCommand command = new DistributeBillsCommand(billList);
        cellByNominalMap.firstEntry().getValue().process(command);
        if (!command.isFinished()) {
            throw new AtmException("Cannot put bills in ATM, invalid nominals");
        }
    }

    @Override
    public int getBalance() {
        AtmCellChainCommand command = new GetBalanceCommand();
        cellByNominalMap.firstEntry().getValue().process(command);
        return command.getResult().entrySet().stream().collect(Collectors.summingInt(Map.Entry::getValue));
    }
}
