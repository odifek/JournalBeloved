package com.techbeloved.journalbeloved.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String userId;

    public User(){

    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }
}
