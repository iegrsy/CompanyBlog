package space.iegrsy.companyblogmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<PostModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    PostAdapter(Context context, ArrayList<PostModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_post_row_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.txtUsername.setText(mData.get(position).getUser());
        holder.txtTitle.setText(mData.get(position).getTitle());
        holder.txtBody.setText(mData.get(position).getBody());
        holder.txtDate.setText(mData.get(position).getDate());
    }

    public PostModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setPostList(ArrayList<PostModel> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        TextView txtDate;
        TextView txtBody;
        TextView txtUsername;

        PostViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.post_title);
            txtDate = (TextView) itemView.findViewById(R.id.post_date);
            txtBody = (TextView) itemView.findViewById(R.id.post_body);
            txtUsername = (TextView) itemView.findViewById(R.id.post_user);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
