package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;



    public class StateFilterAdapter extends BaseAdapter  {

        Activity activity;
        List<State> StateList;
        LayoutInflater inflater;
        StateFilterAdapter.ViewHolder viewHolder;
        List<State> StateSelectedList;
        List<State> StateTrueList;
        private int selectedPosition = -1;




        public StateFilterAdapter(Activity activity, List<State> stateList) {
            this.activity =activity;
            this.StateList = stateList;
            StateSelectedList = new ArrayList<>();
            StateTrueList = new ArrayList<>();
            Log.e("list","count..."+stateList.size());

        }



        @Override
        public int getCount() {
            return StateList.size();
        }

        @Override
        public Object getItem(int position) {
            return StateList.get(position);
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
                convertView = inflater.inflate(R.layout.filter_cityitem, parent, false);
                viewHolder = new ViewHolder();
                //viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
                viewHolder.chckBox = (CheckBox) convertView.findViewById(R.id.Checkbox2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            StateTrueList.clear();
            viewHolder.chckBox.setText(StateList.get(position).getStateName());
            viewHolder.chckBox.setChecked(StateList.get(position).isChecked());
            viewHolder.chckBox.setTag(StateList.get(position));
//            viewHolder.chckBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CheckBox cb = (CheckBox) v;
//                    State contact = (State) cb.getTag();
//                    contact.setChecked(cb.isChecked());
//                    StateList.get(pos).setChecked(cb.isChecked());
//                }
//            });
//            convertView.setTag(R.id.ckId, viewHolder.chckBox);
//            //convertView.setTag(viewHolder);
//
//            convertView.setTag(viewHolder);
//            return convertView;


            viewHolder.chckBox.setChecked(position == selectedPosition);

            viewHolder.chckBox.setOnClickListener(onStateChangedListener(viewHolder.chckBox, position));

            return convertView;
        }

        private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        selectedPosition = position;
                    } else {
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();
                }
            };
        }

        class ViewHolder {
           // public RadioButton radioBtn;
            public CheckBox chckBox;
        }

        public List<State> GetSelectedState() {
            try{
                StateTrueList.clear();
                for (int i = 0; i < StateList.size(); i++) {
                    if (StateList.get(i).isChecked() == true) {
                        StateTrueList.add(new State(StateList.get(i).getStateID()));
                    }
                }

            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            return StateTrueList;
        }

        public List<State> GetResultState()
        {
            StateSelectedList.clear();
            for(int i = 0; i< StateList.size(); i++){
                if(StateList.get(i).isChecked() == true) {
                    StateSelectedList.add(new State(StateList.get(i).getStateID(),StateList.get(i).getStateName(),true));
                }else
                {
                    StateSelectedList.add(new State(StateList.get(i).getStateID(),StateList.get(i).getStateName(),false));
                }
            }

            return StateSelectedList;
        }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.filter_childitems, parent, false);
//
//            viewHolder = new ViewHolder();
//            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        setRowColor(convertView,position);
//        viewHolder.tvItemName.setText(stateListFilter.get(position).getStateName());
//        return convertView;
//    }






    }
