package space.iegrsy.companyblogmobile.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import space.iegrsy.companyblogmobile.R;
import space.iegrsy.companyblogmobile.adapter.CommentAdapter;
import space.iegrsy.companyblogmobile.helper.DataBaseUtil;
import space.iegrsy.companyblogmobile.models.CommentModel;
import space.iegrsy.companyblogmobile.models.PostModel;

public class PostFragment {
    private Context context;
    private int userid;

    private PostDialogHolder dialogHolder;

    public PostFragment(Context context, int userid) {
        this.context = context;
        this.userid = userid;
    }

    public void show(PostModel postModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(newInstance(postModel));

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    private View newInstance(PostModel postModel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_post_dialog, null, false);
        dialogHolder = new PostDialogHolder(view, userid, postModel);

        return view;
    }

    class PostDialogHolder {
        TextView txtTitle;
        TextView txtBody;
        TextView txtUser;
        TextView txtDate;
        EditText edComment;
        ImageButton btnSend;
        RecyclerView listComment;

        CommentAdapter commentAdapter;
        ArrayList<CommentModel> commentList;

        final int mUserid;
        final int mPostId;

        PostDialogHolder(View view, int userid, PostModel model) {
            mUserid = userid;
            mPostId = model.getPostid();

            txtTitle = view.findViewById(R.id.post_title);
            txtBody = view.findViewById(R.id.post_body);
            txtUser = view.findViewById(R.id.post_user);
            txtDate = view.findViewById(R.id.post_date);
            edComment = view.findViewById(R.id.post_dialog_comment_txt);
            btnSend = view.findViewById(R.id.post_dialog_comment_send_btn);
            listComment = view.findViewById(R.id.post_dialog_comment_list);

            commentList = new ArrayList<CommentModel>();
            commentAdapter = new CommentAdapter(context, commentList);

            listComment.setLayoutManager(new LinearLayoutManager(context));
            listComment.setAdapter(commentAdapter);
            btnSend.setOnClickListener(clickListener);

            DataBaseUtil.getComments(context, getCommentsListener, mPostId);

            initUI(model);
        }

        private void initUI(PostModel model) {
            txtTitle.setText(model.getTitle());
            txtDate.setText(model.getDate());
            txtBody.setText(model.getBody());
            txtUser.setText(model.getUser());
        }

        ImageButton.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = edComment.getText().toString().trim();
                if (c.isEmpty())
                    return;

                DataBaseUtil.QComment comment = new DataBaseUtil.QComment();
                comment.userid = mUserid;
                comment.postid = mPostId;
                comment.comment = c;
                comment.date = DataBaseUtil.getTimeStr(System.currentTimeMillis());

                DataBaseUtil.addComment(context, addCommentListener, comment);
            }
        };

        DataBaseUtil.AddCommentListener addCommentListener = new DataBaseUtil.AddCommentListener() {
            @Override
            public void isAddComment(boolean b) {
                if (b) {
                    edComment.setText("");
                    DataBaseUtil.getComments(context, getCommentsListener, mPostId);
                }
            }
        };

        DataBaseUtil.GetCommentsListener getCommentsListener = new DataBaseUtil.GetCommentsListener() {
            @Override
            public void onRequestComments(ArrayList<CommentModel> list) {
                commentAdapter.setPostList(list);
            }
        };
    }
}
