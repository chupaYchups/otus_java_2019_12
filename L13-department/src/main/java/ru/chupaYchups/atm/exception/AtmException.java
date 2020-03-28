package ru.chupaYchups.atm.exception;

public class AtmException extends RuntimeException {

    public AtmException() {
    }

    public AtmException(String msg) {
        super(msg);
    }
}
