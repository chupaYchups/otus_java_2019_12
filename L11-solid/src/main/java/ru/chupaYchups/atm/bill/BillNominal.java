package ru.chupaYchups.atm.bill;

public enum BillNominal {

    NOMINAL_50 (50),
    NOMINAL_100 (100),
    NOMINAL_500(500),
    NOMINAL_1000(1000);

    BillNominal(int nominal) {
        this.nominal = nominal;
    }

    private int nominal;

    public int getNominal() {
        return nominal;
    }
}
