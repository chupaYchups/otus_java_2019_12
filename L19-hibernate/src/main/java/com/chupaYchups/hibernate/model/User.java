package com.chupaYchups.hibernate.model;

import java.util.List;

public class User {

    private AddressDataSet address;
    private List<PhoneDataSet> phoneNumber;

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public List<PhoneDataSet> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<PhoneDataSet> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
