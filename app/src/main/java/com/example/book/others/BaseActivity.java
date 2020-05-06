package com.example.book.others;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by 大清爹 on 2019/4/27.
 */

public class BaseActivity extends AppCompatActivity {

    private long firstTime=0;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {              //退出程序二次代码
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK://点击返回键
                long secondTime = System.currentTimeMillis();//以毫秒为单位
                if(secondTime -firstTime>2000){
                    Toast.makeText(this, "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;                }
                else{
                    ActivityCollector.fnishAll();
                    System.exit(0);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);

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
