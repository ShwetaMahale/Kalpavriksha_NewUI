package com.mwbtech.dealer_register.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mwbtech.dealer_register.PojoClass.Notification;
import com.mwbtech.dealer_register.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {

    private final Context context;
    private final List<Notification> listitems;
    public static AdClickEvent adClickEvent;
    Dialog mDialogCity;
    PhotoView ivimage;

    public  interface AdClickEvent {
        void adclick(Notification notification);

        void deleteNotification(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> listitems, AdClickEvent event) {
        this.context = context;
        this.listitems = listitems;
        adClickEvent = event;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        if (listitems == null || listitems.isEmpty()) return;
            Notification list=listitems.get(position);
            holder.tvNotification.setText(list.getPushNotification());
            holder.tvSenderName.setText(list.getTitle());
            holder.tvSenderStatus.setText(list.getNotificationType());
            holder.tvdate.setText(list.getNotificationDateStr());
            if(list.getNotificationType()!=null){
                if(list.getNotificationType().equals("New Enquiry")){
                    holder.itemView.setClickable(true);
                    if(list.getCustomerDetailsQuestionDTO()!=null && !TextUtils.isEmpty(list.getCustomerDetailsQuestionDTO().getUserImage())){
                        Glide.with(context).load(list.getCustomerDetailsQuestionDTO().getUserImage()).into(holder.ivicon);
                        String imgurl=list.getCustomerDetailsQuestionDTO().getUserImage();
                        holder.ivicon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertMethod(list.getCustomerDetailsQuestionDTO().getUserImage());
                            }
                        });
                    }
                    else{
                        Glide.with(context).load(R.drawable.profile).into(holder.ivicon);
                    }

                }
                else if(list.getNotificationType().equals("General Enquiry")){
                    holder.itemView.setClickable(true);
                    if(!TextUtils.isEmpty(list.getCustomerDetailsQuestionDTO().getUserImage())){
                        Glide.with(context).load(list.getCustomerDetailsQuestionDTO().getUserImage()).into(holder.ivicon);
                        String imgurl=list.getCustomerDetailsQuestionDTO().getUserImage();
                        holder.ivicon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertMethod(list.getCustomerDetailsQuestionDTO().getUserImage());
                            }
                        });
                    }
                    else{
                        Glide.with(context).load(R.drawable.profile).into(holder.ivicon);
                    }
                }
                else if(list.getNotificationType().equals("Group Enquiry")){
                    holder.itemView.setClickable(true);
                    if(list.getCustomerDetailsQuestionDTO() != null && !TextUtils.isEmpty(list.getCustomerDetailsQuestionDTO().getUserImage())){
                        Glide.with(context).load(list.getCustomerDetailsQuestionDTO().getUserImage()).into(holder.ivicon);
                        String imgurl=list.getCustomerDetailsQuestionDTO().getUserImage();
                        holder.ivicon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertMethod(list.getCustomerDetailsQuestionDTO().getUserImage());
                            }
                        });
                    }else{
                        Glide.with(context).load(R.drawable.profile).into(holder.ivicon);
                    }
                }
                else if(list.getNotificationType().equals("Advertisement Enquiry")){
                    holder.itemView.setClickable(true);
                    if(list.getCustomerDetailsQuestionDTO().getUserImage() != null && !TextUtils.isEmpty(list.getCustomerDetailsQuestionDTO().getUserImage())){
                        Glide.with(context).load(list.getCustomerDetailsQuestionDTO().getUserImage()).into(holder.ivicon);
                        //String imgurl=list.getCustomerDetailsQuestionDTO().getUserImage();
                        holder.ivicon.setOnClickListener(v -> alertMethod(list.getCustomerDetailsQuestionDTO().getUserImage()));
                    }
                    else{
                        Glide.with(context).load(R.drawable.profile).into(holder.ivicon);
                    }
                }
                else{
                    holder.itemView.setClickable(false);
                    Glide.with(context).load(R.drawable.login).into(holder.ivicon);
                    if(list.getImageURL() != null && !TextUtils.isEmpty(list.getImageURL())){
                        holder.ivimage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(list.getImageURL()).into(holder.ivimage);
                        holder.cardView.setOnClickListener(view -> alertMethod(list.getImageURL()));
                    }
                    else{
                        holder.ivimage.setVisibility(View.GONE);
                        holder.cardView.setClickable(false);
                    }

                }
            }
            else{
                Glide.with(context).load(R.drawable.login).into(holder.ivicon);
                if(list.getImageURL() != null && !TextUtils.isEmpty(list.getImageURL())){
                    Log.e("imageurl..",list.getImageURL());
                    holder.itemView.setClickable(false);
                    holder.ivimage.setVisibility(View.VISIBLE);
                    Glide.with(context).load(list.getImageURL()).into(holder.ivimage);
                    holder.cardView.setOnClickListener(view -> alertMethod(list.getImageURL()));
                }
                else{
                    holder.itemView.setClickable(true);
                    holder.ivimage.setVisibility(View.GONE);
                }
            }
    }

    @Override
    public int getItemCount() {
        return listitems.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNotification,tvSenderStatus,tvdate,tvSenderName;
        public ImageView ivimage, delete;
        CircleImageView ivicon;
        public LinearLayout cardView;
        public ViewHolder(View itemView){
            super(itemView);
            tvNotification=(TextView) itemView.findViewById(R.id.txt_notification);
            tvdate=(TextView) itemView.findViewById(R.id.txt_notification_time);
            tvSenderStatus=(TextView) itemView.findViewById(R.id.txt_sender_status);
            tvSenderName=(TextView) itemView.findViewById(R.id.txt_sender_name);
            ivicon=itemView.findViewById(R.id.img_notification_icon);
            ivimage=(ImageView)itemView.findViewById(R.id.imageView);
            cardView=(LinearLayout) itemView.findViewById(R.id.cardview);
            delete = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(view -> adClickEvent.adclick(listitems.get(getAdapterPosition())));
            delete.setOnClickListener(v -> adClickEvent.deleteNotification(listitems.get(getAdapterPosition())));
        }
    }
}
