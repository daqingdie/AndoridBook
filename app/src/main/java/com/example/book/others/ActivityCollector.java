package com.example.book.others;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大清爹 on 2019/4/15.
 */

public class ActivityCollector {
    public  static List<Activity> activities=new ArrayList<>();

    public  static  void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void  removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void fnishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing())
                activity.finish();
        }
        activities.clear();
    }
}
