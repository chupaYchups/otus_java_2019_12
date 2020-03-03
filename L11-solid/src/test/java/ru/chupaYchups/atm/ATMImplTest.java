package ru.chupaYchups.atm;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.chupaYchups.atm.bill.Bill;
import ru.chupaYchups.atm.bill.BillFactory;
import ru.chupaYchups.atm.bill.BillNominal;
import ru.chupaYchups.atm.exception.AtmException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест проверяющий что банкомат")
class ATMImplTest {

    private static AtmFactory atmFactory;
    private Atm atm;

    private final int TEST_SUMM_VALUE = 2150;

    @BeforeAll
    static void beforeAll() {
        atmFactory = new AtmFactory(List.of(BillNominal.values()));
    }

    @BeforeEach
    void beforeEach() {
        atm = atmFactory.createATM();
    }

    @Test
    @DisplayName("Корректно отрабатывает запрос денежной суммы, возвращает минимальный количеством купюр")
    void getSummSucess() {
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_1000));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_500));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_100));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_50));

        List<Bill> billList = atm.getSumm(2150);

        Assertions.assertThat(billList).
            hasSize(4).
            extracting("nominal").
            containsExactly(BillNominal.NOMINAL_1000, BillNominal.NOMINAL_1000,
                BillNominal.NOMINAL_100,
                BillNominal.NOMINAL_50);
    }

    @Test
    @DisplayName("Возвращает ошибку, с сообщением о нехватке денег, если суммы на счету недостаточно")
    void getSummHaveNoMoney() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> atm.getSumm(2150)).
            isInstanceOf(AtmException.class).
            hasMessageContaining("Cannot get such summ : " + TEST_SUMM_VALUE);
    }

    @Test
    @DisplayName("Возвращает ошибку, с сообщением о том, что запрошена сумма, которая не может быть выдана, ввиду отсутствия подобных номиналов(сумма не кратна минимальному номиналу)")
    void getSummNotMultipleToMinimalNominal() {
        atm.putSumm(BillFactory.createBillList(2, BillNominal.NOMINAL_1000));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_100));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_50));
        assertThrows(AtmException.class, () -> atm.getSumm(TEST_SUMM_VALUE + 4));
    }

    @Test
    @DisplayName("Возвращает ошибку, с сообщением о том, что запрошена сумма, которая не может быть выдана, ввиду отсутствия подобных номиналов(нет таких номиналов)")
    void getSummHaveNoNeededNominal() {
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_1000));
        atm.putSumm(BillFactory.createBillList(4, BillNominal.NOMINAL_100));
        assertThrows(AtmException.class, () -> atm.getSumm(TEST_SUMM_VALUE));
    }

    @Test
    @DisplayName("Корректно возвращает значение общего баланса банкомата. Остаток денег. ")
    void getBalance() {
        atm.putSumm(BillFactory.createBillList(1, BillNominal.NOMINAL_1000));
        atm.putSumm(BillFactory.createBillList(2, BillNominal.NOMINAL_500));
        atm.putSumm(BillFactory.createBillList(1, BillNominal.NOMINAL_100));
        atm.putSumm(BillFactory.createBillList(1, BillNominal.NOMINAL_50));
        assertEquals(TEST_SUMM_VALUE, atm.getBalance());
    }
}