package com.example.book.actor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.book.R;
import com.example.book.adapter.AdminUserAdapter;
import com.example.book.javaBean.Actor;
import com.example.book.others.UpdateListener;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AdminUserActivity extends AppCompatActivity {

    private AdminUserAdapter adapter;
    private EditText userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        final List<Actor> actors= DataSupport.findAll(Actor.class);
        ListView user_list_view=(ListView)findViewById(R.id.user_list_view);
        adapter=new AdminUserAdapter(this, R.layout.user_list_item,actors, new UpdateListener() {
            @Override
            public void update() {
                List<Actor> newActors= DataSupport.findAll(Actor.class);
                actors.clear();
                actors.addAll(newActors);
                adapter.notifyDataSetChanged();
            }
        });
        user_list_view.setAdapter(adapter);

        userId=(EditText)findViewById(R.id.query_user);
        Button query=(Button)findViewById(R.id.button_query_user);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Actor> actors1=DataSupport.where("id=?",userId.getText().toString())
                        .find(Actor.class);
                actors.clear();
                actors.addAll(actors1);
                adapter.notifyDataSetChanged();
            }
        });

    }

}
