package com.mwbtech.dealer_register.Dashboard.Support;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.Adapter.BusinessNewsAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealer;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerRequest;
import com.mwbtech.dealer_register.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Prerna Sharma on 09-02-2023
 */
public class BusinessNewsListActivity extends AppCompatActivity {

    // TODO : set the API_KEY variable to your api key
    private static final String API_KEY = "cf95f461f2mshe7d87580eb1d7cep1f2d2bjsnce8cbf423936";

    // setting the TAG for debugging purposes
    private static final String TAG = "MainActivity";

    // declaring the views
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ImageView back;

    // declaring an ArrayList of articles
    private ArrayList<NewsArticle> mArticleList;
    String _categoryName;
    TextView _nonews;

    private BusinessNewsAdapter mArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_news_list);

        // assigning views to their ids
        mProgressBar = findViewById(R.id.progressbar_id);
        _nonews=findViewById(R.id.no_data);
        mRecyclerView = findViewById(R.id.recyclerview_id);
        back = findViewById(R.id.back);
        if (getIntent() != null) {
            _categoryName = (String) getIntent().getStringExtra("categoryName");
        }
        Log.e("category name...",_categoryName);
        back.setOnClickListener(v -> onBackPressed());
        // setting the recyclerview layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initializing the ArrayList of articles
        mArticleList = new ArrayList<>();
        Log.e("category name...",_categoryName);
        // calling get_news_from_api()
        try {
            get_news_from_api();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void get_news_from_api() throws IOException {
        // clearing the articles list before adding news ones
        mArticleList.clear();

        OkHttpClient client = new OkHttpClient();
        Request request;
       /* Request request = new Request.Builder()
                .url("https://yahoo-finance15.p.rapidapi.com/api/yahoo/ne/news/AAPL")
                .get()
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", "yahoo-finance15.p.rapidapi.com")
                .build();*/
        //to get the news related to US

        if(!_categoryName.equals("us")){
        request = new Request.Builder()
                    //.url("https://newsapi.org/v2/top-headlines?country=in&category="+_categoryName+"&apiKey=36771134d13842beaf674e8520d01661")
                    .url("http://192.168.1.213/masterportal/news/get/"+_categoryName)
                    .get()
                    .build();
        }
        else{
            request = new Request.Builder()
                    //.url("https://newsapi.org/v2/top-headlines?country=us&apiKey=36771134d13842beaf674e8520d01661")
                    .url("http://192.168.1.213/masterportal/news/get/international")
                    .get()
                    .build();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {

                    final String myResponse = response.body().string();
                    BusinessNewsListActivity.this.runOnUiThread(() -> {

                        // disabling the progress bar
                        mProgressBar.setVisibility(View.GONE);
                        // handling the response
                        try {
                            JSONObject json = new JSONObject(myResponse);
//                        NewsResponse newsResponse = json.getJSONObject("item").getJSONObject("item");
                            // storing the response in a JSONArray
                            //JSONArray articles = json.getJSONArray("item");
                            JSONArray articles = json.getJSONObject("data").getJSONArray("articles");
                            if(articles.length()==0){
                                _nonews.setVisibility(View.VISIBLE);
                            }
                            else{
                                _nonews.setVisibility(View.GONE);
                            }
                            // looping through all the articles
                            // to access them individually
                            for (int j = 0; j < articles.length(); j++) {
                                // accessing each article object in the JSONArray
                                JSONObject article = articles.getJSONObject(j);
                                // initializing an empty ArticleModel
                                NewsArticle currentArticle = new NewsArticle();
                                // storing values of the article object properties
                                String title = article.getString("title");
                                String description = article.getString("description");
                                //String url = article.getString("link");
                                String url = article.getString("url");
                               // String publishedAt = article.getString("pubDate");
                                String publishedAt = article.getString("publishedAt");

                                // setting the values of the ArticleModel
                                // using the set methods
                                currentArticle.setTitle(title);
                                currentArticle.setDescription(description);
                                currentArticle.setLink(url);

                                // adding an article to the articles List
                                mArticleList.add(currentArticle);
                            }

                            // setting the adapter
                            mArticleAdapter = new BusinessNewsAdapter(getApplicationContext(), mArticleList);
                            mRecyclerView.setAdapter(mArticleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // logging the JSONException LogCat
                            Log.d(TAG, "Error : " + e.getMessage());
                        }

                    });

                }
            }
        });
//        try (Response response = client.newCall(request).execute()) {
//            return response.body();
        // converts response into an array of books
//            ObjectMapper mapper = new ObjectMapper();
//            mArticleList = mapper.readValue(response.body().bytes(), ArrayList<NewsArticle>.class);
//        }
//        Response response = client.newCall(request).execute();

        // Making a GET Request using Fast
        // Android Networking Library
        // the request returns a JSONObject containing
        // news articles from the news api
        // or it will return an error
        /*AndroidNetworking.get("https://indianstockexchange.p.rapidapi.com/index.php?id=4081835")
                .addQueryParameter("country", "in")
                .addQueryParameter("apiKey",API_KEY)
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        // disabling the progress bar
                        mProgressBar.setVisibility(View.GONE);

                        // handling the response
                        try {

                            // storing the response in a JSONArray
                            JSONArray articles=response.getJSONArray("articles");

                            // looping through all the articles
                            // to access them individually
                            for (int j=0;j<articles.length();j++)
                            {
                                // accessing each article object in the JSONArray
                                JSONObject article=articles.getJSONObject(j);

                                // initializing an empty ArticleModel
                                NewsArticle currentArticle=new NewsArticle();

                                // storing values of the article object properties
                                String author=article.getString("author");
                                String title=article.getString("title");
                                String description=article.getString("description");
                                String url=article.getString("url");
                                String urlToImage=article.getString("urlToImage");
                                String publishedAt=article.getString("publishedAt");
                                String content=article.getString("content");

                                // setting the values of the ArticleModel
                                // using the set methods
                                currentArticle.setAuthor(author);
                                currentArticle.setTitle(title);
                                currentArticle.setDescription(description);
                                currentArticle.setUrl(url);
                                currentArticle.setUrlToImage(urlToImage);
                                currentArticle.setPublishedAt(publishedAt);
                                currentArticle.setContent(content);

                                // adding an article to the articles List
                                mArticleList.add(currentArticle);
                            }

                            // setting the adapter
                            mArticleAdapter=new BusinessNewsAdapter(getApplicationContext(),mArticleList);
                            mRecyclerView.setAdapter(mArticleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // logging the JSONException LogCat
                            Log.d(TAG,"Error : "+e.getMessage());
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // logging the error detail and response to LogCat
                        Log.d(TAG,"Error detail : "+error.getErrorDetail());
                        Log.d(TAG,"Error response : "+error.getResponse());
                    }
                });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        BusinessNewsListActivity.this.finish();
    }
}
