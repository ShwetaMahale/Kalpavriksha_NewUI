package com.mwbtech.dealer_register.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mwbtech.dealer_register.Dashboard.Support.NewsArticle
import com.mwbtech.dealer_register.R
import com.mwbtech.dealer_register.databinding.NewsHeadlineLayoutBinding

class NewsHeadlineAdapter(private val context: Context, private val news: List<NewsArticle>, private val  open:(NewsArticle)->Unit) : RecyclerView.Adapter<NewsHeadlineAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.news_headline_layout, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.button.text = news[position].title
        holder.itemView.setOnClickListener { open(news[position]) }
    }

    override fun getItemCount(): Int = news.size
    class MyViewHolder(val binding: NewsHeadlineLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}