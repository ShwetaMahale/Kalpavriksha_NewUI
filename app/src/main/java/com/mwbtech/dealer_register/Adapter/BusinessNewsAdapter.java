package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.Dashboard.Support.BusinessNewsActivity;
import com.mwbtech.dealer_register.Dashboard.Support.NewsArticle;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;

/**
 * Created by Prerna Sharma on 09-02-2023
 */
public class BusinessNewsAdapter extends RecyclerView.Adapter<BusinessNewsAdapter.ViewHolder> {
    // setting the TAG for debugging purposes
    private static final String TAG = "ArticleAdapter";

    private final ArrayList<NewsArticle> mArrayList;
    private final Context mContext;

    public BusinessNewsAdapter(Context context, ArrayList<NewsArticle> list) {
        // initializing the constructor
        this.mContext = context;
        this.mArrayList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the layout with the article view  (R.layout.article_item)
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_business_news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // the parameter position is the index of the current article
        // getting the current article from the ArrayList using the position
        NewsArticle currentArticle = mArrayList.get(position);

        // setting the text of textViews
        holder.title.setText(currentArticle.getTitle());
        holder.description.setText(currentArticle.getDescription());

        // handling click event of the article
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // an intent to the WebActivity that display web pages
                Intent intent = new Intent(mContext, BusinessNewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url_key", currentArticle.getLink());

                // starting an Activity to display the page of the article
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // declaring the views
        private final TextView title;
        private final TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // assigning views to their ids
            title = itemView.findViewById(R.id.title_id);
            description = itemView.findViewById(R.id.description_id);
        }
    }

}
