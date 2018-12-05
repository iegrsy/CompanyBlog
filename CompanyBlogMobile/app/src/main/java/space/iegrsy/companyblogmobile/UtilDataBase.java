package space.iegrsy.companyblogmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UtilDataBase {
    private static final String TAG = UtilDataBase.class.getSimpleName();

    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_LIST_ALL_POST = "list_all_post";
    private static final String ACTION_LIST_USER_POST = "list_user_post";
    private static final String ACTION_LIST_ALL_POST_OF_COMMENT = "list_all_post_of_comment";
    private static final String ACTION_ADD_USER = "add_user";
    private static final String ACTION_ADD_POST = "add_post";
    private static final String ACTION_ADD_COMMENT = "add_comment";

    private static Thread execThread;
    private static final String host = "http://10.5.20.41/index.php";

    public static void executeQuary(final Context context, final StringRequest request) {
        if (execThread != null) {
            execThread.interrupt();
            execThread = null;
        }

        execThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);
            }
        });

        execThread.start();
    }

    private static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Response error: " + error.getMessage());
        }
    };

    public interface LoginListener {
        void onLoginRest(boolean islogin, int userid);
    }

    public static class QLogin {
        boolean islogin = false;
        int userid = -1;
    }

    public static QLogin getLoginResponse(String s) {
        QLogin qlogin = new QLogin();
        try {
            JSONObject object = new JSONObject(s);
            qlogin.islogin = object.getBoolean("islogin");
            qlogin.userid = object.getInt("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qlogin;
    }

    public static void login(@NonNull final Context context, final LoginListener listener,
                             final String username, final String email, final String password) {

        String resUrl = String.format("%s?action=%s", host, ACTION_LOGIN);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, resUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        QLogin qlogin = getLoginResponse(response);
                        Log.i(TAG, String.format("Login response: %s", response));
                        if (listener != null)
                            listener.onLoginRest(qlogin.islogin, qlogin.userid);
                    }
                }, errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> par = new HashMap<>();
                par.put("username", username);
                par.put("email", email);
                par.put("password", getMd5(password));

                return par;
            }
        };

        // Add the request to the RequestQueue.
        executeQuary(context, stringRequest);
    }

    public static String getMd5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
