package space.iegrsy.companyblogmobile.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import space.iegrsy.companyblogmobile.R;
import space.iegrsy.companyblogmobile.helper.DataBaseUtil;

@SuppressLint("ValidFragment")
public class NewPostFragment extends Fragment {
    private final Activity context;
    private final int userid;

    private EditText edTitle;
    private EditText edBody;
    private ImageButton btnSend;

    public NewPostFragment(Activity context, int userid) {
        this.context = context;
        this.userid = userid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_post_page, container, false);

        edTitle = (EditText) rootView.findViewById(R.id.new_post_title);
        edBody = (EditText) rootView.findViewById(R.id.new_post_body);
        btnSend = (ImageButton) rootView.findViewById(R.id.new_post_send_btn);
        btnSend.setOnClickListener(clickListener);

        return rootView;
    }

    ImageButton.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = edTitle.getText().toString().trim();
            String body = edBody.getText().toString().trim();
            String date = DataBaseUtil.getTimeStr(System.currentTimeMillis());

            if (title.length() <= 0 || body.length() <= 0) {
                Snackbar.make(context.findViewById(android.R.id.content),
                        "Is not empty title or body.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            DataBaseUtil.QPost post = new DataBaseUtil.QPost();
            post.userid = userid;
            post.title = title;
            post.body = body;
            post.date = date;

            DataBaseUtil.addPost(context, addPostListener, post);
        }
    };

    DataBaseUtil.AddPostListener addPostListener = new DataBaseUtil.AddPostListener() {
        @Override
        public void isAddPost(boolean b) {
            if (b)
                Snackbar.make(context.findViewById(android.R.id.content),
                        "Post added.", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(context.findViewById(android.R.id.content),
                        "Not post try again later.", Snackbar.LENGTH_SHORT).show();
        }
    };
}
