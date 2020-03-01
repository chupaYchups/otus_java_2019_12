package ru.chupaYchups.atm;

import ru.chupaYchups.atm.bill.BillNominal;

import java.util.List;

public class ATMFactory {

    private List<BillNominal> nominalList;

    public ATMFactory(List<BillNominal> billNominalList) {
        this.nominalList = billNominalList;
    }

    public ATM createATM() {
        return new ATMImpl(nominalList);
    }
}
