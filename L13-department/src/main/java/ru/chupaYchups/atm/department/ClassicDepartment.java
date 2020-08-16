package ru.chupaYchups.atm.department;

import ru.chupaYchups.atm.Atm;
import ru.chupaYchups.atm.command.AtmCommand;
import ru.chupaYchups.atm.command.GetTotalBalanceCommand;
import ru.chupaYchups.atm.command.ResetToInitialStateCommand;
import java.util.HashMap;
import java.util.Map;

public class ClassicDepartment implements Department {

    private Map<Long, Atm> atmMap;

    public ClassicDepartment() {
        this.atmMap = new HashMap<>();
    }

    @Override
    public int getTotalBalance() {
        GetTotalBalanceCommand command = new GetTotalBalanceCommand();
        executeCommandOnAllListeners(command);
        return command.getResult();
    }

    @Override
    public void resetToInitialState() {
        ResetToInitialStateCommand command = new ResetToInitialStateCommand();
        executeCommandOnAllListeners(command);
    }

    private void executeCommandOnAllListeners(AtmCommand command) {
        for (AtmCommandListener listener : atmMap.values()) {
            listener.onCommand(command);
        }
    }

    @Override
    public void addAtm(Long atmId, Atm atm) {
        atmMap.put(atmId, atm);
    }

    @Override
    public void removeAtm(Atm atm) {
        atmMap.remove(atm);
    }

    @Override
    public Atm getAtm(Long atmId) {
        return atmMap.get(atmId);
    }
}
