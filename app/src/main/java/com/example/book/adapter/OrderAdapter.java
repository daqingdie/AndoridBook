package com.example.book.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book.R;
import com.example.book.javaBean.Orders;
import com.example.book.others.UpdateListener;

import java.util.List;

/**
 * Created by 大清爹 on 2019/5/4.
 */

public class OrderAdapter extends ArrayAdapter<Orders> {

    private int resourceId;

    public OrderAdapter(Context context, int resource, List<Orders> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        Orders orders=getItem(position);
        ImageView bookImg=(ImageView)view.findViewById(R.id.book_img);
        TextView bookName=(TextView)view.findViewById(R.id.book_name);
        TextView bookPrice=(TextView)view.findViewById(R.id.book_price);
        TextView orderNumber=(TextView)view.findViewById(R.id.order_number);
        TextView orderSum=(TextView)view.findViewById(R.id.order_sum);
        TextView orderStatus=(TextView)view.findViewById(R.id.order_status);

        Bitmap bitmap= BitmapFactory.decodeFile(orders.getBookImg());
        bookImg.setImageBitmap(bitmap);
        bookName.setText(orders.getBookName());
        bookPrice.setText(String.valueOf(orders.getBookPrice()));
        orderNumber.setText(String.valueOf(orders.getOrderNumber()));
        orderSum.setText(String.valueOf(orders.getOrderSum()));
        orderStatus.setText(orders.getOrderStatus());
        return view;

    }
}
