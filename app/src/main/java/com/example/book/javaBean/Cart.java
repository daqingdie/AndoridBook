package com.example.book.javaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by 大清爹 on 2019/5/2.
 */

public class Cart extends DataSupport{
    private int id;       			//购物车编号
    private String bookId;
    private String bookImg;
    private String bookName;			//图书书名
    private int number;					//购买数量
    private double bookPrice;			//图书单价
    private double sum;					//购物车总价
    private String userName;			//用户名

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getId() {
        return id;
    }
    public String getBookImg() {
        return bookImg;
    }
    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
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
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public double getBookPrice() {
        return bookPrice;
    }
    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }
    public double getSum() {
        return sum;
    }
    public void setSum(double sum) {
        this.sum = sum;
    }
}
