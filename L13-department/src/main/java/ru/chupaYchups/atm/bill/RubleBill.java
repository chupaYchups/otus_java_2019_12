package ru.chupaYchups.atm.bill;

import ru.chupaYchups.atm.exception.AtmException;

public class RubleBill implements Bill, Cloneable {

    private BillNominal billNominal;

    public RubleBill(BillNominal billNominal) {
        this.billNominal = billNominal;
    }

    @Override
    public BillNominal getNominal() {
        return billNominal;
    }

    @Override
    public RubleBill clone() {
        try {
            return (RubleBill)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new AtmException("Error while cloning bill");
        }
    }
}
