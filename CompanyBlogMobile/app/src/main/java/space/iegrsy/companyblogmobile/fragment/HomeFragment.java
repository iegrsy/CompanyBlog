package space.iegrsy.companyblogmobile.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import space.iegrsy.companyblogmobile.helper.DataBaseUtil;
import space.iegrsy.companyblogmobile.adapter.PostAdapter;
import space.iegrsy.companyblogmobile.models.PostModel;
import space.iegrsy.companyblogmobile.R;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    private Activity context;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;

    private PostAdapter postAdapter;
    private ArrayList<PostModel> postList;

    private PostAdapter.ItemClickListener itemClickListener = new PostAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Toast.makeText(context, "You clicked " + postAdapter.getItem(position).getTitle() +
                    " on row number " + position, Toast.LENGTH_SHORT).show();
        }
    };

    public HomeFragment(@NonNull Activity context) {
        this.context = context;
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(context, postList);
        postAdapter.setClickListener(itemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);

        listView = (RecyclerView) rootView.findViewById(R.id.home_post_list);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_post_list_refresh);

        listView.setLayoutManager(new LinearLayoutManager(context));

        listView.setAdapter(postAdapter);
        refreshLayout.setOnRefreshListener(refreshListener);

        return rootView;
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            DataBaseUtil.getAllPost(context, postListener);
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
        DataBaseUtil.getAllPost(context, postListener);
    }
}
