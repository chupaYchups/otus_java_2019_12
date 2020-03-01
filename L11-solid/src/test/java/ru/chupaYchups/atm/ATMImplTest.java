package ru.chupaYchups.atm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.chupaYchups.atm.bill.BillFactory;
import ru.chupaYchups.atm.bill.RubleBill;
import ru.chupaYchups.atm.bill.BillNominal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест проверяющий что банкомат")
class ATMImplTest {

    @Test
    @DisplayName("Корректно отрабатывает запрос денежной суммы, возвращает минимальный количеством купюр")
    void getSummSucess() {
        ATMFactory atmFactory = new ATMFactory(List.of(BillNominal.values()));
        ATM atm = atmFactory.createATM();
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_1000));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_500));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_100));
        atm.putSumm(BillFactory.createBillList(3, BillNominal.NOMINAL_50));
        assertEquals(4, atm.getSumm(2150).size());
    }

    @Test
    @DisplayName("Возвращает ошибку, с сообщением о нехватке денег, если суммы на счету недостаточно")
    void getSummHaveNoMoney() {

    }

    @Test
    @DisplayName("Корректно кладёт денежные средства на счёт. Баланс увеличивается.")
    void putSummSucess() {
    }

    @Test
    @DisplayName("Корректно возвращает значение общего баланса банкомата. Остаток денег. ")
    void getBalance() {
    }
}