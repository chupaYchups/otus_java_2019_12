package ru.chupaYchups.atm.bill;

public class BillImpl implements Bill {

    private BillNominal billNominal;

    public BillImpl(BillNominal billNominal) {
        this.billNominal = billNominal;
    }

    @Override
    public BillNominal getNominal() {
        return null;
    }
}
