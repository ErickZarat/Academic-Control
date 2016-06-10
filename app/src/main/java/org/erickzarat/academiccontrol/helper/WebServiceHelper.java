package org.erickzarat.academiccontrol.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by erick on 5/06/16.
 */
public class WebServiceHelper {

    private static WebServiceHelper instance = null;

    public static WebServiceHelper getInstance(Context context) {
        if(instance == null) {
            instance = new WebServiceHelper(context);
        }
        return instance;
    }

    public final String SERVER =  "http://192.168.64.100:3000/api/";

    public final String ROUTE_PROFESOR = SERVER + "profesor";
    public final String ROUTE_ALUMNO = SERVER + "alumno";
    public final String ROUTE_ROL = SERVER + "rol";
    public final String ROUTE_GRADO = SERVER + "grado";
    public final String ROUTE_SECCION = SERVER + "seccion";
    public final String ROUTE_USUARIO = SERVER + "usuario";
    public final String ROUTE_AUTENTICAR = ROUTE_USUARIO + "/autenticar";
    public final String ROUTE_NOTA = SERVER + "nota";
    public final String ROUTE_PLANIFICACION = SERVER + "planificacion";
    public final String ROUTE_ACTIVIDAD = SERVER + "actividad";
    public final String ROUTE_BIMESTRE = SERVER + "bimestre";
    public final String ROUTE_MATERIA = SERVER + "materia";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private WebServiceHelper(Context context) {
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
