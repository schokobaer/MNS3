package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.app.Activity;
import android.content.Context;

public class PreferencesService {

    public final static String SECURE_MODE = "secureModeOn";

    public void setSecureMode(Activity activity, boolean secure) {
        activity.getPreferences(Context.MODE_PRIVATE).edit().putBoolean(SECURE_MODE, secure).commit();
    }

    public boolean isSecureMode(Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE).getBoolean(SECURE_MODE, false);
    }


}
