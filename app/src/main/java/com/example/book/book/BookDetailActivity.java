package com.example.book.book;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.actor.LoginActivity;
import com.example.book.adapter.BookDetailAdapter;
import com.example.book.cart.ShopCartActivity;
import com.example.book.javaBean.Actor;
import com.example.book.javaBean.Book;
import com.example.book.javaBean.Cart;
import com.example.book.javaBean.Orders;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private static final String TAG = "BookDetailActivity";
    private BookDetailAdapter adapter;
    private List<Book> books;
    private Book book;
    private List<Cart> carts;
    public static final int BOOK_ADD=0;
    public static final int BOOK_SUB=1;
    private View dialogView;
    private EditText orderName;
    private EditText orderPhone;
    private EditText orderAddress;
    private EditText orderMeo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String id=getIntent().getStringExtra("id");

        /*Log.d(TAG, "onCreate: "+id);
        Log.d(TAG, "onCreate: "+getIntent().getStringExtra("book"));
        Intent intent=getIntent();
        Log.d(TAG, "onCreate: "+intent.getStringExtra("id"));*/

            //Log.d(TAG, "onCreate: "+book.getBookName());

        books=DataSupport.where("id=?",id).find(Book.class);
        book=books.get(0);
        //Log.d(TAG, "onCreate: "+books.get(0).getBookName());
        ListView listView=(ListView)findViewById(R.id.book_detail_list_view);
        adapter=new BookDetailAdapter(this,R.layout.book_detail_item,books);
        listView.setAdapter(adapter);

        Button add_cart=(Button)findViewById(R.id.button_add_shop_cart);
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.username.equals("")){
                    Log.d(TAG, "isLogin2: ");
                    Intent intent=new Intent(BookDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(BookDetailActivity.this, "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    if(changeBookNumber(BOOK_SUB,book.getBookName(),1)){
                        if(isEmpty()){
                            Cart cart=carts.get(0);
                            cart.setNumber(cart.getNumber()+1);
                            cart.setSum(cart.getNumber()*cart.getBookPrice());
                            cart.updateAll("bookName=?",book.getBookName());
                            //Log.d(TAG, "onClick1: "+book.getBookName());
                        }else {
                            addCart();
                            //Log.d(TAG, "onClick2: "+book.getBookName());
                        }
                        Snackbar.make(v,"添加成功",Snackbar.LENGTH_LONG)
                                .setAction("查看", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(BookDetailActivity.this, ShopCartActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                        List<Book> newBooks=DataSupport.where("id=?",getIntent().getStringExtra("id")).find(Book.class);
                        books.clear();
                        books.addAll(newBooks);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(BookDetailActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        Button add_order=(Button)findViewById(R.id.button_add_order);
        add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.username.equals("")){
                    Log.d(TAG, "isLogin2: ");
                    Intent intent=new Intent(BookDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(BookDetailActivity.this, "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    dialogView= LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.add_order_dialog,null);
                    orderName=(EditText)dialogView.findViewById(R.id.order_name);
                    orderPhone=(EditText)dialogView.findViewById(R.id.order_phone);
                    orderAddress=(EditText)dialogView.findViewById(R.id.order_address);
                    orderMeo=(EditText)dialogView.findViewById(R.id.order_meo);

                    Actor actor=DataSupport.where("userName=?",MainActivity.username).find(Actor.class).get(0);
                    orderName.setText(actor.getUsername());
                    orderPhone.setText(actor.getPhone());
                    orderAddress.setText(actor.getAddress());
                    AlertDialog dialog=new AlertDialog.Builder(BookDetailActivity.this)
                            .setView(dialogView)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(changeBookNumber(BOOK_SUB,book.getBookName(),1)) {
                                        Orders order=new Orders();
                                        order.setUserName(MainActivity.username);
                                        order.setOrderName(orderName.getText().toString());
                                        order.setOrderPhone(orderPhone.getText().toString());
                                        order.setOrderAddress(orderAddress.getText().toString());
                                        order.setBookImg(book.getBookImg());
                                        order.setBookName(book.getBookName());
                                        order.setBookPrice(book.getPrice());
                                        order.setOrderNumber(1);
                                        order.setOrderSum(order.getOrderNumber()*order.getBookPrice());
                                        order.setOrderMeo(orderMeo.getText().toString());
                                        order.setOrderStatus("待发货");
                                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date=new Date();
                                        order.setOrderTime(sdf.format(date));
                                        order.save();
                                        Log.d(TAG, "onClick: "+sdf.format(date));
                                        Toast.makeText(BookDetailActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                                        List<Book> newBooks=DataSupport.where("id=?",getIntent().getStringExtra("id")).find(Book.class);
                                        books.clear();
                                        books.addAll(newBooks);
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(BookDetailActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }

            }
        });
    }


    private void addCart(){
        Cart cart=new Cart();
        cart.setBookId(String.valueOf(book.getId()));
        cart.setBookName(book.getBookName());
        cart.setNumber(1);
        cart.setBookPrice(book.getPrice());
        cart.setSum(cart.getNumber()*cart.getBookPrice());
        cart.setUserName(MainActivity.username);
        cart.setBookImg(book.getBookImg());
        cart.save();
    }
    private boolean isEmpty(){
        boolean result=false;
        carts=DataSupport.where("bookName=? and userName=?",book.getBookName(),MainActivity.username).find(Cart.class);
        if(carts.size()>0)
            result=true;
        return result;
    }

    public static boolean changeBookNumber(int MODE,String bookName,int number){
        Book book=DataSupport.where("bookName=?",bookName).find(Book.class).get(0);
        switch (MODE){
            case BOOK_ADD:
                book.setNumber(book.getNumber()+number);
                break;
            case BOOK_SUB:
                book.setNumber(book.getNumber()-number);
               // Log.d(TAG, "changeBookNumber: "+book.getNumber());
                break;
            default:
                break;
        }
        if(book.getNumber()>0){
            book.updateAll("bookName=?",bookName);
            return true;
        }else {
            return false;
        }
    }

    public void isLogin(){
        if(MainActivity.username.equals("")){
            Log.d(TAG, "isLogin: ");
            Intent intent=new Intent(BookDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}
