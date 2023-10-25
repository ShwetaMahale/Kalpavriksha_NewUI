package com.mwbtech.dealer_register.Dashboard.ChatUtil;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.core.view.MenuItemCompat;

import com.mwbtech.dealer_register.Adapter.RcvInboxAdapter;
import com.mwbtech.dealer_register.Dashboard.EnquiryReceived.EnquiryReceivedActivity;
import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;

public class ToolbarActionMod implements ActionMode.Callback {
     Context context;
    RcvInboxAdapter recyclerView_adapter;
     EnquiryReceivedActivity recyclerFragment;
     ArrayList<InboxDealer> message_models;

    AlertDialog.Builder builder;

    public ToolbarActionMod(Context context, RcvInboxAdapter recyclerView_adapter, EnquiryReceivedActivity infofragment, ArrayList<InboxDealer> message_model) {
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
         builder = new AlertDialog.Builder(context).setTitle("Delete").setMessage(R.string.delete1).setIcon(R.drawable.delete);
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

    private void goAlertasRead() {
        builder = new AlertDialog.Builder(context)/*.setTitle("Mark as read")*/.setMessage("Selected item(s) will be marked as read");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                recyclerFragment.markAsReadRows();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void goAlertasunRead() {
        builder = new AlertDialog.Builder(context)/*.setTitle("Mark as read")*/.setMessage("Selected item(s) will be marked as unread");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                recyclerFragment.goAlertAsUnRead();
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

            case R.id.menu_read:
                goAlertasRead();
                break;

            case R.id.menu_unread:
                goAlertasunRead();
                break;
        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.removeSelection();
        recyclerFragment.setNullToActionMode();
    }
}
