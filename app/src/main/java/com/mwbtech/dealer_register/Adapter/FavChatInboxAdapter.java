package com.mwbtech.dealer_register.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavChatInboxAdapter extends RecyclerView.Adapter<FavChatInboxAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<InboxDealer> dealerRegisters;
    List<InboxDealer> dealerRegisterList;
    ItemCreationAdapterListener listener;
    List<InboxDealer> selectedItemCreationArrayList;
    Dialog mDialogCity;
    PhotoView ivimage;

    String image;
    String date,time;
    int var;
    PrefManager prefManager;
    private SparseBooleanArray mSelectedItemsIds;
    private final SparseBooleanArray mSelectedItemStar;
    private static int currentSelectedIndex = -1;
    private final List<InboxDealer> dealerList;
    private final List<InboxDealer> dealerListStar;
    private final starClickEvent clickEvent;
    MyViewHolder myViewHolder;
    //ImageView imageView;

    public FavChatInboxAdapter(Context context, List<InboxDealer> dealerRegisters, ItemCreationAdapterListener listener, starClickEvent starClickEvent) {
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
    }

    public interface starClickEvent{
        void starclick(int queryid, int custid, int id);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_chat_item, parent, false);
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
        if (!TextUtils.isEmpty(itemCreation.getBusinessDemand()))
        {
            holder.itemChildName.setText("Product Name : "+itemCreation.getChildCategoryName());
            holder.itemQuantity.setText("Business Demand : "+itemCreation.getBusinessDemand());
        }else
        {
            holder.itemChildName.setText("Looking for : "+itemCreation.getChildCategoryName());
            holder.itemQuantity.setText("Requirement : "+itemCreation.getRequirementName());
        }

        date = ConvertdateFormat(itemCreation.getLastUpdatedMsgDate().substring(0,10));
        time = itemCreation.getLastUpdatedMsgDate().substring(11,16);
        holder.date.setText(""+date+" "+time);

        if (!TextUtils.isEmpty(itemCreation.getEnquiryDate()))
        {
            String Enq_Date = Convertdate(itemCreation.getEnquiryDate().substring(0,10));
            holder.EnquiryDate.setText("Enquiry Date : "+Enq_Date);
        }

        if (!TextUtils.isEmpty(itemCreation.getSenderImage()))
        {
            Glide.with(context).load(itemCreation.getSenderImage()).into(holder.PrifileImg);
            holder.PrifileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertMethod(itemCreation.getSenderImage());
                }
            });
        }

        if(itemCreation.getEnquiryType()!=null)
        {
            holder.EnquiryType.setText("EnquiryType : "+itemCreation.getEnquiryType());
            if(!TextUtils.isEmpty(itemCreation.getTransactionType())) {
                holder.Transaction_Type.setText("Transaction Type : " + itemCreation.getTransactionType());
            }
            else
                holder.Transaction_Type.setVisibility(View.GONE);
            System.out.println("HHHHHHHHHHH"+itemCreation.getEnquiryType());
            if(itemCreation.getEnquiryType().equals("Group Enquiry"))
            {
                holder.TileRelative.setBackgroundColor(Color.parseColor("#c4ede2"));
                //holder.EnquiryType.setTextColor(0xFF00FF00);
            }
            if(itemCreation.getEnquiryType().equals("Advertisement Enquiry")){
                holder.TileRelative.setBackgroundColor(Color.parseColor("#cddcfe"));
            }
        }
        if(itemCreation.getIsFavorite() == 1){
            mSelectedItemStar.put(position,true);
/*
            holder.imgStar.setImageResource(R.drawable.full_star);
            holder.imgStar.setColorFilter(
                    context.getResources().getColor(R.color.red),
                    android.graphics.PorterDuff.Mode.MULTIPLY
            );
*/

            Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.full_star);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
            holder.imgStar.setImageDrawable(wrappedDrawable);
        }
//        else if(itemCreation){
//            mSelectedItemStar.put(position,false);
//            holder.imgStar.setImageResource(R.drawable.star);
//        }
        else {
            mSelectedItemStar.put(position,false);
            Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.star);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
            holder.imgStar.setImageDrawable(wrappedDrawable);
/*
            holder.imgStar.setImageResource(R.drawable.icon_star_unselected);
            holder.imgStar.setColorFilter(
                    context.getResources().getColor(R.color.red),
                    android.graphics.PorterDuff.Mode.MULTIPLY
            );
*/
        }

        if (itemCreation.getIsSentEnquiry()==1)
        {
            holder.EnqTypeimg.setImageResource(R.drawable.uparrow);
        }
        else
        {
            holder.EnqTypeimg.setImageResource(R.drawable.downarrow);
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
                    /*holder.imgStar.setImageResource(R.drawable.full_star);
                    holder.imgStar.setColorFilter(
                            context.getResources().getColor(R.color.red),
                            android.graphics.PorterDuff.Mode.MULTIPLY
                    );*/
                    clickEvent.starclick(dealerRegisterList.get(position).getQueryId(),dealerRegisterList.get(position).getCustID(),1);
                }else {
                    mSelectedItemStar.put(position,false);
                    Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.star);
                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                    wrappedDrawable.setTint(ContextCompat.getColor(context, R.color.red));
                    holder.imgStar.setImageDrawable(wrappedDrawable);
                    /*holder.imgStar.setImageResource(R.drawable.star);
                    holder.imgStar.setColorFilter(
                            context.getResources().getColor(R.color.red),
                            android.graphics.PorterDuff.Mode.MULTIPLY
                    );*/
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

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirmName().toLowerCase().contains(charString.toLowerCase()) || row.getEnquiryType().toLowerCase().contains(charString.toLowerCase())|| row.getChildCategoryName().toLowerCase().contains(charString.toLowerCase())) {
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
        TextView itemName, itemQuantity,date,itemChildName,cityname,EnquiryDate,EnquiryType,Transaction_Type;
        public  TextView textNotify;

        RelativeLayout TileRelative;

        String base64String;
        //ImageView imageView;
        ImageView imgStar,EnqTypeimg,PrifileImg;
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
            Transaction_Type=(TextView) itemView.findViewById(R.id.trans_type);
            date = (TextView)itemView.findViewById(R.id.chat_date_txt);
            textNotify = itemView.findViewById(R.id.textNotify);
            imgStar = itemView.findViewById(R.id.star);
            EnqTypeimg = itemView.findViewById(R.id.type);
            TileRelative = (RelativeLayout) itemView.findViewById(R.id.fav_relative);
            PrifileImg = (ImageView) itemView.findViewById(R.id.icon);
            if(null == PrifileImg) {
            }
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
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(InboxDealer item, int position) {
        dealerRegisters.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
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


    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    public void selectAll() {

        for (int i = 0; i < getItemCount(); i++)
         mSelectedItemsIds.put(i, true);
        notifyDataSetChanged();

    }


    public int getSelectedItemCount() {
        return mSelectedItemsIds.size();
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

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
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




    public List<InboxDealer> getUpdatedList()
    {
        return dealerRegisterList;
    }

}
