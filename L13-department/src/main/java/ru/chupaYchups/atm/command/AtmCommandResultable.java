package ru.chupaYchups.atm.command;

public interface AtmCommandResultable<T> extends AtmCommand {
    T getResult();
}
