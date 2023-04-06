//
//  Tools_PreferabliApp.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.app.Application;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

class Tools_PreferabliApp extends Application {

    private static Tools_PreferabliApp mAppContext;
    private static MixpanelAPI mixpanel;

    @Override
    public void onCreate() {
        super.onCreate();
        setAppContext(this);
        registerEventBus();
        Tools_PreferabliTools.handleCursorSize();
        Tools_PreferabliTools.clearValues();
        Tools_DBHelper.initializeInstance(new Tools_DBHelper.SQLiteOpenHelper());
        Tools_PreferabliTools.handleUpgrade();
        Tools_PreferabliTools.addSDKProperties();
    }

    public static Tools_PreferabliApp getAppContext() {
        return mAppContext;
    }

    private void setAppContext(Tools_PreferabliApp mAppContext) {
        Tools_PreferabliApp.mAppContext = mAppContext;
    }

    private void registerEventBus() {
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException e) {
            // ignore the exception.
        }
    }

    public static MixpanelAPI getMixpanel() {
        return mixpanel;
    }

    public static void setupMixpanel(String client_interface, long integration_id) {
        try {
            JSONObject superprops = new JSONObject();
            superprops.put("CLIENT_INTERFACE", client_interface);
            superprops.put("INTEGRATION_ID", integration_id);

            mixpanel = MixpanelAPI.getInstance(getAppContext(), "ff8f35c4aa7d67838380626736c19066", superprops);
        } catch (JSONException e) {
            // do nothing.
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void postAnalytics(JSONObject props) {
        try {
            String type = props.getString("analyticsType");
            props.remove("analyticsType");
            if (type.equalsIgnoreCase("PreferabliDataSDKAnalytics")) {
                String event = props.getString("event");
                props.remove("event");
                getMixpanel().track(event, props);
            } else if (type.equalsIgnoreCase("PreferabliDataSDKAnalyticsSuper")) {
                getMixpanel().registerSuperPropertiesOnce(props);
            } else if (type.equalsIgnoreCase("PreferabliDataSDKAnalyticsPeople")) {
                getMixpanel().getPeople().set(props);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }
}