package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdvertisementAdapter extends RecyclerView.Adapter implements Filterable {
    Context context;

    public static  List<AdDetailsModel> adDetailsModels;
    List<AdDetailsModel> detailsModelList;
    public static AdClickEvent adClickEvent;
    public static AdClickEvent1 buttonClick;
    private final SparseBooleanArray mSelectedItemsIds;
    int adID;

    public  interface AdClickEvent {
        void adclick(AdDetailsModel adDetailsModel);
    }
    public  interface AdClickEvent1 {
        void buttonClick(AdDetailsModel adDetailsModel);
    }

    public MyAdvertisementAdapter(Context context, List<AdDetailsModel> adDetailsModels, AdClickEvent event,AdClickEvent1 button) {
        this.context = context;
        MyAdvertisementAdapter.adDetailsModels = adDetailsModels;
        this.detailsModelList = adDetailsModels;
        mSelectedItemsIds = new SparseBooleanArray();
        adClickEvent = event;
        buttonClick=button;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvAdType, tvAdStatus, tvPaymentStatus, tvProduct,remark_status,tvAdName;
        ImageView imageViewAd;
        LinearLayout remarksLayout,duelayout;
        ImageButton ad_delete;
        Button make_payment;
        TextView dueDate;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.tvProduct = itemView.findViewById(R.id.adProduct);
            this.tvAdType = itemView.findViewById(R.id.adType);
            this.tvAdName=itemView.findViewById(R.id.adName);
            this.tvAdStatus = itemView.findViewById(R.id.approvalSts);
            this.tvPaymentStatus = itemView.findViewById(R.id.paymentSts);
            this.imageViewAd = itemView.findViewById(R.id.ivAd);
            this.ad_delete=itemView.findViewById(R.id.ad_delete);
            this.make_payment=itemView.findViewById(R.id.make_payment);
            this.remark_status=itemView.findViewById(R.id.remarkStatus);
            this.remarksLayout=itemView.findViewById(R.id.remarksLayout);
            this.duelayout=itemView.findViewById(R.id.dueLayout);
            this.dueDate=itemView.findViewById(R.id.duedate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adClickEvent.adclick(adDetailsModels.get(getAdapterPosition()));
                }
            });

            make_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClick.buttonClick(adDetailsModels.get(getAdapterPosition()));
                }
            });
        }

    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView tvAdType, tvAdStatus, tvPaymentStatus, tvProduct, tvText,make_payment,remark_status,tvAdName,dueDate;
        ImageButton ad_delete;
        LinearLayout remarksLayout,duelayout;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.tvProduct = itemView.findViewById(R.id.adProduct);
            this.tvAdType = itemView.findViewById(R.id.adType);
            this.tvAdName=itemView.findViewById(R.id.adName);
            this.tvAdStatus = itemView.findViewById(R.id.approvalSts);
            this.tvPaymentStatus = itemView.findViewById(R.id.paymentSts);
            this.tvText = itemView.findViewById(R.id.title);
            this.ad_delete=itemView.findViewById(R.id.ad_delete);
            this.make_payment=itemView.findViewById(R.id.make_payment);
            this.remark_status=itemView.findViewById(R.id.remarkStatus);
            remarksLayout=itemView.findViewById(R.id.remarksLayout);
            this.dueDate=itemView.findViewById(R.id.duedate);
            this.duelayout=itemView.findViewById(R.id.dueLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adClickEvent.adclick(adDetailsModels.get(getAdapterPosition()));
                }
            });
            make_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClick.buttonClick(adDetailsModels.get(getAdapterPosition()));
                }
            });
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case AdDetailsModel.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ads_image_type_item_layout, parent, false);
                return new ImageViewHolder(view);
            case AdDetailsModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ads_text_type_item_layout, parent, false);
                return new TextViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

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
        adID=adDetailsModel.getAdvertisementMainID();
        if (adDetailsModel != null) {
            switch (adDetailsModel.type) {
                case AdDetailsModel.IMAGE_TYPE:
                    ((ImageViewHolder) holder).tvProduct.setText(""+adDetailsModel.getProductName());
                    ((ImageViewHolder) holder).tvAdType.setText(""+adDetailsModel.getAdvertisementType());
                    ((ImageViewHolder) holder).tvAdName.setText(""+adDetailsModel.getAdvertisementName());
                    ((ImageViewHolder) holder).tvAdStatus.setText(""+adDetailsModel.getApprovalStatus());
                    ((ImageViewHolder) holder).tvPaymentStatus.setText(""+adDetailsModel.getPaymentStatus());
                    ((ImageViewHolder) holder).dueDate.setText(""+adDetailsModel.getPaymentDueDate());
                    ((ImageViewHolder) holder).itemView.setBackgroundColor(Color.WHITE);
                    if (!TextUtils.isEmpty(adDetailsModel.getAdText())) {
                        Glide
                                .with(context)
                                .load(adDetailsModel.getAdImageURL())
                                .into(((ImageViewHolder) holder).imageViewAd);
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Approved")&& adDetailsModel.isMakePaymentAllowed()==true){

                        ((ImageViewHolder) holder).make_payment.setVisibility(View.VISIBLE);
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Pending")){

                        ((ImageViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).make_payment.setVisibility(View.GONE);
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Approved")&& adDetailsModel.isMakePaymentAllowed()==false){

                        ((ImageViewHolder) holder).make_payment.setVisibility(View.GONE);
                    }
                    if(adDetailsModel.getPaymentStatus().equals("Approved")){
                        ((ImageViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).make_payment.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#c4ede2"));
                    }
                    if(adDetailsModel.isExpired()==true){
                        ((ImageViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).make_payment.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).remarksLayout.setVisibility(View.VISIBLE);
                        ((ImageViewHolder) holder).remark_status.setText(""+adDetailsModel.getRemarks());
                        ((ImageViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#ff6666"));
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Rejected")){
                        ((ImageViewHolder) holder).remarksLayout.setVisibility(View.VISIBLE);
                        ((ImageViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((ImageViewHolder) holder).remark_status.setText(""+adDetailsModel.getRemarks());
                    }
                    break;
                case AdDetailsModel.TEXT_TYPE:
                    ((TextViewHolder) holder).tvProduct.setText(""+adDetailsModel.getProductName());
                    ((TextViewHolder) holder).tvAdType.setText(""+adDetailsModel.getAdvertisementType());
                    ((TextViewHolder) holder).tvAdName.setText(""+adDetailsModel.getAdvertisementName());
                    ((TextViewHolder) holder).tvAdStatus.setText(""+adDetailsModel.getApprovalStatus());
                    ((TextViewHolder) holder).tvPaymentStatus.setText(""+adDetailsModel.getPaymentStatus());
                    ((TextViewHolder) holder).tvText.setText(""+adDetailsModel.getAdText());
                    ((TextViewHolder) holder).tvText.setSelected(true);
                    ((TextViewHolder) holder).dueDate.setText(""+adDetailsModel.getPaymentDueDate());
                    ((TextViewHolder) holder).itemView.setBackgroundColor(Color.WHITE);
                    if(adDetailsModel.getApprovalStatus().equals("Approved")&& adDetailsModel.isMakePaymentAllowed()==true){

                        ((TextViewHolder) holder).make_payment.setVisibility(View.VISIBLE);
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Pending")){

                        ((TextViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((TextViewHolder) holder).make_payment.setVisibility(View.GONE);
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Approved")&& adDetailsModel.isMakePaymentAllowed()==false){

                        ((TextViewHolder) holder).make_payment.setVisibility(View.GONE);
                    }
                    if(adDetailsModel.getPaymentStatus().equals("Approved")){
                        ((TextViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((TextViewHolder) holder).make_payment.setVisibility(View.GONE);
                        ((TextViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#c4ede2"));
                    }
                    if(adDetailsModel.isExpired()==true){
                        ((TextViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((TextViewHolder) holder).make_payment.setVisibility(View.GONE);
                        ((TextViewHolder) holder).remarksLayout.setVisibility(View.VISIBLE);
                        ((TextViewHolder) holder).remark_status.setText(""+adDetailsModel.getRemarks());
                        ((TextViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#ff6666"));
                    }
                    if(adDetailsModel.getApprovalStatus().equals("Rejected")){
                        ((TextViewHolder) holder).remarksLayout.setVisibility(View.VISIBLE);
                        ((TextViewHolder) holder).duelayout.setVisibility(View.GONE);
                        ((TextViewHolder) holder).remark_status.setText(""+adDetailsModel.getRemarks());
                    }
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
//                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())|| row.getAdvertisementName().toLowerCase().contains(charString.toLowerCase()) || row.getAdvertisementType().toLowerCase().contains(charString.toLowerCase()) || row.getApprovalStatus().toLowerCase().contains(charString.toLowerCase()) || row.getPaymentStatus().toLowerCase().contains(charString.toLowerCase())) {
//                            filteredList.add(row);
//                        }
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())|| row.getAdvertisementName().toLowerCase().contains(charString.toLowerCase()) || row.getAdvertisementType().toLowerCase().contains(charString.toLowerCase()) || row.getApprovalStatus().toLowerCase().contains(charString.toLowerCase()) || row.getPaymentStatus().toLowerCase().contains(charString.toLowerCase())) {
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
