package space.iegrsy.companyblogmobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import space.iegrsy.companyblogmobile.R;
import space.iegrsy.companyblogmobile.models.CommentModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<CommentModel> mData;
    private LayoutInflater mInflater;

    public CommentAdapter(Context context, ArrayList<CommentModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_comment_row_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.txtUsername.setText(mData.get(position).getUsername());
        holder.txtDate.setText(mData.get(position).getDate());
        holder.txtComment.setText(mData.get(position).getComment());
    }

    public CommentModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setPostList(ArrayList<CommentModel> list) {
        mData = list;
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        TextView txtDate;
        TextView txtComment;

        CommentViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.comment_user);
            txtDate = (TextView) itemView.findViewById(R.id.comment_date);
            txtComment = (TextView) itemView.findViewById(R.id.comment_body);
        }
    }
}
