package space.iegrsy.companyblogmobile.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class AuthenticationUtil {
    private static final String PREF_IS_AUTH = "is_auth";

    private static final String PREF_USERID = "userid";
    private static final String PREF_USERNAME = "username";

    public static class Authentication {
        public int userid = -1;
        String username = "";

        Authentication(int userid, String username) {
            this.userid = userid;
            this.username = username;
        }
    }

    public static Authentication newInstance(@NonNull Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isAuth = sharedPref.getBoolean(PREF_IS_AUTH, false);

        if (!isAuth)
            return null;

        int id = sharedPref.getInt(PREF_USERID, -1);
        String username = sharedPref.getString(PREF_USERNAME, "");

        if (id < 0)
            return null;
        if (username.length() < 3)
            return null;

        return new Authentication(id, username);
    }

    public static void setAuthUser(@NonNull Context context, int id, String username) throws Exception {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (id < 0)
            throw new Exception("id cannot be less than 0");
        if (username.length() < 3)
            throw new Exception("username length cannot be less than 0");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_USERID, id);
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }

    public static void setPrefIsAuth(@NonNull Context context, boolean isLogin) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_IS_AUTH, isLogin);
        editor.apply();
    }
}
