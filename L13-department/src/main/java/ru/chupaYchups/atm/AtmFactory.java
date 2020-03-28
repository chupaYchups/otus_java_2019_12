package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.BillFactory;
import ru.chupaYchups.atm.bill.BillNominal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmFactory {

    private List<BillNominal> nominalList;

    public AtmFactory(List<BillNominal> billNominalList) {
        this.nominalList = billNominalList;
    }

    public Atm createATM() {
        return new OldModelAtm(nominalList);
    }

    public Atm createATM(Map<BillNominal, Integer> billNominalMap) {
        Atm atm = new OldModelAtm(nominalList);
        atm.putSumm(billNominalMap.
            entrySet().
            stream().
            flatMap(billNominalIntegerEntry -> BillFactory.createBillList(billNominalIntegerEntry.getValue(), billNominalIntegerEntry.getKey()).stream()).
            collect(Collectors.toList()));
        return atm;
    }
}
