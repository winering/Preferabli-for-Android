package com.ringit.sdk.demo;

import classes.Preferabli;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Preferabli.initialize(this, "YOUR_CLIENT_INTERFACE_HERE", 6301,true);
    }
}
