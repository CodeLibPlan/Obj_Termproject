package com.example.myapplication;

/**
 * Created by 전혜민 on 2017-11-21.
 */

public interface BackgroundListener {
    public void start();
    public void callback(CallbackEvent<String> ev);
    public void stopListen();
}
