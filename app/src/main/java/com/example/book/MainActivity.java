package com.example.book;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.actor.LoginActivity;
import com.example.book.actor.UserActivity;
import com.example.book.adapter.BookAdapter;
import com.example.book.book.BookDetailActivity;
import com.example.book.cart.ShopCartActivity;
import com.example.book.javaBean.Book;
import com.example.book.order.OrderActivity;
import com.example.book.others.BaseActivity;
import com.example.book.others.SettingActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    public static String username="";    //显示的用户名
    private DrawerLayout mDrawerLayout;
    private static View headView;
    private BookAdapter adapter;
    private List<Book> books;
    private EditText queryBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);  //初始化布局
        final NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);  //初始化菜单
        headView=LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header,null); //初始化菜单头文件
        CircleImageView head=(CircleImageView)headView.findViewById(R.id.icon_i);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("")){                                                //检查是否登录
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);  //跳转到登录界面
                    mDrawerLayout.closeDrawers();
                    startActivity(intent);
                }else {
                    AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                            .setTitle("注销确认")
                            .setMessage("您已登录，是否确认注销")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    username="";
                                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }

            }
        });
        navigationView.addHeaderView(headView);

        //初始化导航按钮
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(username.equals("")){            //检查是否登录
                    Toast.makeText(MainActivity.this, "您未登录，请先登录", Toast.LENGTH_SHORT).show();  //未登录跳转到登录界面
                    intent=new Intent(MainActivity.this,LoginActivity.class);
                    mDrawerLayout.closeDrawers();
                    startActivity(intent);
                }else {                             //跳转到各界面
                    switch (item.getItemId()){
                        case R.id.nav_user:
                            intent=new Intent(MainActivity.this,UserActivity.class);
                            mDrawerLayout.closeDrawers();
                            startActivity(intent);
                            break;
                        case R.id.nav_shop_cart:
                            intent=new Intent(MainActivity.this,ShopCartActivity.class);
                            mDrawerLayout.closeDrawers();
                            startActivity(intent);
                            break;
                        case R.id.nav_order:
                            intent=new Intent(MainActivity.this,OrderActivity.class);
                            mDrawerLayout.closeDrawers();
                            startActivity(intent);
                            break;
                        case R.id.nav_settings:
                            intent=new Intent(MainActivity.this,SettingActivity.class);
                            mDrawerLayout.closeDrawers();
                            startActivity(intent);
                            break;
                        case R.id.nav_about:
                            AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("关于的标语")
                                    .setMessage("内容")
                                    .setPositiveButton("确定",null)
                                    .show();
                            mDrawerLayout.closeDrawers();
                            break;
                        default:
                            break;
                    }
                }

                return true;
            }
        });

        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("")){            //检查是否登录
                    Toast.makeText(MainActivity.this, "您未登录，请先登录", Toast.LENGTH_SHORT).show();  //未登录跳转到登录界面
                    Intent  intent=new Intent(MainActivity.this,LoginActivity.class);
                    mDrawerLayout.closeDrawers();
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, ShopCartActivity.class);   //购物车悬浮按钮
                    startActivity(intent);
                }
            }
        });

        books = DataSupport.findAll(Book.class);
        final ListView listView=(ListView)findViewById(R.id.book_list_view);
        adapter=new BookAdapter(this,R.layout.book_list_item, books);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,BookDetailActivity.class);
                intent.putExtra("id",String.valueOf(books.get(position).getId()));
                //Log.d("BookDetailActivity", "onItemClick: "+books.get(position).getId());
                startActivity(intent);
            }
        });

        queryBook=(EditText)findViewById(R.id.query_book_name);
        Button query=(Button)findViewById(R.id.button_query_book);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> newBooks = DataSupport.where("bookName like ?", "%"+queryBook.getText().toString()+"%")
                        .find(Book.class);
                books.clear();
                books.addAll(newBooks);
                adapter.notifyDataSetChanged();

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);             //点击导航按钮打开菜单
        }
        return true;
    }


    public static void setName(String username){
        TextView name=(TextView)headView.findViewById(R.id.nav_name);
        name.setText(username);
    }


}
