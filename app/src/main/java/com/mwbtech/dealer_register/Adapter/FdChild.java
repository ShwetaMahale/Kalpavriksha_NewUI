package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class FdChild extends BaseAdapter {

	Activity activity;
	List<BusinessType> businessTypes;
	LayoutInflater inflater;
	ViewHolder viewHolder;
	List<BusinessType> businessTypeList;
	List<BusinessType> businessTypeTrueList;

	public FdChild(Activity activity, List<BusinessType> businessTypes) {
		this.activity =activity;
		this.businessTypes = businessTypes;
		businessTypeList = new ArrayList<>();
		businessTypeTrueList = new ArrayList<>();
	}


	@Override
	public int getCount() {
		return businessTypes.size();
	}

	@Override
	public Object getItem(int position) {
		return businessTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int pos = position;
		if (inflater == null) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.filter_childitems, parent, false);
			viewHolder = new ViewHolder();
			//viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.Checkbox2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.checkBox.setText(businessTypes.get(position).getNameOfBusiness());
		viewHolder.checkBox.setChecked(businessTypes.get(position).isChecked());
		viewHolder.checkBox.setTag(businessTypes.get(position));
		viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				BusinessType contact = (BusinessType) cb.getTag();
				contact.setChecked(cb.isChecked());
				businessTypes.get(pos).setChecked(cb.isChecked());
				if(contact.getNameOfBusiness().equals("Professionals And Services")) {
					if (cb.isChecked()) {
						for (int i=0; i < businessTypes.size(); i++) {
                            businessTypes.get(i).setChecked(businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services"));
						}

					}
				} else {
					if (cb.isChecked()) {
						for (int i = 0; i < businessTypes.size(); i++) {
							if (businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services")) {
								businessTypes.get(i).setChecked(false);
							}

						}
					}
				}
				notifyDataSetChanged();
			}
		});



		convertView.setTag(R.id.ckId, viewHolder.checkBox);
		//convertView.setTag(viewHolder);

		convertView.setTag(viewHolder);
		return convertView;
	}


	class ViewHolder {

		public CheckBox checkBox;

	}

	public List<BusinessType> getResultList()
	{
		businessTypeList.clear();
		for(int i = 0; i< businessTypes.size(); i++){
			if(businessTypes.get(i).isChecked() == true) {
				businessTypeList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(),businessTypes.get(i).getNameOfBusiness(),true));
			}else
			{
				businessTypeList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(),businessTypes.get(i).getNameOfBusiness(),false));
			}
		}
		return businessTypeList;
	}

	public List<BusinessType> getTrueResultList()
	{
		businessTypeTrueList.clear();
		for(int i = 0; i< businessTypes.size(); i++){
			if(businessTypes.get(i).isChecked() == true) {
				businessTypeTrueList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID()));
			}
		}
		Log.e("Selected","check box...."+businessTypeTrueList.size());
		return businessTypeTrueList;
	}
}
