package com.mwbtech.dealer_register.Dashboard.EnquirySent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.core.view.MenuItemCompat;

import com.mwbtech.dealer_register.Adapter.OutBoxAdapter;
import com.mwbtech.dealer_register.PojoClass.OutboxDealer;
import com.mwbtech.dealer_register.R;

import java.util.List;

public class EnquirySentListToolbarActionMode implements ActionMode.Callback {
    Context context;
    OutBoxAdapter recyclerView_adapter;
    EnquirySentListActivity recyclerFragment;
    List<OutboxDealer> message_models;
    AlertDialog.Builder builder;

    public EnquirySentListToolbarActionMode(Context context, OutBoxAdapter recyclerView_adapter, EnquirySentListActivity infofragment, List<OutboxDealer> message_model) {
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


        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);

        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_deleteall).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.menu_read).setVisible(false);
            menu.findItem(R.id.menu_unread).setVisible(false);
        }

        return true;
    }



    private void goAlertBox() {
        builder = new AlertDialog.Builder(context).setTitle("Delete").setMessage("Do you want to delete Enquiry ?").setIcon(R.drawable.ic_delete_black);
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
        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.removeSelection();
        recyclerFragment.setNullToActionMode();
    }
}
