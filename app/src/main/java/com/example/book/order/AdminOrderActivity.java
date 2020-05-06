package com.example.book.order;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.adapter.OrderAdapter;
import com.example.book.javaBean.Orders;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AdminOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private OrderAdapter adapter;
    private List<Orders> ordersList;
    private TextView all;
    private TextView unSend;
    private TextView send;
    private TextView gone;
    private EditText userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        ListView listView=(ListView)findViewById(R.id.order_list_view);
        ordersList= DataSupport.findAll(Orders.class);
        adapter=new OrderAdapter(this,R.layout.order_item,ordersList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AdminOrderActivity.this,OrderDetailActivity.class);
                intent.putExtra("id",String.valueOf(ordersList.get(position).getId()));
                startActivityForResult(intent,1);
            }
        });

        all=(TextView)findViewById(R.id.order_all);
        all.setOnClickListener(this);
        all.setTextColor(Color.RED);
        unSend=(TextView)findViewById(R.id.order_un_send);
        unSend.setOnClickListener(this);
        send=(TextView)findViewById(R.id.order_send);
        send.setOnClickListener(this);
        gone=(TextView)findViewById(R.id.order_gone);
        gone.setOnClickListener(this);

        userName=(EditText)findViewById(R.id.query_user_name);
        Button query=(Button)findViewById(R.id.button_query_order);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(DataSupport.where("userName=?",userName.getText().toString()).find(Orders.class));
                all.setTextColor(Color.RED);
                unSend.setTextColor(Color.BLACK);
                send.setTextColor(Color.BLACK);
                gone.setTextColor(Color.BLACK);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_all:
                update(DataSupport.findAll(Orders.class));
                all.setTextColor(Color.RED);
                unSend.setTextColor(Color.BLACK);
                send.setTextColor(Color.BLACK);
                gone.setTextColor(Color.BLACK);
                break;
            case R.id.order_un_send:
                unSend.setTextColor(Color.RED);
                all.setTextColor(Color.BLACK);
                send.setTextColor(Color.BLACK);
                gone.setTextColor(Color.BLACK);
                update(DataSupport.where("orderStatus=?","待发货").find(Orders.class));
                break;
            case R.id.order_send:
                update(DataSupport.where("orderStatus=?","已发货").find(Orders.class));
                send.setTextColor(Color.RED);
                unSend.setTextColor(Color.BLACK);
                all.setTextColor(Color.BLACK);
                gone.setTextColor(Color.BLACK);
                break;
            case R.id.order_gone:
                update(DataSupport.where("orderStatus=?","已完成").find(Orders.class));
                gone.setTextColor(Color.RED);
                unSend.setTextColor(Color.BLACK);
                send.setTextColor(Color.BLACK);
                all.setTextColor(Color.BLACK);
                break;
            default:
                break;
        }
    }

    private void update(List<Orders> orderses){
        ordersList.clear();
        ordersList.addAll(orderses);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    List<Orders> newOrderses= DataSupport.findAll(Orders.class);
                    ordersList.clear();
                    ordersList.addAll(newOrderses);
                    all.setTextColor(Color.RED);
                    unSend.setTextColor(Color.BLACK);
                    send.setTextColor(Color.BLACK);
                    gone.setTextColor(Color.BLACK);
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
