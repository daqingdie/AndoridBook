package com.example.book.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.javaBean.Actor;
import com.example.book.R;
import com.example.book.others.UpdateListener;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 大清爹 on 2019/4/28.
 */

public class AdminUserAdapter extends ArrayAdapter<Actor> {
    private UpdateListener listener;
    private static final String TAG = "AdminUserAdapter";
    private  View dialogView;   //修改对话框
    private  String  userId;   //用户id
    private int resourceId;
    private EditText userPhone;
    private EditText userAddress;
    public AdminUserAdapter(Context context, int resource, List<Actor> objects,UpdateListener listener) {
        super(context, resource, objects);
        this.listener=listener;
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Actor actor=getItem(position);
        View view;
        ViewHandle viewHandle;
        view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        viewHandle=new ViewHandle();
        //初始化控件
        viewHandle.userId=(TextView)view.findViewById(R.id.user_id);
        viewHandle.userName=(TextView)view.findViewById(R.id.user_name);
        viewHandle.userPhone=(TextView)view.findViewById(R.id.user_phone);
        viewHandle.userAddress=(TextView)view.findViewById(R.id.user_address);
        viewHandle.update_user=(Button)view.findViewById(R.id.button_update_user);
        viewHandle.update_user.setTag(actor.getId());  //存放按钮对应的用户id
       // Log.d(TAG, "getView: "+actor.getUsername());
        viewHandle.delete_user=(Button)view.findViewById(R.id.button_delete_user);
        viewHandle.delete_user.setTag(actor.getId());   //存放按钮对应的用户id

        viewHandle.userId.setText(String.valueOf(actor.getId()));
        viewHandle.userName.setText(actor.getUsername());
        viewHandle.userPhone.setText(actor.getPhone());
        viewHandle.userAddress.setText(actor.getAddress());

        //修改的监听器
        viewHandle.update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId= String.valueOf(v.getTag());   //提取Id
               // Log.d(TAG, "onClick: "+userId);
                dialogView=LayoutInflater.from(getContext()).inflate(R.layout.update_user_dialog,null);
                userPhone=(EditText)dialogView.findViewById(R.id.update_user_phone);
                userAddress=(EditText)dialogView.findViewById(R.id.update_user_address);
                List<Actor> actors=DataSupport.where("id=?",userId)
                        .find(Actor.class);
                for(Actor actor1:actors){
                  //  Log.d(TAG, "onClick: "+actor1.getId());
                    userPhone.setText(actor1.getPhone());
                    userAddress.setText(actor1.getAddress());
                }
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isRight().equals("")){
                                    Actor actor=new Actor();
                                    actor.setPhone(userPhone.getText().toString());
                                    actor.setAddress(userAddress.getText().toString());
                                    actor.updateAll("id=?",userId);                 //更新数据
                                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    listener.update();
                                }else {
                                    Toast.makeText(getContext(), ""+isRight(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });
        viewHandle.delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId= String.valueOf(v.getTag());
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setTitle("删除用户")
                        .setMessage("是否确认删除")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSupport.deleteAll(Actor.class,"id=?",userId);
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                listener.update();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        return view;
    }

    private class ViewHandle {
        TextView userId;
        TextView userName;
        TextView userPhone;
        TextView userAddress;
        Button update_user;
        Button delete_user;
    }
    private String isRight(){
        String phone=userPhone.getText().toString();
        String address=userAddress.getText().toString();
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
}
