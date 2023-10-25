package com.mwbtech.dealer_register.Dashboard.EnquirySent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.core.view.MenuItemCompat;

import com.mwbtech.dealer_register.Adapter.ChatAdapter;
import com.mwbtech.dealer_register.PojoClass.Messages;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;

public class EnquirySentChatToolbarAction implements ActionMode.Callback {
    Context context;
    ChatAdapter recyclerView_adapter;
    EnquirySentChatActivity recyclerFragment;
    ArrayList<Messages> message_models;

    AlertDialog.Builder builder;

    public EnquirySentChatToolbarAction(Context context, ChatAdapter recyclerView_adapter, EnquirySentChatActivity infofragment, ArrayList<Messages> message_model) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.recyclerFragment = infofragment;
        this.message_models = message_model;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action_bar, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            //MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            //MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_deleteall).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            //menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }



    private void goAlertBox() {
        builder = new AlertDialog.Builder(context).setTitle("Delete").setMessage(R.string.message_delete).setIcon(R.drawable.delete);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                recyclerFragment.deleteRows();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //recyclerFragment.deleteRows();
                goAlertBox();
                break;

            case R.id.action_deleteall:
                recyclerFragment.selectAll();
                break;
           /* case R.id.action_copy:

                //Get selected ids on basis of current fragment action mode
                SparseBooleanArray selected;

                selected = recyclerView_adapter
                        .getSelectedIds();

                int selectedMessageSize = selected.size();

                //Loop to all selected items
                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //get selected data in Model
                        InboxDealer model = message_models.get(selected.keyAt(i));
                        String title = model.getFirmName();
                        String subTitle = model.getBusinessDemand();
                        //Print the data to show if its working properly or not
                        Log.e("Selected Items", "Title - " + title + "\n" + "Sub Title - " + subTitle);

                    }
                }
                Toast.makeText(context, "You selected Copy menu.", Toast.LENGTH_SHORT).show();//Show toast
                mode.finish();//Finish action mode
                break;
            case R.id.action_forward:
                Toast.makeText(context, "You selected Forward menu.", Toast.LENGTH_SHORT).show();//Show toast
                mode.finish();//Finish action mode
                break;*/


        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.removeSelection();
        recyclerFragment.setNullToActionMode();
    }
}
