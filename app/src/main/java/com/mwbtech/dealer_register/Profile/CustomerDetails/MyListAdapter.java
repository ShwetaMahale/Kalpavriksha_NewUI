package com.mwbtech.dealer_register.Profile.CustomerDetails;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.R;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
   // private MyListData[] listdata;
    private final List<SubCategoryProduct> listdata;
    Context con;
   //private List<String> listdata;
   // private

    // RecyclerView recyclerView;
    public MyListAdapter(Context context, List<SubCategoryProduct> listdata) {
        this.con = context;
        this.listdata = listdata;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        View listItem= layoutInflater.inflate(R.layout.child_category_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        final MyListData myListData = listdata[position];
        //final String myListData = listdata.get(position);
//        holder.textView.setText(listdata[position].getDescription());
//        holder.chkBox.setText(listdata[position].getDescription());
       // holder.imageView.setImageResource(listdata[position].getImgId());
        holder.chkBox.setText(listdata.get(position).getSubCategoryName());
        holder.chkBox.setChecked(listdata.get(position).isChecked());
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public CheckBox chkBox;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
//            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.chkBox = (CheckBox) itemView.findViewById(R.id.subProd);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
