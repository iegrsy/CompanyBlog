package space.iegrsy.companyblogmobile.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import space.iegrsy.companyblogmobile.models.CommentModel;
import space.iegrsy.companyblogmobile.models.PostModel;

public class DataBaseUtil {
    private static final String TAG = DataBaseUtil.class.getSimpleName();

    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_LIST_ALL_POST = "list_all_post";
    private static final String ACTION_LIST_USER_POST = "list_user_post";
    private static final String ACTION_LIST_ALL_POST_OF_COMMENT = "list_all_post_of_comment";
    private static final String ACTION_ADD_USER = "add_user";
    private static final String ACTION_ADD_POST = "add_post";
    private static final String ACTION_ADD_COMMENT = "add_comment";

    private static final String host = "http://10.5.20.41/index.php";

    private static String getRequestUrl(String action) {
        return String.format("%s?action=%s", host, action);
    }

    private static void executeQuary(final Context context, final StringRequest request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);
            }
        }).start();
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

    private static QLogin getLoginResponse(String s) {
        QLogin qlogin = new QLogin();
        try {
            JSONObject object = new JSONObject(s);
            qlogin.islogin = object.getBoolean("islogin");
            qlogin.userid = object.getInt("userid");

            String err = object.getString("err");
            if (!err.isEmpty()) Log.e(TAG, String.format("Login err: %s", err));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qlogin;
    }

    public static void login(@NonNull final Context context, final LoginListener listener,
                             final String username, final String email, final String password) {

        Response.Listener<String> loginResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QLogin qlogin = getLoginResponse(response);
                Log.i(TAG, String.format("Login response: %s", response));
                if (listener != null)
                    listener.onLoginRest(qlogin.islogin, qlogin.userid);
            }
        };

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_LOGIN),
                loginResponse,
                errorListener
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

    public interface GetPostListener {
        void onRequestAllPost(ArrayList<PostModel> postList);
    }

    private static ArrayList<PostModel> getAllPostResponse(String response) {
        ArrayList<PostModel> list = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("posts");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                PostModel model = new PostModel(
                        o.getInt("postid"),
                        o.getString("username"),
                        o.getString("title"),
                        o.getString("body"),
                        o.getString("date")
                );

                list.add(model);
            }

            String err = object.getString("err");
            if (!err.isEmpty()) Log.e(TAG, String.format("Login err: %s", err));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void getAllPost(@NonNull Context context, final GetPostListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<PostModel> list = getAllPostResponse(response);
                Log.i(TAG, "All post response: " + response);
                if (listener != null)
                    listener.onRequestAllPost(list);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_LIST_ALL_POST),
                responseListener,
                errorListener
        );

        executeQuary(context, stringRequest);
    }

    public static void getUserAllPost(@NonNull Context context, final GetPostListener listener, final int userid) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<PostModel> list = getAllPostResponse(response);
                Log.i(TAG, "User post response: " + response);
                if (listener != null)
                    listener.onRequestAllPost(list);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_LIST_USER_POST),
                responseListener,
                errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> par = new HashMap<>();
                par.put("userid", String.valueOf(userid));

                return par;
            }
        };

        executeQuary(context, stringRequest);
    }

    public interface GetCommentsListener {
        void onRequestComments(ArrayList<CommentModel> commentList);
    }

    private static ArrayList<CommentModel> getCommentsResponse(String response) {
        ArrayList<CommentModel> list = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("comments");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                CommentModel model = new CommentModel(
                        o.getInt("commentid"),
                        o.getString("username"),
                        o.getString("comment"),
                        o.getString("date")
                );

                list.add(model);
            }

            String err = object.getString("err");
            if (!err.isEmpty()) Log.e(TAG, String.format("Get comment err: %s", err));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void getComments(@NonNull Context context, final GetCommentsListener listener, final int postid) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<CommentModel> list = getCommentsResponse(response);
                Log.i(TAG, "Comment response: " + response);
                if (listener != null)
                    listener.onRequestComments(list);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_LIST_ALL_POST_OF_COMMENT),
                responseListener,
                errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> par = new HashMap<>();
                par.put("postid", String.valueOf(postid));

                return par;
            }
        };

        executeQuary(context, stringRequest);
    }

    public interface AddUserListener {
        void isCreateUser(boolean b);
    }

    public static class QUser {
        public String username;
        public String email;
        public String password;
        public String date;
    }

    public static void addUser(@NonNull final Context context, final AddUserListener listener, final QUser user) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isAdd = false;
                try {
                    JSONObject object = new JSONObject(response);
                    isAdd = object.getBoolean("isadd");

                    String err = object.getString("err");
                    if (!err.isEmpty()) {
                        Log.e(TAG, String.format("Add user err: %s", err));
                        Toast.makeText(context, String.format("Add user err: %s", err),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Add user response: " + response);
                if (listener != null)
                    listener.isCreateUser(isAdd);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_ADD_USER),
                responseListener,
                errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> par = new HashMap<>();
                par.put("username", user.username);
                par.put("email", user.email);
                par.put("password", getMd5(user.password));
                par.put("birthday", user.date);

                return par;
            }
        };

        executeQuary(context, stringRequest);
    }

    public interface AddPostListener {
        void isAddPost(boolean b);
    }

    public static class QPost {
        public int userid;
        public String title;
        public String body;
        public String date;
    }

    public static void addPost(@NonNull Context context, final AddPostListener listener, final QPost post) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isAdd = false;
                try {
                    JSONObject object = new JSONObject(response);
                    isAdd = object.getBoolean("isadd");

                    String err = object.getString("err");
                    if (!err.isEmpty()) Log.e(TAG, String.format("Add post err: %s", err));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Add post response: " + response);
                if (listener != null)
                    listener.isAddPost(isAdd);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_ADD_POST),
                responseListener,
                errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> par = new HashMap<>();
                par.put("userid", String.valueOf(post.userid));
                par.put("title", post.title);
                par.put("body", post.body);
                par.put("date", post.date);

                return par;
            }
        };

        executeQuary(context, stringRequest);
    }

    public interface AddCommentListener {
        void isAddComment(boolean b);
    }

    public static class QComment {
        public int userid;
        public int postid;
        public String comment;
        public String date;
    }

    public static void addComment(@NonNull Context context, final AddCommentListener listener, final QComment comment) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isAdd = false;
                try {
                    JSONObject object = new JSONObject(response);
                    isAdd = object.getBoolean("isadd");

                    String err = object.getString("err");
                    if (!err.isEmpty()) Log.e(TAG, String.format("Add comment err: %s", err));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Add comment response: " + response);
                if (listener != null)
                    listener.isAddComment(isAdd);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getRequestUrl(ACTION_ADD_COMMENT),
                responseListener,
                errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> par = new HashMap<>();
                par.put("userid", String.valueOf(comment.userid));
                par.put("postid", String.valueOf(comment.postid));
                par.put("comment", comment.comment);
                par.put("date", comment.date);

                return par;
            }
        };

        executeQuary(context, stringRequest);
    }

    private static String getMd5(String s) {
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

    public static String getTimeStr(long ts) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(ts);
        String dateString = dateFormat.format(date);

        return dateString;
    }
}
