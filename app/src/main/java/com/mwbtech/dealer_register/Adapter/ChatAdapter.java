package com.mwbtech.dealer_register.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.mwbtech.dealer_register.PojoClass.Messages;
import com.mwbtech.dealer_register.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter  {
    ArrayList<Messages> msg_models;
    private final Context context;
    String date,time;
    Dialog mDialogCity;
    PhotoView imageView;
    private SparseBooleanArray mSelectedItemsIds;
    String Enq;

    public ChatAdapter(ArrayList<Messages> msg_models,Context context){
        this.msg_models=msg_models;
        this.context=context;
        mSelectedItemsIds = new SparseBooleanArray();

    }

    public ChatAdapter(ArrayList<Messages> msg_models,Context context,String req){
        this.msg_models=msg_models;
        this.context=context;
        this.Enq = req;
        mSelectedItemsIds = new SparseBooleanArray();

    }


    public static class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        LinearLayout imgLayout,txtLayout,imgLayout1,ipdfLayout;
        ImageView imageView,imageView2,ipdfView;
        TextView tv_input_date_time,tv_input_date_time1,tv_input_date_time2,tv_input_date_time3,tvFileName;

        public SenderViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.background);
            this.txtType = (TextView) itemView.findViewById(R.id.tv_input_text);
            this.imgLayout = (LinearLayout) itemView.findViewById(R.id.imageLayout);
            this.imgLayout1 = (LinearLayout) itemView.findViewById(R.id.imageLayout1);
            this.imageView2 = (ImageView) itemView.findViewById(R.id.background1);
            this.txtLayout = (LinearLayout) itemView.findViewById(R.id.txtLayout);
            this.tv_input_date_time = (TextView)itemView.findViewById(R.id.tv_input_date_time);
            this.tv_input_date_time1=(TextView)itemView.findViewById(R.id.tv_input_date_time1);
            this.tv_input_date_time2=(TextView)itemView.findViewById(R.id.tv_input_date_time2);
            this.tv_input_date_time3=(TextView)itemView.findViewById(R.id.tv_input_date_time3);
            this.tvFileName=(TextView)itemView.findViewById(R.id.tvFileName);
            this.ipdfLayout=(LinearLayout) itemView.findViewById(R.id.ic_pdfLayout);
            ipdfView=(ImageView) itemView.findViewById(R.id.ipdfview);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        LinearLayout imgLayout,txtLayout,imgLayout2,rpdfLayout;
        ImageView imageView,imageView2,rpdfView;
        TextView rcv_tv_input_date_time,rcv_tv_input_date_time1,rcv_tv_input_date_time2,rcv_tv_input_date_time3,tvFileName;

        public ReceiverViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.tv_response_text);
            this.imgLayout = (LinearLayout) itemView.findViewById(R.id.rcv_imageLayout);
            this.imgLayout2 = (LinearLayout) itemView.findViewById(R.id.rcv_imageLayout1);
            this.txtLayout = (LinearLayout) itemView.findViewById(R.id.rcv_txtLayout1);
            this.imageView = (ImageView) itemView.findViewById(R.id.rcv_background);
            this.imageView2 = (ImageView) itemView.findViewById(R.id.rcv_background1);
            this.rcv_tv_input_date_time = (TextView)itemView.findViewById(R.id.tv_response_date_time);
            this.rcv_tv_input_date_time1 = (TextView)itemView.findViewById(R.id.rcv_tv_input_date_time1);
            this.rcv_tv_input_date_time2 = (TextView)itemView.findViewById(R.id.rcv_tv_input_date_time2);
            this.rcv_tv_input_date_time3 = (TextView)itemView.findViewById(R.id.tv_input_date_time3);
            this.tvFileName = (TextView)itemView.findViewById(R.id.tvFileName);
            this.rpdfLayout=(LinearLayout)itemView.findViewById(R.id.rc_pdfLayout);
            this.rpdfView=(ImageView) itemView.findViewById(R.id.rpdfview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case Messages.SENDER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_input_layout, parent, false);
                return new SenderViewHolder(view);
            case Messages.RECV_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_response_layout, parent, false);
                return new ReceiverViewHolder(view);

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
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat1.format(mydate);
        return date;

    }

    @Override
    public int getItemViewType(int position) {

        switch (msg_models.get(position).type) {
            case 0:
                return Messages.SENDER_TYPE;
            case 1:
                return Messages.RECV_TYPE;
            default:
                return -1;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Messages object = msg_models.get(listPosition);

        if (object != null) {

            switch (object.type) {
                case Messages.SENDER_TYPE:

                    ((SenderViewHolder) holder).itemView.setBackgroundColor(mSelectedItemsIds.get(listPosition) ? 0x9934B5E4
                            : Color.TRANSPARENT);
                    date = ConvertdateFormat(object.getCreatedDate().substring(0,10));
                    time  = object.getCreatedDate().substring(11,16);

                    if(!TextUtils.isEmpty(object.getFileType()))
                    {
                        if (object.getFileType().equals("image"))
                        {
                            if(!TextUtils.isEmpty(object.getImage()) &&
                                    TextUtils.isEmpty(object.getMessage()))
                            {
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).ipdfLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).imgLayout.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(object.getImage())
                                        .into(((SenderViewHolder) holder).imageView);
                                ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tv_input_date_time1.setText(""+date+" "+time);
                                ((SenderViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertMethod(object.getImage());
                                    }
                                });
                            }else if(!TextUtils.isEmpty(object.getImage()) &&
                                    TextUtils.isEmpty(object.getImage2()) &&
                                    !TextUtils.isEmpty(object.getMessage()))
                            {
                                ((SenderViewHolder) holder).ipdfLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                ((SenderViewHolder) holder).imgLayout.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(object.getImage())
                                        .into(((SenderViewHolder) holder).imageView);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((SenderViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tv_input_date_time1.setText(""+date+" "+time);
                                ((SenderViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        alertMethod(object.getImage());
                                    }
                                });
                            }else if(!TextUtils.isEmpty(object.getImage()) &&
                                    !TextUtils.isEmpty(object.getImage2()) &&
                                    !TextUtils.isEmpty(object.getMessage()))
                            {
                                ((SenderViewHolder) holder).ipdfLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                ((SenderViewHolder) holder).imgLayout.setVisibility(View.VISIBLE);
                                ((SenderViewHolder) holder).imgLayout1.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(object.getImage())
                                        .into(((SenderViewHolder) holder).imageView);
                                Picasso.get()
                                        .load(object.getImage2())
                                        .into(((SenderViewHolder) holder).imageView2);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((SenderViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tv_input_date_time1.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tv_input_date_time2.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertMethod(object.getImage());
                                    }
                                });

                                ((SenderViewHolder) holder).imageView2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertMethod(object.getImage2());
                                    }
                                });
                            }else {
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((SenderViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                            }
                        }else if (object.getFileType().equals("pdf")){

                            if(!TextUtils.isEmpty(object.getImage()) &&
                                    TextUtils.isEmpty(object.getMessage()))
                            {
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).imgLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).imgLayout1.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).ipdfLayout.setVisibility(View.VISIBLE);
                                ((SenderViewHolder) holder).tv_input_date_time3.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tvFileName.setText(""+object.getFileName());
                                ((SenderViewHolder) holder).ipdfLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                        pdfIntent.setDataAndType(Uri.parse(object.getImage()), "application/pdf");
                                        context.startActivity(pdfIntent);
                                    }
                                });
                            }else if (!TextUtils.isEmpty(object.getImage()) &&
                                    !TextUtils.isEmpty(object.getMessage())){

                                ((SenderViewHolder) holder).ipdfLayout.setVisibility(View.VISIBLE);
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                ((SenderViewHolder) holder).imgLayout.setVisibility(View.GONE);
                                ((SenderViewHolder) holder).imgLayout1.setVisibility(View.GONE);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((SenderViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tv_input_date_time3.setText(""+date+" "+time);
                                ((SenderViewHolder) holder).tvFileName.setText(""+object.getFileName());
                                ((SenderViewHolder)holder).ipdfLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                        pdfIntent.setDataAndType(Uri.parse(object.getImage()), "application/pdf");
                                        context.startActivity(pdfIntent);
                                    }
                                });
                            }else {
                                ((SenderViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((SenderViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                            }

                        }

                    }else {
                        ((SenderViewHolder) holder).ipdfLayout.setVisibility(View.GONE);
                        ((SenderViewHolder) holder).imgLayout.setVisibility(View.GONE);
                        ((SenderViewHolder) holder).imgLayout1.setVisibility(View.GONE);
                        ((SenderViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                        //convert emoji to text
                        String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                        ((SenderViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                        ((SenderViewHolder) holder).tv_input_date_time.setText(""+date+" "+time);
                    }

                    break;
                case Messages.RECV_TYPE:

                    ((ReceiverViewHolder) holder).itemView.setBackgroundColor(mSelectedItemsIds.get(listPosition) ? 0x9934B5E4
                            : Color.TRANSPARENT);


                    date = ConvertdateFormat(object.getCreatedDate().substring(0,10));
                    time  = object.getCreatedDate().substring(11,16);
                    if(!TextUtils.isEmpty(object.getFileType()))
                    {
                        // if file type not null
                        if (object.getFileType().equals("image"))
                        {
                            if(!TextUtils.isEmpty(object.getImage()) &&
                                    TextUtils.isEmpty(object.getMessage()))
                            {
                                //display image only
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).imgLayout.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(object.getImage())
                                        .into(((ReceiverViewHolder) holder).imageView);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time1.setText(""+date+" "+time);

                                ((ReceiverViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        alertMethod(object.getImage());
                                    }
                                });

                            }else if(!TextUtils.isEmpty(object.getImage()) &&
                                    TextUtils.isEmpty(object.getImage2()) &&
                                    !TextUtils.isEmpty(object.getMessage()))
                            {
                                ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                ((ReceiverViewHolder) holder).imgLayout.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(object.getImage())
                                        .into(((ReceiverViewHolder) holder).imageView);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((ReceiverViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time1.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time2.setText(""+date+" "+time);
                                ((ReceiverViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        alertMethod(object.getImage());
                                    }
                                });
                            }else if(!TextUtils.isEmpty(object.getImage()) &&
                                    !TextUtils.isEmpty(object.getImage2()) &&
                                    !TextUtils.isEmpty(object.getMessage()))
                            {
                                ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                ((ReceiverViewHolder) holder).imgLayout.setVisibility(View.VISIBLE);
                                ((ReceiverViewHolder) holder).imgLayout2.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(object.getImage())
                                        .into(((ReceiverViewHolder) holder).imageView);
                                Picasso.get()
                                        .load(object.getImage2())
                                        .into(((ReceiverViewHolder) holder).imageView2);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((ReceiverViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time1.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time2.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertMethod(object.getImage());
                                    }
                                });

                                ((ReceiverViewHolder) holder).imageView2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertMethod(object.getImage2());
                                    }
                                });
                            }else {
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                //convert emoji to text
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((ReceiverViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time.setText(""+date+" "+time);

                            }
                        }else if (object.getFileType().equals("pdf")){

                            if(!TextUtils.isEmpty(object.getImage()) &&
                                    TextUtils.isEmpty(object.getMessage()))
                            {
                                //display pdf layout
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).imgLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).imgLayout2.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.VISIBLE);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time3.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).tvFileName.setText(""+object.getFileName());
                                ((ReceiverViewHolder) holder).rpdfLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                        pdfIntent.setDataAndType(Uri.parse(object.getImage()), "application/pdf");
                                        context.startActivity(pdfIntent);
                                    }
                                });
                            }else if (!TextUtils.isEmpty(object.getImage()) &&
                                    !TextUtils.isEmpty(object.getMessage())){

                                ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.VISIBLE);
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                ((ReceiverViewHolder) holder).imgLayout.setVisibility(View.GONE);
                                ((ReceiverViewHolder) holder).imgLayout2.setVisibility(View.GONE);

                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((ReceiverViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time3.setText(""+date+" "+time);
                                ((ReceiverViewHolder) holder).tvFileName.setText(""+object.getFileName());
                                ((ReceiverViewHolder)holder).rpdfLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                        pdfIntent.setDataAndType(Uri.parse(object.getImage()), "application/pdf");
                                        context.startActivity(pdfIntent);
                                    }
                                });
                            }else {
                                ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                                ((ReceiverViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                                ((ReceiverViewHolder) holder).rcv_tv_input_date_time.setText(""+date+" "+time);
                            }

                        }

                    }else {
                        ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.GONE);
                        ((ReceiverViewHolder) holder).imgLayout.setVisibility(View.GONE);
                        ((ReceiverViewHolder) holder).rpdfLayout.setVisibility(View.GONE);
                        ((ReceiverViewHolder) holder).txtLayout.setVisibility(View.VISIBLE);
                        String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(object.getMessage());
                        ((ReceiverViewHolder) holder).txtType.setText(fromServerUnicodeDecoded);
                        ((ReceiverViewHolder) holder).rcv_tv_input_date_time.setText(""+date+" "+time);
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return msg_models == null ? 0 :msg_models.size();
    }

    public void alertMethod(String image){
        mDialogCity = new Dialog(context);
        mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogCity.setContentView(R.layout.zoom_layout);
        mDialogCity.setCancelable(true);
        imageView = (PhotoView) mDialogCity.findViewById(R.id.photo_view);
        mDialogCity.show();
        try {
            Picasso.get().load(image).into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            if (position==0)
            {
                Toast.makeText(context, "You are not allowed to delete enquiry.", Toast.LENGTH_SHORT).show();
                mSelectedItemsIds.delete(position);

            }else
            {
                mSelectedItemsIds.put(position, value);
            }

        else
            mSelectedItemsIds.delete(position);
        notifyItemChanged(position);
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++)
        {
            mSelectedItemsIds.delete(0);
            mSelectedItemsIds.put(i, true);
        }
        notifyDataSetChanged();

    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
        msg_models.remove(position);
        notifyItemRemoved(position);
    }

    public int getSelectedItemCount() {
        return mSelectedItemsIds.size();
    }
}
