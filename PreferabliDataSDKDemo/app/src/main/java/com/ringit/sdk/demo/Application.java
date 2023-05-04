package com.ringit.sdk.demo;

import classes.Preferabli;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Update with your own keys.
        long YOUR_INTEGRATION_ID = 12345;
        Preferabli.initialize(this, "YOUR_CLIENT_INTERFACE_HERE", YOUR_INTEGRATION_ID,true);
    }
}
