package com.example.book.actor;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.javaBean.Actor;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {



    private static final String TAG = "UserActivity";
    private Actor actor;                    //用户信息
    private TextView userId;                //用户id
    private TextView userName;              //用户名
    private TextView userPhone;             //用户手机号
    private TextView userAddress;           //用户地址
    private EditText updatePhone;           //修改手机号
    private EditText updateAddress;         //修改地址
    private EditText password;              //原密码
    private EditText newPassword;          //新密码
    private EditText newPassword2;          //新密码2
    private View view;            //用来获取对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //初始化控件
        userId=(TextView)findViewById(R.id.user_id);
        userName=(TextView)findViewById(R.id.user_name);
        userPhone=(TextView)findViewById(R.id.user_phone);
        userAddress=(TextView)findViewById(R.id.user_address);

        queryActor();           //查询用户信息并显示
        //设置监听器
        Button updateUser=(Button)findViewById(R.id.button_update_user);
        updateUser.setOnClickListener(this);
        Button updatePassword=(Button)findViewById(R.id.button_update_password);
        updatePassword.setOnClickListener(this);
    }

    private void queryActor(){
        List<Actor> actorListView= DataSupport.where("username=?", MainActivity.username)  //查询
                .find(Actor.class);
        for(Actor mactor:actorListView){                   //封装
            actor=mactor;
            userId.setText(String.valueOf(actor.getId()));
            userName.setText(actor.getUsername());
            userPhone.setText(actor.getPhone());
            userAddress.setText(actor.getAddress());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_update_user:           //修改用户信息
                view= LayoutInflater.from(this).inflate(R.layout.update_user_dialog,null);
                updatePhone=(EditText)view.findViewById(R.id.update_user_phone);
                updateAddress=(EditText)view.findViewById(R.id.update_user_address);
                updatePhone.setText(actor.getPhone());
                //Log.d(TAG, "onClick: "+actor.getPhone()+actor.getAddress());
                updateAddress.setText(actor.getAddress());
                AlertDialog dialog=new AlertDialog.Builder(this)                   //显示对话框
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isRight().equals("")){                //检验数据合法性
                                    Actor actor=new Actor();
                                    actor.setPhone(updatePhone.getText().toString());
                                    actor.setAddress(updateAddress.getText().toString());
                                    actor.updateAll("username=?",MainActivity.username);       //更新用户数据
                                    queryActor();                                                //刷新界面
                                    Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(UserActivity.this, ""+ isRight(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.button_update_password:
                view=LayoutInflater.from(this).inflate(R.layout.update_user_password,null);
                password=(EditText)view.findViewById(R.id.update_user_password);
                newPassword=(EditText)view.findViewById(R.id.update_user_new_password);
                newPassword2=(EditText)view.findViewById(R.id.update_user_new_password2);
                AlertDialog aDialog=new AlertDialog.Builder(this)
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isPsdRight().equals("")){
                                    Actor actor=new Actor();
                                    actor.setPassword(newPassword.getText().toString());
                                    actor.updateAll("username=?",MainActivity.username);       //更新用户密码
                                    Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(UserActivity.this, ""+ isPsdRight(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            default:
                break;
        }
    }

    private String isRight(){
        String phone=updatePhone.getText().toString();
        String address=updateAddress.getText().toString();
        String result="";
        if(phone.equals(""))
            result="手机号码不能为空";
        else if((phone.length())!=11)
            result="手机号码只能为11位，请重新输入！";
        else if(!phone.matches("[0-9]{1,}"))
            result="手机号码必须为数字，请重新输入";
        else if(address.equals(""))
            result="地址不能为空";
        else if(address.length()>38)
            result="地址过长，请重新输入";
        return result;
    }
    private String isPsdRight(){
        String psd=password.getText().toString();
        String npsd=newPassword.getText().toString();
        String npsd2=newPassword2.getText().toString();
        String result="";
        if(!psd.equals(actor.getPassword()))
            result="原密码输入错误，请重新输入";
        if(!npsd.equals(npsd2))
            result="两次密码输入不一致，请重新输入";
        else if(npsd.equals(""))
            result="新密码不能为空";
        else if(npsd.length()>10)
            result="密码不能大于10位";
        else if(npsd.length()<3)
            result="密码不能小于3位";
        else if(npsd.matches(".*[^a-zA-Z0-9]+.*"))
            result="密码不能带有特殊符号（如空格）";
        return result;
    }
}
