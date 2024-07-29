package binhntph28014.fpoly.gophoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.model.Comment;


public class ReviewShowAdapter extends RecyclerView.Adapter<ReviewShowAdapter.ReviewViewHolder> {
    private List<Comment> reviewList;
    private Context context;

    public void setReviewList(List<Comment> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewShowAdapter(List<Comment> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_review, parent, false);
        return new ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Comment review = reviewList.get(position);
        holder.nameTextView.setText(review.getName());
        holder.commentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImageView;
        public TextView nameTextView;
        public TextView commentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.img_avt_review);
            nameTextView = itemView.findViewById(R.id.txt_name_review);
            commentTextView = itemView.findViewById(R.id.txt_comment_review);
        }
    }
}
