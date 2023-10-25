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
import com.mwbtech.dealer_register.PojoClass.AdvertisementAreaModel;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;


public class AdvertisementAreaAdapter extends BaseAdapter {

    Activity activity;
    List<AdvertisementAreaModel> areaModels;
    List<AdvertisementAreaModel> areaModelsList;
    AdAreaClick areaClick;
    private int selectedPosition = -1;
    private String adAreaName;
    private int adAreaId;


    public interface AdAreaClick {
        void TypeOfAreaClick(int pos, List<AdvertisementAreaModel> types);
    }

    public AdvertisementAreaAdapter(Activity activity, List<AdvertisementAreaModel> areaModels, AdAreaClick adAreaClick) {
        this.activity = activity;
        this.areaModels = areaModels;
        this.areaClick = adAreaClick;
        this.areaModelsList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return areaModels.size();
    }

    @Override
    public Object getItem(int position) {
        return areaModels.get(position);
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

        holder.checkBox.setVisibility(View.GONE);
        holder.imgBusiness.setVisibility(View.VISIBLE);
        holder.tvItemName.setVisibility(View.VISIBLE);
//        holder.checkBox.setChecked(position == selectedPosition);
//        holder.checkBox.setTypeface(null, Typeface.NORMAL);
//        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        if(areaModels.get(position).isChecked()){
            holder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
            Glide.with(activity)
                    .load(areaModels.get(position).getSelectedImage())
                    .into(holder.imgBusiness);
            holder.tvItemName.setTextColor(activity.getResources().getColor(R.color.white));
            holder.tvItemName.setTypeface(null, Typeface.BOLD);
        } else {
            holder.root.setBackgroundResource(0);
            Glide.with(activity)
                    .load(areaModels.get(position).getUnselectedImage())
                    .into(holder.imgBusiness);
            holder.tvItemName.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tvItemName.setTypeface(null, Typeface.NORMAL);
        }

        holder.tvItemName.setTag(position); // This line is important.
        holder.tvItemName.setText(areaModels.get(position).getAdvertisementAreaName());

        holder.root.setOnClickListener(v -> {
            if (areaModels.get(position).isChecked()) {
                selectedPosition = position;
                areaModels.get(position).setChecked(false);
                areaClick.TypeOfAreaClick(position,areaModels);
                adAreaName = areaModels.get(position).getAdvertisementAreaName();
                adAreaId = areaModels.get(position).getAdvertisementAreaID();
                // areaModels.add(new AdvertisementAreaModel(adAreaId,adAreaName));
                Log.e("Business","ID....."+adAreaId);
            } else {
                areaModels.get(position).setChecked(true);
                areaClick.TypeOfAreaClick(position,areaModels);
                adAreaId=0;
                selectedPosition = -1;
            }
            for (int i = 0; i < areaModels.size(); i++) {
                areaModels.get(i).setChecked(i == position);
            }
            notifyDataSetChanged();
        });

        convertView.setTag(R.id.textView, holder.tvItemName);
        convertView.setTag(holder);
        return convertView;
    }

    /*public View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                    areaModels.get(position).setChecked(checkBox.isChecked());
                    areaClick.TypeOfAreaClick(position,areaModels);
                    adAreaName = areaModels.get(position).getAdvertisementAreaName();
                    adAreaId = areaModels.get(position).getAdvertisementAreaID();
                    checkBox.setTypeface(null, Typeface.BOLD);
                   // areaModels.add(new AdvertisementAreaModel(adAreaId,adAreaName));
                    Log.e("Business","ID....."+adAreaId);
                } else {
                    areaModels.get(position).setChecked(checkBox.isChecked());
                    areaClick.TypeOfAreaClick(position,areaModels);
                    adAreaId=0;
                    selectedPosition = -1;
                    checkBox.setTypeface(null, Typeface.NORMAL);
                }
                notifyDataSetChanged();
            }
        };
    }*/



    private static class ViewHolder {
        public ImageView imgBusiness;
        public TextView tvItemName;
        public CheckBox checkBox;
        public LinearLayout root;

        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.ckId);
            imgBusiness=(ImageView) v.findViewById(R.id.img_business);
            tvItemName=(TextView) v.findViewById(R.id.item_name);
            root = v.findViewById(R.id.root);
        }
    }


    public List<AdvertisementAreaModel> getTrueResultList()
    {
        areaModelsList.clear();
        for(int i = 0; i< areaModels.size(); i++){
            if(areaModels.get(i).isChecked() == true) {
                areaModelsList.add(new AdvertisementAreaModel(areaModels.get(i).getAdvertisementAreaID(),areaModels.get(i).getAdvertisementAreaName()));
            }
        }
        return areaModelsList;
    }
}
