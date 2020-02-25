package ru.chupaYchups.atm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тест проверяющий что банкомат")
class ATMImplTest {

    @Test
    @DisplayName("Корректно отрабатывает запрос денежной суммы, возвращает минимальный количеством купюр")
    void getSummSucess() {
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