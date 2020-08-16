package ru.chupaYchups.atm.department;

import ru.chupaYchups.atm.Atm;

public interface Department {
    int getTotalBalance();
    void resetToInitialState();
    void addAtm(Long atmId, Atm atm);
    Atm getAtm(Long atmId);
    void removeAtm(Atm atm);
}
