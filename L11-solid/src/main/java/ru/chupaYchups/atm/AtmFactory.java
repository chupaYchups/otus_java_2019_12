package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.BillNominal;

import java.util.List;

public class AtmFactory {

    private List<BillNominal> nominalList;

    public AtmFactory(List<BillNominal> billNominalList) {
        this.nominalList = billNominalList;
    }

    public Atm createATM() {
        return new OldModelAtm(nominalList);
    }
}
