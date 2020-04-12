package ru.chupaYchups.jdbc.orm.model;

import ru.chupaYchups.jdbc.orm.annotation.Id;

public class Account {

    @Id
    private Long no;
    private String type;
    private Long number;

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

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
