package ru.chupaYchups.bill;

public class RubleBill implements Bill {

    private BillNominal billNominal;

    public RubleBill(BillNominal billNominal) {
        this.billNominal = billNominal;
    }

    @Override
    public BillNominal getNominal() {
        return billNominal;
    }
}
