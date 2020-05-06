package com.example.book.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.book.BookDetailActivity;
import com.example.book.javaBean.Orders;

import org.litepal.crud.DataSupport;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Orders orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orders= DataSupport.where("id=?",getIntent().getStringExtra("id")).find(Orders.class).get(0);

        TextView orderName=(TextView)findViewById(R.id.order_name);
        TextView orderPhone=(TextView)findViewById(R.id.order_phone);
        TextView orderAddress=(TextView)findViewById(R.id.order_address);
        ImageView bookImg=(ImageView)findViewById(R.id.book_img);
        TextView bookName=(TextView)findViewById(R.id.book_name);
        TextView bookPrice=(TextView)findViewById(R.id.book_price);
        TextView orderNumber=(TextView)findViewById(R.id.order_number);
        TextView orderSum=(TextView)findViewById(R.id.order_sum);
        TextView orderMeo=(TextView)findViewById(R.id.order_meo);
        TextView orderTime=(TextView)findViewById(R.id.order_time);
        TextView orderStatus=(TextView)findViewById(R.id.order_status);

        orderName.setText(orders.getOrderName());
        orderPhone.setText(orders.getOrderPhone());
        orderAddress.setText(orders.getOrderAddress());
        Bitmap bitmap= BitmapFactory.decodeFile(orders.getBookImg());
        bookImg.setImageBitmap(bitmap);
        bookName.setText(orders.getBookName());
        bookPrice.setText(String.valueOf(orders.getBookPrice()));
        orderNumber.setText(String.valueOf(orders.getOrderNumber()));
        orderSum.setText(String.valueOf(orders.getOrderSum()));
        orderMeo.setText(orders.getOrderMeo());
        orderTime.setText(orders.getOrderTime());
        orderStatus.setText(orders.getOrderStatus());

        Button delete=(Button)findViewById(R.id.button_delete_order);
        delete.setOnClickListener(this);
        Button change=(Button)findViewById(R.id.button_change_status);
        change.setOnClickListener(this);
        Button chang2=(Button)findViewById(R.id.button_change_status2);
        chang2.setOnClickListener(this);
        if(MainActivity.username.equals("admin")){
            if(orders.getOrderStatus().equals("待发货"))
                chang2.setVisibility(View.VISIBLE);
        }else if(orders.getOrderStatus().equals("待发货")){
            delete.setVisibility(View.VISIBLE);
        }else if (orders.getOrderStatus().equals("已发货")){
            change.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog dialog;
        switch (v.getId()){
            case R.id.button_delete_order:
                dialog=new AlertDialog.Builder(this)
                        .setTitle("取消确认")
                        .setMessage("是否确认取消订单！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(BookDetailActivity.changeBookNumber(BookDetailActivity.BOOK_ADD,orders.getBookName(),orders.getOrderNumber())){
                                    DataSupport.deleteAll(Orders.class,"id=?",String.valueOf(orders.getId()));
                                    Toast.makeText(OrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.button_change_status:
                dialog=new AlertDialog.Builder(this)
                        .setTitle("收货确认")
                        .setMessage("是否确认已收货！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(BookDetailActivity.changeBookNumber(BookDetailActivity.BOOK_ADD,orders.getBookName(),orders.getOrderNumber())){
                                    orders.setOrderStatus("已完成");
                                    orders.updateAll("id=?",String.valueOf(orders.getId()));
                                    Toast.makeText(OrderDetailActivity.this, "收货成功", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.button_change_status2:
                dialog=new AlertDialog.Builder(this)
                        .setTitle("发货确认")
                        .setMessage("是否确认已发货！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(BookDetailActivity.changeBookNumber(BookDetailActivity.BOOK_ADD,orders.getBookName(),orders.getOrderNumber())){
                                    orders.setOrderStatus("已发货");
                                    orders.updateAll("id=?",String.valueOf(orders.getId()));
                                    Toast.makeText(OrderDetailActivity.this, "发货成功", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            default:
                break;
        }
    }
}
