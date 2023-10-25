package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RcvInboxAdapter extends RecyclerView.Adapter<RcvInboxAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<InboxDealer> dealerRegisters;
    List<InboxDealer> dealerRegisterList;
    ItemCreationAdapterListener listener;
    List<InboxDealer> selectedItemCreationArrayList;
    Customer_Interface customer_interface;
    String date,time;
    String Token;
    int var;
    PrefManager prefManager;
    private SparseBooleanArray mSelectedItemsIds;
    private final SparseBooleanArray mSelectedItemStar;
    private static int currentSelectedIndex = -1;
    private final List<InboxDealer> dealerList;
    private final List<InboxDealer> dealerListStar;
    private final starClickEvent clickEvent;

    Dialog mDialogCity;
    PhotoView ivimage;
    MyViewHolder myViewHolder;
    public RcvInboxAdapter(Context context, List<InboxDealer> dealerRegisters, ItemCreationAdapterListener listener, starClickEvent starClickEvent) {
        this.context = context;
        this.clickEvent = starClickEvent;
        this.dealerRegisters = dealerRegisters;
        this.dealerRegisterList = dealerRegisters;
        mSelectedItemsIds = new SparseBooleanArray();
        mSelectedItemStar = new SparseBooleanArray();
        this.listener = listener;
        prefManager = new PrefManager(context);
        selectedItemCreationArrayList = new ArrayList<>();
        dealerList = new ArrayList<>();
        dealerListStar = new ArrayList<>();
        Token = prefManager.getToken().get(TOKEN);
    }

    public interface starClickEvent{
        void starclick(int queryid, int custid, int id);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_chat_item, parent, false);
            myViewHolder = new MyViewHolder(itemView);
            return new MyViewHolder(itemView, context);
        }
        return null;
    }


    private String ConvertdateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mydate = null;
        try{

            mydate = dateFormat.parse(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM");
        date = dateFormat1.format(mydate);

        return date;

    }

    private String Convertdate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mydate = null;
        try{

            mydate = dateFormat.parse(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat1.format(mydate);

        return date;

    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        myViewHolder = holder;
        holder.setIsRecyclable(false);
        final int pos = position;
        InboxDealer itemCreation = dealerRegisterList.get(position);
        if(itemCreation.getIsRead() == 0){
            holder.textNotify.setVisibility(View.VISIBLE);
        }else {
            holder.textNotify.setVisibility(View.GONE);
        }
        holder.itemName.setText(itemCreation.getFirmName());
        holder.cityname.setText("City : "+itemCreation.getVillageLocalityname());

        Log.e("demand","1....."+itemCreation.getBusinessDemand());
        if (!TextUtils.isEmpty(itemCreation.getBusinessDemand()))
        {

            holder.itemChildName.setText("Product Name : "+itemCreation.getChildCategoryName());
            holder.itemQuantity.setText("Business Demand : "+itemCreation.getBusinessDemand());
        }else
        {
            holder.itemChildName.setText("Looking for : "+itemCreation.getChildCategoryName());
            holder.itemQuantity.setText("Requirement : "+itemCreation.getRequirementName());
        }

        if (!TextUtils.isEmpty(itemCreation.getEnquiryDate()))
        {
            String Enq_Date = Convertdate(itemCreation.getEnquiryDate().substring(0,10));
            holder.EnquiryDate.setText("Enquiry Date : "+Enq_Date);
        }

        if(itemCreation.getEnquiryType()!=null)
        {
            holder.EnquiryType.setText("EnquiryType : "+itemCreation.getEnquiryType());
            if(!TextUtils.isEmpty(itemCreation.getTransactionType())) {
                holder.Transaction_type.setText("Transaction Type :" + itemCreation.getTransactionType());
            }
            else
                holder.Transaction_type.setVisibility(View.GONE);
            System.out.println("HHHHHHHHHHH"+itemCreation.getEnquiryType());
            if(itemCreation.getEnquiryType().equals("Group Enquiry"))
            {
                holder.TileRelative.setBackgroundColor(Color.parseColor("#c4ede2"));
            }
            if(itemCreation.getEnquiryType().equals("Advertisement Enquiry")){
                holder.TileRelative.setBackgroundColor(Color.parseColor("#cddcfe"));
            }
        }
        if (!TextUtils.isEmpty(itemCreation.getSenderImage()))
        {
            Glide.with(context).load(itemCreation.getSenderImage()).into(holder.ProfileImg);
            holder.ProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertMethod(itemCreation.getSenderImage());
                }
            });
        }
        if (!TextUtils.isEmpty(itemCreation.getLastUpdatedMsgDate()))
        {
            date = ConvertdateFormat(itemCreation.getLastUpdatedMsgDate().substring(0,10));
            time = itemCreation.getLastUpdatedMsgDate().substring(11,16);
        }else
        {
            date = "";
            time = "";
        }

        holder.date.setText(""+date+" "+time);

        if(itemCreation.getIsFavorite() == 1){
            mSelectedItemStar.put(position,true);

            Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.full_star);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
            holder.imgStar.setImageDrawable(wrappedDrawable);
