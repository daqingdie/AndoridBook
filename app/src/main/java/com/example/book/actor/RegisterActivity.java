package com.example.book.actor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.book.R;
import com.example.book.javaBean.Actor;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText password2;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        username=(EditText)findViewById(R.id.register_user_name);
        password=(EditText)findViewById(R.id.register_user_password);
        password2=(EditText)findViewById(R.id.register_user_password2);

        Button button=(Button)findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(queryActor()) {                   //检查是否已存在同名用户
                    Toast.makeText(RegisterActivity.this, "该用户名已被注册，请重新输入", Toast.LENGTH_SHORT).show();
                    clear();
                }
                else
                    insertActor();
            }
        });

        
    }
    private boolean queryActor(){
        List<Actor> actorListView= DataSupport.select("password")        //查询用户名
                .where("username=?",username.getText().toString())
                .find(Actor.class);
        if(actorListView.size()!=0)
            return true;
        return false;
    }

    private void  insertActor(){
        if(isRight().equals("")){
            Actor actor=new Actor();
            actor.setUsername(username.getText().toString());
            actor.setPassword(password.getText().toString());
            actor.save();                                           //插入一条新数据
            Log.d(TAG, "onCreate: 注册成功");
            Toast.makeText(RegisterActivity.this, "注册成功,请登录", Toast.LENGTH_SHORT).show();
            finish();                //回到登录界面
        }else {
            Toast.makeText(RegisterActivity.this, ""+isRight(), Toast.LENGTH_SHORT).show();
            clear();
        }
    }
    
    private String isRight(){
        String psd=password.getText().toString();
        String psd2=password2.getText().toString();
        String name=username.getText().toString();
        String result="";
        if(!psd.equals(psd2)){
            result="两次密码输入不一致，请重新输入";
        }else if(name.equals(""))
            result="用户名不能为空";
        else if(name.length()>10)
            result="用户名过长，请重新输入";
        else if(psd.equals(""))
            result="密码不能为空";
        else if(psd.length()>10)
            result="密码不能大于10位";
        else if(psd.length()<3)
            result="密码不能小于3位";
        else if(psd.matches(".*[^a-zA-Z0-9]+.*"))
            result="密码不能带有特殊符号（如空格）";
        return result;
    }
    private void clear(){
        username.setText("");
        password.setText("");
        password2.setText("");
    }
}
