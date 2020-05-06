package com.example.book.actor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.javaBean.Actor;
import com.example.book.others.ActivityCollector;
import com.example.book.others.BaseActivity;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private Button button_login;
    private TextView register;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LitePal.getDatabase();
        userName=(EditText)findViewById(R.id.login_user_name);
        userPassword=(EditText)findViewById(R.id.login_user_password);
        button_login=(Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRight().equals("")){                  //检验数据合法性
                    List<Actor> actorListView= DataSupport.select("password")        //查询用户名对应的密码
                            .where("username=?",userName.getText().toString())
                            .find(Actor.class);
                    if(actorListView.size()>0){
                        for(Actor actor:actorListView){
                            if(userPassword.getText().toString().equals(actor.getPassword())) {             //密码正确，跳转回首页
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                MainActivity.username = userName.getText().toString();                         //设置用户名
                                MainActivity.setName(userName.getText().toString());
                                if(MainActivity.username.equals("admin")){
                                    Intent intent=new Intent(LoginActivity.this,AdminActivity.class);//如果管理员则跳到管理员界面
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent=new Intent(LoginActivity.this,UserActivity.class);
                                    finish();                                                    //普通用户则回到首页
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "账号密码有误，请重新登录", Toast.LENGTH_SHORT).show();
                                userName.setText("");                              //清空编辑框
                                userPassword.setText("");
                            }
                         }
                    }else {
                        Toast.makeText(LoginActivity.this, "账号密码有误，请重新登录", Toast.LENGTH_SHORT).show();
                        userName.setText("");                              //清空编辑框
                        userPassword.setText("");
                    }
                }else {
                    Toast.makeText(LoginActivity.this, ""+isRight(), Toast.LENGTH_SHORT).show();
                    userName.setText("");                      //清空编辑框
                    userPassword.setText("");
                }

            }
        });

        register=(TextView)findViewById(R.id.to_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);   //跳转到注册界面
                startActivity(intent);
            }
        });


    }

    private String isRight(){
        String name=userName.getText().toString();
        String psd=userPassword.getText().toString();
        String result="";
        if(name.equals(""))
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

}
