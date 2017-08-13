package com.sankholin.customlistview;

public class User {
    String userName;
    String addDate;

    public User(String userName, String addDate) {
        this.userName = userName;
        this.addDate = addDate;
    }

    public String getAddDate() {
        return addDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
