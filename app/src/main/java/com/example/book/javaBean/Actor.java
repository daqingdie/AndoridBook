package com.example.book.javaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by 大清爹 on 2019/4/27.
 */

public class Actor extends DataSupport{
    private int id;
    private String username;
    private String password;
    private String phone="未知";
    private String address="未知";

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
