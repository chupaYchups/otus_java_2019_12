package ru.chupaYchups.dto;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

public class UserDataList  extends ResultDataType {

    private List<UserData> userDataList;

    public UserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    public List<UserData> getUserDataList() {
        return userDataList;
    }
    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }
}
