package ru.chupaYchups.atm.department;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.chupaYchups.atm.Atm;
import ru.chupaYchups.atm.AtmFactory;
import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillFactory;
import ru.chupaYchups.atm.bill.BillNominal;
import java.util.List;
import java.util.Map;

class ClassicDepartmentTest {

    public static final long FIRST_ATM_ID = 1L;
    public static final long SECOND_ATM_ID = 2L;
    public static final long THIRD_ATM_ID = 3L;
    private Department department;
    private final Integer TEST_INITIAL_BALANCE = 5000;
    private Map<Long, Map<BillNominal, Integer>> initialStateMap;

    @BeforeEach
    void setUp() {
        department = new ClassicDepartment();
        AtmFactory atmFactory = new AtmFactory(List.of(BillNominal.values()));
        //Раскладываем в разных купюрах TEST_INITIAL_BALANCE
        initialStateMap = Map.of(1L, Map.of(BillNominal.NOMINAL_1000, 1, BillNominal.NOMINAL_500, 2),
                2L, Map.of(BillNominal.NOMINAL_500, 2, BillNominal.NOMINAL_100, 10),
                3L, Map.of(BillNominal.NOMINAL_50, 20));
        initialStateMap.forEach((atmId, billNominalQtyMap) -> department.addAtm(atmId, atmFactory.createATM(billNominalQtyMap)));
    }

    @Test
    void getTotalBalance() {
        Assertions.assertThat(department.getTotalBalance()).isEqualTo(TEST_INITIAL_BALANCE);

        final List<Bill> billList = BillFactory.createBillList(3, BillNominal.NOMINAL_50);
        department.getAtm(1L).putSumm(billList);

        Assertions.assertThat(department.getTotalBalance()).
            isEqualTo(billList.
                stream().
                map(bill -> bill.getNominal().getNominal()).
                reduce(TEST_INITIAL_BALANCE, Integer::sum));
    }

    @Test
    void resetToInitialState() {
        Atm firstAtm = department.getAtm(FIRST_ATM_ID);
        Atm secondAtm = department.getAtm(SECOND_ATM_ID);
        Atm thirdAtm = department.getAtm(THIRD_ATM_ID);
        //Эмуляция работы банкомата
        final List<Bill> putBillList = BillFactory.createBillList(3, BillNominal.NOMINAL_1000);
        firstAtm.putSumm(putBillList);
        secondAtm.putSumm(putBillList);
        thirdAtm.putSumm(putBillList);
        firstAtm.getSumm(BillNominal.NOMINAL_500.getNominal());
        thirdAtm.getSumm(BillNominal.NOMINAL_100.getNominal());

        department.resetToInitialState();

        Assertions.assertThat(department.getTotalBalance()).isEqualTo(TEST_INITIAL_BALANCE);
        for (Long id : List.of(FIRST_ATM_ID, SECOND_ATM_ID, THIRD_ATM_ID)) {
            Assertions.assertThat(department.getAtm(id).
                    getBalance()).
                    isEqualTo(initialStateMap.get(id).entrySet().stream().
                            map(billNominalIntegerEntry -> billNominalIntegerEntry.getKey().getNominal() * billNominalIntegerEntry.getValue()).
                            reduce(0, Integer::sum));
        }
    }
}