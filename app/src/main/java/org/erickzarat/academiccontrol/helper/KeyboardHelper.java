package org.erickzarat.academiccontrol.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by erick on 4/06/16.
 */
public class KeyboardHelper {

    private static KeyboardHelper instance = null;
    protected KeyboardHelper() {
        // Exists only to defeat instantiation.
    }
    public static KeyboardHelper getInstance() {
        if(instance == null) {
            instance = new KeyboardHelper();
        }
        return instance;
    }

    public void hideKeyboard(Activity activity){
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        try{
            inputManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (NullPointerException npe){
            Log.e( activity.getLocalClassName(), Log.getStackTraceString(npe));
        }
    }
}
