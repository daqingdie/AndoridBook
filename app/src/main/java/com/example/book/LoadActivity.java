package com.example.book;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.book.others.BaseActivity;

public class LoadActivity extends BaseActivity {


    private Handler handler=new Handler();
    private Runnable runnable;
    private int time=5;
    private  Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        button=(Button)findViewById(R.id.button);
        button.setText("测试");
        View v = findViewById(R.id.button);//找到你要设透明背景的layout 的id
        v.getBackground().setAlpha(10);//0~255透明度值
        runnable=new Runnable() {
            @Override
            public void run() {
                if(time>0){
                    button.setText(""+time+"秒后跳过");
                    handler.postDelayed(this,1000);
                    time--;
                }else {
                    Intent intent=new Intent(LoadActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        handler.postDelayed(runnable,100);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);


                intent.setClass(LoadActivity.this,MainActivity.class);

                handler.removeCallbacks(runnable);
                startActivity(intent);
            }
        });
    }
}
