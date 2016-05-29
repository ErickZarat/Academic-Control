package org.erickzarat.academiccontrol.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by erick on 25/05/16.
 */
public class WebService {

    public static String server = "http://academiccontrol-64005.onmodulus.net/api/";
    //"https://sleepy-caverns-10999.herokuapp.com/api/";
    public static String usuarios = server + "usuario";
    public static String autenticar = server + "usuario/autenticar";
    private static WebService mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private WebService(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {

                    }
                });

    }

    public static synchronized WebService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WebService(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> reg) {
        getRequestQueue().add(reg);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
