package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mwbtech.dealer_register.PojoClass.AdvertisementSlotModel;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends ArrayAdapter<AdvertisementSlotModel> {
    private final Context mContext;
    private final ArrayList<AdvertisementSlotModel> listState;
    private final ArrayList<AdvertisementSlotModel> selectedSlotList = new ArrayList<>();
    private final TimeSlotAdapter myAdapter;
    private boolean isFromView = false;
    private final OnSlotSelectedListener onSlotSelectedListener;

    public interface OnSlotSelectedListener {
        void onSlotSelected();
    }

    public TimeSlotAdapter(Context context, int resource, List<AdvertisementSlotModel> objects, OnSlotSelectedListener onSlotSelectedListener) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<AdvertisementSlotModel>) objects;
        this.onSlotSelectedListener = onSlotSelectedListener;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.slot_spinner_item, null);
            holder = new ViewHolder();
//            holder.mTextView = (TextView) convertView
//                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mCheckBox.setText(listState.get(position).getTimeSlotName());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    if (isChecked) holder.mCheckBox.setTypeface(null, Typeface.BOLD);
                    else holder.mCheckBox.setTypeface(null, Typeface.NORMAL);
                    listState.get(position).setSelected(isChecked);
                    onSlotSelectedListener.onSlotSelected();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        //        private TextView mTextView;
        private CheckBox mCheckBox;
    }

    public List<AdvertisementSlotModel> getSelectedSlots() {
        selectedSlotList.clear();
        for (int i = 0; i < listState.size(); i++) {
            if (listState.get(i).isSelected()) {
                selectedSlotList.add(new AdvertisementSlotModel(listState.get(i).getAdSlotId(), listState.get(i).getTimeSlotName(), listState.get(i).getTimeSlotMatrix()));
            }
        }
        return selectedSlotList;
    }
}