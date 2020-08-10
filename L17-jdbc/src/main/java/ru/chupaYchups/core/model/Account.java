package ru.chupaYchups.core.model;

import ru.chupaYchups.core.model.annotation.Id;

import java.util.Objects;

public class Account implements IdentifiedEntity{

    @Id
    private Long no;
    private String type;
    private Long rest;


    @Override
    public Long getId() {
        return no;
    }

    @Override
    public void setId(Long id) {
        no = id;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRest() {
        return rest;
    }

    public void setRest(Long rest) {
        this.rest = rest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(no, account.no) &&
                Objects.equals(type, account.type) &&
                Objects.equals(rest, account.rest);
    }
}
