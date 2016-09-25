package getless.sveri.de.getless;

import android.content.SharedPreferences;

/**
 * Created by sveri on 19.09.16.
 */
public class Preferences {

    public static final String PREFS_NAME = "GetlessPrefsFile";
    public static final String GET_LESS_TOKEN = "GetLessToken";

    public static String getGetlessToken(SharedPreferences sharedPreferences){
        return sharedPreferences.getString(GET_LESS_TOKEN, "");
    }

    public static void setGetlessToken(SharedPreferences sharedPreferences, String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GET_LESS_TOKEN, token);
        editor.commit();
    }
}
