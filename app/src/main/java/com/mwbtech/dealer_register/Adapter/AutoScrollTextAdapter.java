package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.SlotBookImages;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class AutoScrollTextAdapter extends RecyclerView.Adapter<AutoScrollTextAdapter.MyViewHolder> {


    //public List<AdDetailsModel> scrollTextModels = new ArrayList<>();
    public List<SlotBookImages> scrollTextModels = new ArrayList<>();
    public Context context;
    private final TextSelected selected;


    /*public AutoScrollTextAdapter(Context context, List<AdDetailsModel> textModelList, TextSelected textSelected) {
        this.context = context;
        this.scrollTextModels = textModelList;
        this.selected = textSelected;
    }*/

    public AutoScrollTextAdapter(Context context, List<SlotBookImages> textModelList, TextSelected textSelected) {
        this.context = context;
        this.scrollTextModels = textModelList;
        this.selected = textSelected;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_text_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvText.setText("" + scrollTextModels.get(position).getAdText());
        Log.e("text....",scrollTextModels.get(position).getAdText());

    }

    @Override
    public int getItemCount() {
        return scrollTextModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvText;

        public MyViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);

            tvText = (TextView) itemLayoutView.findViewById(R.id.scrollingtext);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected.onTextSelected(scrollTextModels.get(getAdapterPosition()));

                }
            });
        }
    }

    public interface TextSelected {
        void onTextSelected(SlotBookImages textModel);
    }
}
