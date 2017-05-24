package com.ins.middle.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/10.
 */

public class User implements Serializable{

    private int id;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
