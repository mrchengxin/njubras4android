package cn.nju.cs.seg.brasclient.util;

import android.app.Application;

/**
 * 获取
 */
public class MyApplication extends Application {
	private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
