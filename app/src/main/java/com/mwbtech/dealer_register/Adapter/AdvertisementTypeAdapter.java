package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mwbtech.dealer_register.PojoClass.AdvertisementTypeModel;
import com.mwbtech.dealer_register.R;

import java.util.List;


public class AdvertisementTypeAdapter extends BaseAdapter {

    Activity activity;
    List<AdvertisementTypeModel> adTypeModels;
    AdTypeClick typeClick;

    private int selectedPosition = -1;
    private String adTypeName;
    private int adTypeId;

    public interface AdTypeClick {
        void TypeOfAdClick(int pos, List<AdvertisementTypeModel> types);
    }

    public AdvertisementTypeAdapter(Activity activity, List<AdvertisementTypeModel> areaModels, AdTypeClick adTypeClick) {
        this.activity = activity;
        this.adTypeModels = areaModels;
        this.typeClick = adTypeClick;
    }

    @Override
    public int getCount() {
        return adTypeModels.size();
    }

    @Override
    public Object getItem(int position) {
        return adTypeModels.get(position);
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
        if(adTypeModels.get(position).isChecked()){
            holder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
            Glide.with(activity)
                    .load(adTypeModels.get(position).getSelectedImage())
                    .into(holder.imgBusiness);
            holder.tvItemName.setTextColor(activity.getResources().getColor(R.color.white));
            holder.tvItemName.setTypeface(null, Typeface.BOLD);
//            holder.checkBox.setChecked(true);
        } else {
            holder.root.setBackgroundResource(0);
            Glide.with(activity)
                    .load(adTypeModels.get(position).getUnselectedImage())
                    .into(holder.imgBusiness);
//            holder.checkBox.setChecked(false);
            holder.tvItemName.setTextColor(activity.getResources().getColor(R.color.black));
            holder.tvItemName.setTypeface(null, Typeface.NORMAL);
        }

        holder.tvItemName.setTag(position); // This line is important.

        holder.tvItemName.setText(adTypeModels.get(position).getDescription());
//        holder.checkBox.setText(adTypeModels.get(position).getDescription());
//        holder.checkBox.setChecked(position == selectedPosition);
//        holder.checkBox.setTypeface(null, Typeface.NORMAL);
//        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        holder.root.setOnClickListener(v -> {
            if (adTypeModels.get(position).isChecked()) {
                selectedPosition = position;
                holder.root.setBackgroundResource(0);
                holder.tvItemName.setTextColor(activity.getResources().getColor(R.color.black));
                holder.tvItemName.setTypeface(null, Typeface.NORMAL);
                adTypeModels.get(position).setChecked(false);
                typeClick.TypeOfAdClick(position,adTypeModels);
                adTypeName = adTypeModels.get(position).getAdvertisementType();
                adTypeId = adTypeModels.get(position).getAdTypeId();
            } else {
                holder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
                holder.tvItemName.setTextColor(activity.getResources().getColor(R.color.white));
                holder.tvItemName.setTypeface(null, Typeface.BOLD);
//                holder.checkBox.setTextColor(activity.getResources().getColor(R.color.white));
//                holder.checkBox.setTypeface(null, Typeface.NORMAL);
                adTypeModels.get(position).setChecked(true);
                typeClick.TypeOfAdClick(position,adTypeModels);
                adTypeId=0;
                selectedPosition = -1;
            }
            for (int i = 0; i < adTypeModels.size(); i++) {
                adTypeModels.get(i).setChecked(i == position);
            }
            notifyDataSetChanged();
        });

        return convertView;
    }

    public View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                    adTypeModels.get(position).setChecked(checkBox.isChecked());
                    typeClick.TypeOfAdClick(position,adTypeModels);
                    adTypeName = adTypeModels.get(position).getAdvertisementType();
                    adTypeId = adTypeModels.get(position).getAdTypeId();
                    checkBox.setTypeface(null, Typeface.BOLD);
                } else {
                    adTypeModels.get(position).setChecked(checkBox.isChecked());
                    typeClick.TypeOfAdClick(position,adTypeModels);
                    adTypeId=0;
                    selectedPosition = -1;
                    checkBox.setTypeface(null, Typeface.NORMAL);
                }
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder {
        public TextView tvItemName;
        public ImageView imgBusiness;
        public LinearLayout root;
        public CheckBox checkBox;
        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.ckId);
            tvItemName=(TextView) v.findViewById(R.id.item_name);
            imgBusiness=(ImageView) v.findViewById(R.id.img_business);
            root=(LinearLayout) v.findViewById(R.id.root);
        }
    }

}