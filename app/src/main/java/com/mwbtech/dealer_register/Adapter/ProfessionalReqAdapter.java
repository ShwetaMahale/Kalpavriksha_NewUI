package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mwbtech.dealer_register.PojoClass.ProfessionalReqModel;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalReqAdapter extends BaseAdapter {

    Activity activity;
    List<ProfessionalReqModel> FDlist;
    private int selectedPosition = -1;
    List<ProfessionalReqModel> businessDemandslist = new ArrayList<>();
    int ProfBusinessDemandID = 0;
    String ProfBusinessDemandName;
    ViewHolder viewHolder;
    ProfessionalTypeClick click;

    public ProfessionalReqAdapter(Activity activity, List<ProfessionalReqModel> fdlist1s, ProfessionalTypeClick click) {
        this.activity = activity;
        this.FDlist = fdlist1s;
        this.click = click;
    }

    @Override
    public int getCount() {
        return FDlist.size();
    }

    @Override
    public Object getItem(int position) {
        return FDlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.business_adapter, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.checkBox.setTag(position); // This line is important.
        holder.friendName.setTag(position); // This line is important.
        holder.friendName.setText(FDlist.get(position).getRequirementName());

//        holder.checkBox.setText(FDlist.get(position).getRequirementName());
        if (FDlist.get(position).isChecked()) {
            holder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
            Glide.with(activity)
                    .load(FDlist.get(position).getSelectedImage())
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .disallowHardwareConfig()
                    .into(holder.imgBusiness);
            /*Picasso.get()
                    .load(businessTypes.get(position).getSelectedImage())
                    .into(viewHolder.imgBusiness);*/
            holder.friendName.setTextColor(activity.getResources().getColor(R.color.white));
            holder.friendName.setTypeface(null, Typeface.BOLD);
        } else {
            holder.root.setBackgroundResource(0);
            Glide.with(activity)
                    .load(FDlist.get(position).getUnSelectedImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(holder.imgBusiness);
            /*Picasso.get()
                    .load(businessTypes.get(position).getSelectedImage())
                    .into(viewHolder.imgBusiness);*/
            holder.friendName.setTextColor(activity.getResources().getColor(R.color.black));
            holder.friendName.setTypeface(null, Typeface.NORMAL);
        }
//        Glide.with(activity)
//                .load( FDlist.get(position).isChecked()? FDlist.get(position).getSelectedImage():FDlist.get(position).getUnSelectedImage())
//                .into(holder.imgBusiness);
//        if (position == selectedPosition) {
//            holder.checkBox.setChecked(true);
//        } else holder.checkBox.setChecked(false);

        //holder.checkBox.setChecked(FDlist.get(position).isChecked());
        //RK holder.checkBox.setTag(FDlist.get(position));
        //RK holder.checkBox = (CheckBox)convertView.findViewById(R.id.ckId);
        //holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        //important
//        holder.checkBox.setVisibility(View.GONE);
//        holder.friendName.setVisibility(View.VISIBLE);
//        holder.checkBox.setChecked(FDlist.get(position).isChecked());
//        holder.checkBox.setTag(FDlist.get(position));
        holder.root.setOnClickListener(v -> {

            if (!FDlist.get(position).isChecked()) {
                selectedPosition = position;
                ProfBusinessDemandName = FDlist.get(position).getRequirementName();
                ProfBusinessDemandID = FDlist.get(position).getProfessionalRequirementID();
                businessDemandslist.add(new ProfessionalReqModel(ProfBusinessDemandName, ProfBusinessDemandID));
                Log.e("Business", "ID....." + ProfBusinessDemandID);

            } else {
                ProfBusinessDemandID = 0;
                selectedPosition = -1;
            }

//            CheckBox cb = (CheckBox) v;
//            ProfessionalReqModel contact = (ProfessionalReqModel) cb.getTag();
//            contact.setChecked(cb.isChecked());
//            FDlist.get(position).setChecked(FDlist.get(position).isChecked());

            for (int i = 0; i < FDlist.size(); i++) {
                FDlist.get(i).setChecked(i == position);
            }
            click.TypeOfProfClick(position, FDlist);
//                if(contact.getRequirementName().equals("Admission")) {
//                    if (cb.isChecked()) {
//                        for (int i=0; i < FDlist.size(); i++) {
//                            if (FDlist.get(i).getRequirementName().equals("Admission")) {
//                                FDlist.get(i).setChecked(true);
//                            } else {
//                                FDlist.get(i).setChecked(false);
//                            }
//                        }
//
//                    }
//                } else {
           /* if (FDlist.get(position).isChecked()) {
                for (int i = 0; i < FDlist.size(); i++) {
                    //if (FDlist.get(i).getRequirementName().equals("Admission")) {
//                            if(FDlist.get(i).isChecked()){
//                                FDlist.get(i).setChecked(false);
//                            }

                    if (FDlist.get(i).getProfessionalRequirementID() == FDlist.get(position).getProfessionalRequirementID()) {
                        FDlist.get(i).setChecked(FDlist.get(i).isChecked());
                        ProfBusinessDemandID = FDlist.get(position).getProfessionalRequirementID();
                        if (FDlist.get(i).isChecked()) {
                            Log.d("Sonu", "onClick: ");
                            Glide.with(activity)
                                    .load(FDlist.get(i).getSelectedImage())
                                    .into(holder.imgBusiness);
                        }
                    } else {
                        FDlist.get(i).setChecked(false);
                        Glide.with(activity)
                                .load(FDlist.get(i).getUnSelectedImage())
                                .into(holder.imgBusiness);
                    }

                    //}

                }
            }*/
            //}
            notifyDataSetChanged();
//                ////////
//
        });

//        if (position == selectedPosition) {
//            viewHolder.checkBox.setChecked(true);
//        } else viewHolder.checkBox.setChecked(false);
//
//        viewHolder.checkBox.setOnClickListener(onStateChangedListener(viewHolder.checkBox, position));

        return convertView;
    }

    public View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {

                    selectedPosition = position;
                    ProfBusinessDemandName = FDlist.get(position).getRequirementName();
                    ProfBusinessDemandID = FDlist.get(position).getProfessionalRequirementID();
                    businessDemandslist.add(new ProfessionalReqModel(ProfBusinessDemandName, ProfBusinessDemandID));
                    Log.e("Business", "ID....." + ProfBusinessDemandID);

                } else {
                    ProfBusinessDemandID = 0;
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {
        private final TextView friendName;
        private final LinearLayout root;
        private final ImageView imgBusiness;
        private final CheckBox checkBox;

        public ViewHolder(View v) {
            root = (LinearLayout) v.findViewById(R.id.root);
            checkBox = (CheckBox) v.findViewById(R.id.ckId);
            friendName = (TextView) v.findViewById(R.id.item_name);
            imgBusiness = (ImageView) v.findViewById(R.id.img_business);
        }
    }

    public int GetselectedProfBusinessDemand() {
        Log.e("selected", "prof ID...." + ProfBusinessDemandID);
        return ProfBusinessDemandID;
    }

    public interface ProfessionalTypeClick {
        void TypeOfProfClick(int pos, List<ProfessionalReqModel> types);
    }
}
