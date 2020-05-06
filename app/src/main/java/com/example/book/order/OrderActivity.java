package com.example.book.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.adapter.OrderAdapter;
import com.example.book.javaBean.Orders;

import org.litepal.crud.DataSupport;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private OrderAdapter adapter;
    List<Orders> orderses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderses= DataSupport.where("userName=?", MainActivity.username).order("id desc").find(Orders.class);
        ListView listView=(ListView)findViewById(R.id.order_list_view);
        adapter=new OrderAdapter(this,R.layout.order_item,orderses);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(OrderActivity.this,OrderDetailActivity.class);
                intent.putExtra("id",String.valueOf(orderses.get(position).getId()));
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    List<Orders> newOrderses= DataSupport.where("userName=?", MainActivity.username).order("id desc").find(Orders.class);
                    orderses.clear();
                    orderses.addAll(newOrderses);
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
