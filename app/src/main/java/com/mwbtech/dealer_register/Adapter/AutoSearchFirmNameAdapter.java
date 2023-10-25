package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class AutoSearchFirmNameAdapter extends ArrayAdapter<InboxDealer> {

    private List<InboxDealer> UserList;
    List<InboxDealer> matchValues1;
    private final Context mContext;
    private final int itemLayout;
    TextView strName;

    private final ListFilter listFilter = new ListFilter();
    private List<InboxDealer> dataListAllItems;

    public AutoSearchFirmNameAdapter(Context context, int resource, List<InboxDealer> storeDataLst) {
        super(context, resource, storeDataLst);
        UserList = storeDataLst;
        matchValues1 = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public InboxDealer getItem(int position) {

        return UserList.get(position);
    }

    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statelist_layout, parent, false);
        }
        strName = (TextView) view.findViewById(R.id.lbl_name);
        strName.setText(UserList.get(position).getFirmName());
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private final Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<InboxDealer>(UserList);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<InboxDealer> matchValues = new ArrayList<InboxDealer>();
                matchValues1 = new ArrayList<InboxDealer>();

                for (InboxDealer dataItem : dataListAllItems) {
                    if (dataItem.getFirmName().toLowerCase().contains(searchStrLowerCase))
                    {
                        matchValues.add(dataItem);

                    }
                }
                results.values = matchValues;
                results.count = matchValues.size();


            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                UserList = (ArrayList<InboxDealer>)results.values;
            } else {
                UserList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            }
            else {
                notifyDataSetInvalidated();
            }
        }
    }

}