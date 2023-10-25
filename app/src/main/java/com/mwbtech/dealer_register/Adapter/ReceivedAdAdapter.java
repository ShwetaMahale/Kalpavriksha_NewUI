package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ReceivedAdAdapter extends RecyclerView.Adapter implements Filterable {
    Context context;

    public static List<AdDetailsModel> adDetailsModels;
    List<AdDetailsModel> detailsModelList;
    public static AdClickEvent adClickEvent;



    public  interface AdClickEvent {
        void adclick(AdDetailsModel adDetailsModel);
    }

    public ReceivedAdAdapter(Context context, List<AdDetailsModel> adDetailsModels, AdClickEvent event) {
        this.context = context;
        ReceivedAdAdapter.adDetailsModels = adDetailsModels;
        this.detailsModelList = adDetailsModels;
        adClickEvent = event;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView tvExpireDate, tvAdType, tvProduct;
        ImageView imageViewAd;



        public ImageViewHolder(View itemView) {
            super(itemView);
            this.tvProduct = itemView.findViewById(R.id.adProduct);
            this.tvExpireDate = itemView.findViewById(R.id.ad_date_txt);
            this.tvAdType = itemView.findViewById(R.id.adType);
            this.imageViewAd = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adClickEvent.adclick(adDetailsModels.get(getAdapterPosition()));
                }
            });
        }

    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView tvExpireDate, tvAdType, tvProduct, tvText;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.tvProduct = itemView.findViewById(R.id.adProduct);
            this.tvExpireDate = itemView.findViewById(R.id.ad_date_txt);
            this.tvAdType = itemView.findViewById(R.id.adType);
            this.tvText = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adClickEvent.adclick(adDetailsModels.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case AdDetailsModel.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_ad_image_item_layout, parent, false);
                return new ImageViewHolder(view);
            case AdDetailsModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_ad_text_item_layout, parent, false);
                return new TextViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("type...",String.valueOf(adDetailsModels.get(position).type));
        switch (adDetailsModels.get(position).type) {
            case 0:
                return AdDetailsModel.IMAGE_TYPE;
            case 1:
                return AdDetailsModel.TEXT_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        AdDetailsModel adDetailsModel = adDetailsModels.get(listPosition);
        if (adDetailsModel != null) {
            switch (adDetailsModel.type) {
                case AdDetailsModel.IMAGE_TYPE:
                    ((ImageViewHolder) holder).tvProduct.setText(""+adDetailsModel.getProductName());
                    ((ImageViewHolder) holder).tvAdType.setText(""+adDetailsModel.getAdvertisementType());
                    //((ImageViewHolder) holder).tvExpireDate.setText(""+adDetailsModel.getToDateStr());
                    ((ImageViewHolder) holder).tvExpireDate.setVisibility(View.GONE);
                    Glide
                            .with(context)
                            .load(adDetailsModel.getAdImageURL())
                            .into(((ImageViewHolder) holder).imageViewAd);
                    break;

                case AdDetailsModel.TEXT_TYPE:
                    ((TextViewHolder) holder).tvProduct.setText(""+adDetailsModel.getProductName());
                    ((TextViewHolder) holder).tvAdType.setText(""+adDetailsModel.getAdvertisementType());
                    //((TextViewHolder) holder).tvExpireDate.setText(""+adDetailsModel.getToDateStr());
                    ((TextViewHolder) holder).tvExpireDate.setVisibility(View.GONE);
                    ((TextViewHolder) holder).tvText.setText(""+adDetailsModel.getAdText());
                    ((TextViewHolder) holder).tvText.setSelected(true);
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return adDetailsModels == null ? 0 :adDetailsModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    adDetailsModels = detailsModelList;
                } else {
                    ArrayList<AdDetailsModel> filteredList = new ArrayList<>();
                    for (AdDetailsModel row : adDetailsModels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase()) || row.getAdvertisementType().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    adDetailsModels = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = adDetailsModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                adDetailsModels = (ArrayList<AdDetailsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
