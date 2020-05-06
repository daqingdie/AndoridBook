package com.example.book.javaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by 大清爹 on 2019/5/4.
 */

public class Orders extends DataSupport{
    private int id;
    private String userName;
    private String orderName;
    private String orderPhone;
    private String orderAddress;
    private String bookImg;
    private String bookName;
    private Double bookPrice;
    private int  orderNumber;
    private Double orderSum;
    private String orderTime;
    private String orderStatus;
    private String orderMeo;


    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderMeo() {
        return orderMeo;
    }

    public void setOrderMeo(String orderMeo) {
        this.orderMeo = orderMeo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Double orderSum) {
        this.orderSum = orderSum;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
