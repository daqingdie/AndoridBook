package com.example.book.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.book.BookDetailActivity;
import com.example.book.javaBean.Actor;
import com.example.book.javaBean.Book;
import com.example.book.javaBean.Cart;
import com.example.book.javaBean.Orders;
import com.example.book.others.UpdateListener;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 大清爹 on 2019/5/3.
 */

public class CartAdapter extends ArrayAdapter<Cart> {

    private  int resourceId;
    private UpdateListener updateListener;
    private String cartId;
    private View dialogView;
    private EditText orderName;
    private EditText orderPhone;
    private EditText orderAddress;
    private EditText orderMeo;
    private Book book;
    public CartAdapter(Context context, int resource, List<Cart> objects,UpdateListener listener) {
        super(context, resource, objects);
        resourceId=resource;
        updateListener=listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        final Cart cart=getItem(position);
        ImageView bookImg=(ImageView)view.findViewById(R.id.book_img);
        TextView bookName=(TextView)view.findViewById(R.id.book_name);
        TextView bookPrice=(TextView)view.findViewById(R.id.book_price);
        TextView cartNumber=(TextView)view.findViewById(R.id.cart_number);
        TextView cartSum=(TextView)view.findViewById(R.id.cart_sum);
        Button sub=(Button)view.findViewById(R.id.button_sub_book_number);
        Button add=(Button)view.findViewById(R.id.button_add_book_numebr);
        Button addOrder=(Button)view.findViewById(R.id.button_add_order);
        Button delete=(Button)view.findViewById(R.id.delete_cart);
        sub.setTag(cart.getId());
        add.setTag(cart.getId());
        addOrder.setTag(cart.getBookName());
        delete.setTag(cart.getId());

        Bitmap bitmap= BitmapFactory.decodeFile(cart.getBookImg());
        bookImg.setImageBitmap(bitmap);
        bookName.setText(cart.getBookName());
        DecimalFormat sum = new DecimalFormat("#.00");
        bookPrice.setText(sum.format(cart.getBookPrice()));
        cartNumber.setText(String.valueOf(cart.getNumber()));
        cartSum.setText(sum.format(cart.getSum()));
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartId=String.valueOf(v.getTag());
                List<Cart> carts= DataSupport.where("id=?",cartId).find(Cart.class);
                Cart cart1=carts.get(0);
                if(cart1.getNumber()>1){
                    if(BookDetailActivity.changeBookNumber(BookDetailActivity.BOOK_ADD,cart1.getBookName(),1)){
                        cart1.setNumber(cart1.getNumber()-1);
                        cart1.setSum(cart1.getNumber()*cart1.getBookPrice());
                        cart1.updateAll("id=?",cartId);
                        updateListener.update();
                    }
                }else {
                    Toast.makeText(getContext(), "数量已达最低", Toast.LENGTH_SHORT).show();
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartId=String.valueOf(v.getTag());
                //Log.d("BookDetailActivity", "onClick: "+cartId);
                final Cart cart1=DataSupport.where("id=?",cartId).find(Cart.class).get(0);
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setTitle("删除确认")
                        .setMessage("是否确认从购物车中删除该商品")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(BookDetailActivity.changeBookNumber(BookDetailActivity.BOOK_ADD,cart1.getBookName(),cart1.getNumber()))
                                    DataSupport.deleteAll(Cart.class,"id=?",cartId);
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                updateListener.update();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartId=String.valueOf(v.getTag());
                List<Cart> carts= DataSupport.where("id=?",cartId).find(Cart.class);
                Cart cart1=carts.get(0);
                //Log.d("BookDetailActivity", "onClick: "+cart1.getBookName());
                if(BookDetailActivity.changeBookNumber(BookDetailActivity.BOOK_SUB,cart1.getBookName(),1)){
                        cart1.setNumber(cart1.getNumber()+1);
                        cart1.setSum(cart1.getNumber()*cart1.getBookPrice());
                        cart1.updateAll("id=?",cartId);
                        updateListener.update();
                } else {
                    Toast.makeText(getContext(), "库存不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book=DataSupport.where("bookName=?",(String)v.getTag()).find(Book.class).get(0);
                dialogView= LayoutInflater.from(getContext()).inflate(R.layout.add_order_dialog,null);
                orderName=(EditText)dialogView.findViewById(R.id.order_name);
                orderPhone=(EditText)dialogView.findViewById(R.id.order_phone);
                orderAddress=(EditText)dialogView.findViewById(R.id.order_address);
                orderMeo=(EditText)dialogView.findViewById(R.id.order_meo);

                Actor actor=DataSupport.where("userName=?", MainActivity.username).find(Actor.class).get(0);
                orderName.setText(actor.getUsername());
                orderPhone.setText(actor.getPhone());
                orderAddress.setText(actor.getAddress());
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                DataSupport.deleteAll(Cart.class,"bookName=? and userName=?",book.getBookName(),MainActivity.username);
                                order.save();
                                Toast.makeText(getContext(), "下单成功", Toast.LENGTH_SHORT).show();
                                updateListener.update();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        return view;
    }

}
