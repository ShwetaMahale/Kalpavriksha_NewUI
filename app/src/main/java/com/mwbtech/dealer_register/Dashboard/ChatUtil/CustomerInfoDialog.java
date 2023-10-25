package com.mwbtech.dealer_register.Dashboard.ChatUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mwbtech.dealer_register.R;

public class CustomerInfoDialog extends BottomSheetDialogFragment {


    BottomSheetListener mListener;
    TextView tvName,tvEmail,tvConctact,tvDemand,tvcity;
    String Phone,name,email,purpose,demand,city;
    public CustomerInfoDialog(String name, String email, String phone, String purpose, String demand, String city) {
        this.name = name;
        this.email = email;
        this.Phone = phone;
        this.purpose = purpose;
        this.demand = demand;
        this.city = city;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.botom_sheet, container, false);


        tvName = v.findViewById(R.id.Name);
        tvcity = v.findViewById(R.id.city);
        tvEmail = v.findViewById(R.id.email);
        tvConctact = v.findViewById(R.id.contactno);
        tvDemand = v.findViewById(R.id.demand);

        tvName.setText(""+name);
        tvcity.setText(""+city);
        tvEmail.setText(""+email);
//        tvConctact.setText(""+Phone);


        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            if(context instanceof BottomSheetListener)
            mListener = (BottomSheetListener) context;
            else {
                throw new RuntimeException(context.toString()
                        + " must implement OnGreenFragmentListener");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
