package ru.chupaYchups.atm.bill;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BillFactory {

    public static Bill createBill(BillNominal nominal) {
       return new RubleBill(nominal);
    }

    public static List<Bill> createBillList(int qty, BillNominal nominal) {
        return IntStream.
                range(0, qty).
                mapToObj(operand -> createBill(nominal)).
                collect(Collectors.toList());
    }
}
