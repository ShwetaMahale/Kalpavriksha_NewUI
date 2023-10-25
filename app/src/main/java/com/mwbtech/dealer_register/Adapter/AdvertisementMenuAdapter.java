package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mwbtech.dealer_register.R;

public class AdvertisementMenuAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] maintitle;

    public AdvertisementMenuAdapter(Activity context, String[] maintitle) {
        super(context, R.layout.menu_list_items, maintitle);

        this.context=context;
        this.maintitle=maintitle;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.menu_list_items, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        titleText.setText(maintitle[position]);

        return rowView;

    }
}
