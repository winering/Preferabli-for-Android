package com.ringit.sdk.demo;

import tools.WRCustomization;
import tools.WineRing;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WineRing.main().initialize(this,"YOUR_TOKEN_HERE");
        WRCustomization.fontBoldName = "fonts/BobaPandaFont.otf";
        WRCustomization.fontItalicName = "fonts/BobaPandaFont.otf";
        WRCustomization.fontRegName = "fonts/BobaPandaFont.otf";
    }
}
