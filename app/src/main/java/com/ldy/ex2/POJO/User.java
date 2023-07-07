package com.ldy.ex2.POJO;

import android.content.Intent;

import java.util.Date;




public class User {
    private Intent uid;
    private String userName;
    private Date regTime;

    public User(Intent uid, String userName, Date regTime) {
        this.uid = uid;
        this.userName = userName;
        this.regTime = regTime;
    }

    public String getUserName() {
        return userName;
    }

    public User(String userName) {
        this.userName = userName;
    }
}
