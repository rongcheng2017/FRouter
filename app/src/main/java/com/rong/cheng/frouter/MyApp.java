package com.rong.cheng.frouter;

import android.app.Application;

import com.rong.cheng.router_runtime.Router;

/**
 * @author: frc
 * @description:
 * @date: 4/21/21 7:01 PM
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.INSTANCE.init();
    }
}