//            holder.imgStar.setImageResource(R.drawable.full_star);
        }else {
            mSelectedItemStar.put(position,false);

            Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.star);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
            holder.imgStar.setImageDrawable(wrappedDrawable);
//            holder.imgStar.setImageResource(R.drawable.star);
        }

        holder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedItemStar.get(position) == view.isActivated()){
                    mSelectedItemStar.put(position,true);

                    Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.full_star);
                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                    wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
                    holder.imgStar.setImageDrawable(wrappedDrawable);
//                    holder.imgStar.setImageResource(R.drawable.full_star);

                    clickEvent.starclick(dealerRegisterList.get(position).getQueryId(),dealerRegisterList.get(position).getCustID(),1);
                }else {
                    mSelectedItemStar.put(position,false);
//                    holder.imgStar.setImageResource(R.drawable.star);

                    Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.star);
                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                    wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
                    holder.imgStar.setImageDrawable(wrappedDrawable);

                    clickEvent.starclick(dealerRegisterList.get(position).getQueryId(),dealerRegisterList.get(position).getCustID(),2);
                }
            }
        });

        holder.itemView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                : Color.TRANSPARENT);

    }

    @Override
    public int getItemCount() {
        return dealerRegisters == null ? 0 : dealerRegisterList.size();
    }

    public void alertMethod(String image){
        mDialogCity = new Dialog(context);
        mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogCity.setContentView(R.layout.zoom_layout);
        mDialogCity.setCancelable(true);
        ivimage = (PhotoView) mDialogCity.findViewById(R.id.photo_view);
        mDialogCity.show();
        try {
            Picasso.get().load(image).into(ivimage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dealerRegisterList = dealerRegisters;
                } else {
                    ArrayList<InboxDealer> filteredList = new ArrayList<>();
                    for (InboxDealer row : dealerRegisters) {
                        if (row.getFirmName().toLowerCase().contains(charString.toLowerCase()) || row.getEnquiryType().toLowerCase().contains(charString.toLowerCase()) || row.getChildCategoryName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    dealerRegisterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dealerRegisterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dealerRegisterList = (ArrayList<InboxDealer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity,date,itemChildName,cityname,EnquiryDate,EnquiryType,Transaction_type;
        public  TextView textNotify;
        ImageView imgStar,ProfileImg;
        RelativeLayout TileRelative;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.chat_name_txt);
            itemQuantity = (TextView) itemView.findViewById(R.id.chat_description);
            itemChildName = (TextView)itemView.findViewById(R.id.chat_child_name);
            cityname = (TextView)itemView.findViewById(R.id.chat_city);
            EnquiryDate = (TextView)itemView.findViewById(R.id.Enq_Date);
            EnquiryType = (TextView) itemView.findViewById(R.id.enq_type);
            Transaction_type=(TextView) itemView.findViewById(R.id.trans_type) ;
            date = (TextView)itemView.findViewById(R.id.chat_date_txt);
            textNotify = itemView.findViewById(R.id.textNotify);
            imgStar = itemView.findViewById(R.id.star);
            ProfileImg=(ImageView) itemView.findViewById(R.id.icon);
            TileRelative = (RelativeLayout) itemView.findViewById(R.id.rec_reltive);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callbac
                    listener.onItemSelected(dealerRegisterList.get(getAdapterPosition()));
                }
            });
        }
    }


    public void removeItem(int position) {
        dealerRegisters.remove(position);
        notifyItemRemoved(position);
    }

    public List<InboxDealer> getResultDealerList()
    {
        selectedItemCreationArrayList.clear();
        for(int i=0; i< dealerRegisterList.size();i++){

            InboxDealer categoryProduct = dealerRegisterList.get(i);
            if(categoryProduct.isChecked() == true){
                selectedItemCreationArrayList.add(new InboxDealer(categoryProduct.getQueryId(),categoryProduct.getCustID(),categoryProduct.getFirmName(), categoryProduct.getMobileNumber(), categoryProduct.getEmailID(),categoryProduct.getVillageLocalityname(),categoryProduct.getBusinessDemand(),categoryProduct.getPurposeOfBusiness(),categoryProduct.getRequirements(),categoryProduct.getProductPhoto(),categoryProduct.getLastUpdatedMsgDate(),categoryProduct.getIsRead(),categoryProduct.getChildCategoryName(),categoryProduct.getIsFavorite(),categoryProduct.getEnquiryType(),categoryProduct.getSenderImage()));
            }
        }

        return selectedItemCreationArrayList;
    }


    public interface ItemCreationAdapterListener {
        void onItemSelected(InboxDealer itemCreation);
    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

   /* public void selectAll() {
        for (int i = 0; i < getItemCount(); i++)
         mSelectedItemsIds.put(i, true);
        notifyDataSetChanged();

    }*/
    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++)
            if (dealerRegisterList.get(i).getIsFavorite()==1)
            {
                mSelectedItemsIds.delete(i);
                CustomToast.showToast(context, "Please unfavourite your chat to delete");
            }
            else
            {
                mSelectedItemsIds.put(i, true);
            }
        notifyDataSetChanged();
    }



    public List<InboxDealer> getSelectedStar(){

        for(int i = 0 ; i< mSelectedItemStar.size(); i++){

            if(mSelectedItemStar.get(i) == true){

                InboxDealer inboxDealer = dealerRegisters.get(i);
                if(inboxDealer.getIsRead() == 0){
                    myViewHolder.textNotify.setVisibility(View.VISIBLE);
                }else {
                    myViewHolder.textNotify.setVisibility(View.GONE);
                }

                dealerListStar.add(new InboxDealer(inboxDealer.getQueryId(),inboxDealer.getCustID(),inboxDealer.getFirmName(), inboxDealer.getMobileNumber(), inboxDealer.getEmailID(),inboxDealer.getVillageLocalityname(),inboxDealer.getBusinessDemand(),inboxDealer.getPurposeOfBusiness(),inboxDealer.getRequirements(),inboxDealer.getProductPhoto(),inboxDealer.getLastUpdatedMsgDate(),inboxDealer.getIsRead(),inboxDealer.getChildCategoryName(),1,inboxDealer.getEnquiryType(),inboxDealer.getSenderImage()));
            }
        }
        return dealerListStar;

    }

    public int getSelectedItemCount() {
        return mSelectedItemsIds.size();
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    //Put or delete selected position into SparseBooleanArray
    /*public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }*/
    public void selectView(int position, boolean value) {
        if (value)
            if(dealerRegisterList.get(position).getIsFavorite()==1){
                mSelectedItemsIds.delete(position);
                CustomToast.showToast(context, "Please unfavourite your chat to delete");
            }
            else{
                mSelectedItemsIds.put(position, value);
            }
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {

        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    public SparseBooleanArray getmSelectedItemStar(){
        return mSelectedItemStar;
    }

    private void postFavouriteStar(int Cust_ID, int queryId, int receiverId, int isFavorite) {

        ProgressDialog progressBar = ShowProgressDialog.createProgressDialog(context);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Void> call = customer_interface.postFavouriteData("bearer "+Token,Cust_ID,queryId,receiverId,isFavorite);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                int statusCode = response.code();
                switch (statusCode){
                    case 200:
                        progressBar.dismiss();
                        break;

                    case 404:
                        progressBar.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(context,"Server Error ");
                        progressBar.dismiss();
                        break;
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.dismiss();
                CustomToast.showToast(context,"Error " + t.getMessage());
            }
        });
    }

    public List<InboxDealer> getUpdatedList()
    {
        return dealerRegisterList;
    }

}
