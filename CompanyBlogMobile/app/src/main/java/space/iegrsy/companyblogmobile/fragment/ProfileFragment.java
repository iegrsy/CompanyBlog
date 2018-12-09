package space.iegrsy.companyblogmobile.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import space.iegrsy.companyblogmobile.activity.LoginActivity;
import space.iegrsy.companyblogmobile.helper.AuthenticationUtil;
import space.iegrsy.companyblogmobile.helper.DataBaseUtil;
import space.iegrsy.companyblogmobile.adapter.PostAdapter;
import space.iegrsy.companyblogmobile.models.PostModel;
import space.iegrsy.companyblogmobile.R;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment {
    private Activity context;
    private final int userid;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;
    private ImageButton buttonLogout;

    private PostAdapter postAdapter;
    private ArrayList<PostModel> postList;

    private PostAdapter.ItemClickListener itemClickListener = new PostAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Toast.makeText(context, "You clicked " + postAdapter.getItem(position).getTitle() +
                    " on row number " + position, Toast.LENGTH_SHORT).show();
        }
    };

    public ProfileFragment(@NonNull Activity context, int userid) {
        this.context = context;
        this.userid = userid;

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(context, postList);
        postAdapter.setClickListener(itemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);

        listView = (RecyclerView) rootView.findViewById(R.id.profile_post_list);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.profile_post_list_refresh);
        buttonLogout = (ImageButton) rootView.findViewById(R.id.profile_post_btn_logout);

        listView.setLayoutManager(new LinearLayoutManager(context));

        listView.setAdapter(postAdapter);
        refreshLayout.setOnRefreshListener(refreshListener);
        buttonLogout.setOnClickListener(logoutClickListener);

        return rootView;
    }

    ImageButton.OnClickListener logoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to end the session?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AuthenticationUtil.setPrefIsAuth(context, false);
                    startActivity(new Intent(context, LoginActivity.class));
                }
            });
            builder.setNegativeButton("no", null);
            builder.setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            DataBaseUtil.getUserAllPost(context, postListener, userid);
        }
    };

    DataBaseUtil.GetPostListener postListener = new DataBaseUtil.GetPostListener() {
        @Override
        public void onRequestAllPost(ArrayList<PostModel> list) {
            postAdapter.setPostList(list);
            refreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        DataBaseUtil.getUserAllPost(context, postListener, userid);
    }
}
