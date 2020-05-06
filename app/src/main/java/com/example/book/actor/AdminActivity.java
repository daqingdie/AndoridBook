package com.example.book.actor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.book.book.AdminBookActivity;
import com.example.book.order.AdminOrderActivity;
import com.example.book.R;
import com.example.book.others.BaseActivity;

public class AdminActivity extends BaseActivity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button to_user=(Button)findViewById(R.id.button_to_admin_user);
        to_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(AdminActivity.this,AdminUserActivity.class);
                startActivity(intent);
            }
        });

        Button to_Book=(Button)findViewById(R.id.button_to_admin_book);
        to_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(AdminActivity.this,AdminBookActivity.class);
                startActivity(intent);
            }
        });

        Button to_order=(Button)findViewById(R.id.button_to_admin_order);
        to_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(AdminActivity.this,AdminOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}
