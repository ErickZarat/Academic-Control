package org.erickzarat.academiccontrol.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by erick on 26/05/16.
 */
public class Aplication extends Application {
    public static final String TAG = Aplication.class.getSimpleName();
    private RequestQueue requestQueue;
    private static Aplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized Aplication getInstance(Context context) {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> reg, String tag) {
        reg.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(reg);
    }

    public <T> void addToRequestQueue(Request<T> reg) {
        reg.setTag(TAG);
        getRequestQueue().add(reg);
    }

    public <T> void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
