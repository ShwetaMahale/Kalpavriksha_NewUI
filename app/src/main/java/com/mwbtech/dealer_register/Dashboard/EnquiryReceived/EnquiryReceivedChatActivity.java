package com.mwbtech.dealer_register.Dashboard.EnquiryReceived;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.ChatAdapter;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.CustomerInfoDialog;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerClickLongClickListener;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerViewTouchListener;
import com.mwbtech.dealer_register.Dashboard.NotificationsTabs.MainNotificationsActivity;
import com.mwbtech.dealer_register.PojoClass.DeleteInbox;
import com.mwbtech.dealer_register.PojoClass.Messages;
import com.mwbtech.dealer_register.PojoClass.Msg_Model;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.ProjectUtilsKt;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnquiryReceivedChatActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener, TextView.OnEditorActionListener, CustomerInfoDialog.BottomSheetListener {

    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    private static final int PICKFILE_RESULT_CODE = 3;

    static final long file_size = 5242880;
    static final long image_size = 2097152;

    File mPhotoFile, mPdfFile;
    FileCompressor mCompressor;
    Uri photoURI;


    int CustID;
    String Token;
    Customer_Interface api_interface;
    ArrayList<Messages> msg_models = new ArrayList<>();
    ArrayList<Messages> msg_modelArrayList = new ArrayList<>();
    RecyclerView mRecyclerView;
    EditText editText;
    LinearLayoutCompat coordinatorLayout;
    PrefManager prefManager;
    ImageView imageView, send, attach;
    int QryId, RecieverID;
    ChatAdapter chatAdapter;
    String Phone, name, email, purpose, demand, city;
    AppCompatImageView imgCall, imgInfo, back;
    TextView receiverName;

    AlertDialog.Builder builder;
    List<Messages> msgList = new ArrayList<>();
    List<DeleteInbox> deleteInboxes = new ArrayList<>();

    private ActionMode mActionMode;
        boolean isfromnotification=false;
    SparseBooleanArray selected;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void showContent() {
        setContentView(R.layout.test);

        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        initializeRecyclerViewTouchListener();
        initializeClickEvent();
        getUserMessages();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternetConnected();
            }
        });
    }

    private void checkInternetConnected() {
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    }


    private void initializeViews() {
        coordinatorLayout = findViewById(R.id.parentLayout);
        mCompressor = new FileCompressor(this);
        editText = findViewById(R.id.et_search_box);
        imageView = findViewById(R.id.add_image);
        send = findViewById(R.id.add_image1);
        attach = findViewById(R.id.attach_image);
        receiverName = findViewById(R.id.receiver_name);
        imgCall = findViewById(R.id.information);
        back = findViewById(R.id.back);
        imgInfo = findViewById(R.id.call);
        msg_modelArrayList.clear();
        msg_models.clear();
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    }


    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        isfromnotification=getIntent().getBooleanExtra("notification",false);
        RecieverID = getIntent().getExtras().getInt("recId");
        QryId = getIntent().getExtras().getInt("QryID");
        Phone = getIntent().getStringExtra("phone_no");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        purpose = getIntent().getStringExtra("purpose");
        demand = getIntent().getStringExtra("demand");
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
        receiverName.setText(name);
//        setTitle("" + name);
    }

    private void initializeRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(chatAdapter);

    }

    private void initializeRecyclerViewTouchListener() {
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getBaseContext(), mRecyclerView, new RecyclerClickLongClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                onListItemSelect(position);
            }
        }));
    }

    private void initializeClickEvent() {

        editText.setOnEditorActionListener(this);
        imageView.setOnClickListener(this);
        send.setOnClickListener(this);
        attach.setOnClickListener(this);

        imgCall.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
        back.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }


    private void onListItemSelect(int position) {
        chatAdapter.toggleSelection(position);
        boolean hasCheckedItems = chatAdapter.getSelectedCount() > 0;//Check if any items are already selected or not

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode

            mActionMode = startSupportActionMode(new EnquiryReceivedChatToolbarAction(this, chatAdapter, this, msg_modelArrayList));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(chatAdapter
                    .getSelectedCount() + " selected");
    }


    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    public void selectAll() {
        chatAdapter.selectAll();
        int count = chatAdapter.getSelectedItemCount();
        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(count + " selected");
            mActionMode.invalidate();
        }
        //mActionMode = null;
    }


    //Delete selected rows
    public void deleteRows() {
        msgList.clear();
        deleteInboxes.clear();
        selected = chatAdapter.getSelectedIds();//Get selected ids
        for (int i = selected.size() - 1; i >= 0; i--) {
            if (selected.valueAt(i)) {
                msgList.add(msg_modelArrayList.get(selected.keyAt(i)));
                chatAdapter.removeItem(selected.keyAt(i));
                chatAdapter.notifyDataSetChanged();//notify CitySelectedAdapter
            }
        }

        for (Messages messages : msgList) {
            deleteInboxes.add(new DeleteInbox(CustID, messages.getQueryId(), CustID, messages.getID()));
        }
        if (ConnectivityReceiver.isConnected()) {
            deleteIndividualConversion(deleteInboxes);
            mActionMode.finish();
        } else {
            showError();
        }

    }

    private void deleteIndividualConversion(List<DeleteInbox> deleteInboxes) {
        api_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DeleteInbox> call = api_interface.deleteChat("bearer " + Token, CustID, deleteInboxes);
        Gson gson = new Gson();
        String json = gson.toJson(deleteInboxes);
        Log.e("Deletechat",json);
        call.enqueue(new Callback<DeleteInbox>() {
            @Override
            public void onResponse(Call<DeleteInbox> call, Response<DeleteInbox> response) {
                int statusCode = response.code();
                Log.e("statuscode..indelete",String.valueOf(statusCode));
                switch (statusCode) {
                    case 200:
                        break;
                    case 404:
                        break;
                    case 500:
                        //CustomToast.showToast(EnquiryReceivedChatActivity.this, "Server Error");
                        break;
                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(EnquiryReceivedChatActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<DeleteInbox> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryReceivedChatActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryReceivedChatActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void getUserMessages() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryReceivedChatActivity.this,"Please wait");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        msg_modelArrayList.clear();
        api_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Msg_Model> call = api_interface.getMessages("bearer " + Token, CustID, QryId, RecieverID);
        Log.e("Get messges...", CustID + "" + QryId + "" + RecieverID);
        call.enqueue(new Callback<Msg_Model>() {
            @Override
            public void onResponse(Call<Msg_Model> call, Response<Msg_Model> response) {
                int status_code = response.code();
                Log.e("statuscode..getmessege",String.valueOf(status_code));
                progressDialog.dismiss();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        Msg_Model model = response.body();
                        city = model.getVillageLocalityname();
                        Log.e("cust", "......" + model.getCustID() + "....." + CustID);
                        if (model.getCustID() == CustID) {

                            if (!TextUtils.isEmpty(model.getImage())) {
                                msg_modelArrayList.add(new
                                        Messages(Messages.SENDER_TYPE,
                                        model.getRequirements(),
                                        model.getImage(),
                                        model.getImage2(),
                                        model.getCreatedDate(), "image"));

                            } else {
                                msg_modelArrayList.add(new
                                        Messages(Messages.SENDER_TYPE,
                                        model.getRequirements(),
                                        model.getImage(),
                                        model.getImage2(),
                                        model.getCreatedDate()));
                            }

                        } else {
                            if (!TextUtils.isEmpty(model.getImage())) {
                                msg_modelArrayList.add(new
                                        Messages(Messages.RECV_TYPE,
                                        model.getRequirements(),
                                        model.getImage(),
                                        model.getImage2(),
                                        model.getCreatedDate(), "image"));
                            } else {
                                msg_modelArrayList.add(new
                                        Messages(Messages.RECV_TYPE,
                                        model.getRequirements(),
                                        model.getImage(),
                                        model.getImage2(),
                                        model.getCreatedDate()));
                            }
                        }

                        if (model.getMessagesArrayList().size() == 0) {

                        } else {
                            msg_models = model.getMessagesArrayList();
                            for (int i = 0; i < msg_models.size(); i++) {
                                if (msg_models.get(i).getCustID() == CustID) {
                                    if (!TextUtils.isEmpty(msg_models.get(i).getMessage()) || !TextUtils.isEmpty(msg_models.get(i).getImage())) {
                                        msg_modelArrayList.add(
                                                new Messages(Messages.SENDER_TYPE,
                                                        msg_models.get(i).getMessage(),
                                                        msg_models.get(i).getImage(),
                                                        msg_models.get(i).getCreatedDate(),
                                                        msg_models.get(i).getQueryId(),
                                                        msg_models.get(i).getID(),
                                                        msg_models.get(i).getFileType(),
                                                        msg_models.get(i).getFileName()));
                                    }
                                } else {
                                    if (!TextUtils.isEmpty(msg_models.get(i).getMessage()) || !TextUtils.isEmpty(msg_models.get(i).getImage())) {
                                        msg_modelArrayList.add(
                                                new Messages(Messages.RECV_TYPE,
                                                        msg_models.get(i).getMessage(),
                                                        msg_models.get(i).getImage(),
                                                        msg_models.get(i).getCreatedDate(),
                                                        msg_models.get(i).getQueryId(),
                                                        msg_models.get(i).getID(),
                                                        msg_models.get(i).getFileType(),
                                                        msg_models.get(i).getFileName()));
                                    }
                                }

                            }
                        }
                        mRecyclerView.getRecycledViewPool().clear();
                        chatAdapter = new ChatAdapter(msg_modelArrayList, EnquiryReceivedChatActivity.this, model.getRequirements());
                        mRecyclerView.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                        int bottom = mRecyclerView.getAdapter().getItemCount() - 1;
                        mRecyclerView.smoothScrollToPosition(bottom);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryReceivedChatActivity.this);
                        break;
                }


            }

            @Override
            public void onFailure(Call<Msg_Model> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryReceivedChatActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryReceivedChatActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }


    // input msg
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String str = editText.getText().toString().trim();
            if (!str.startsWith(" ") && !str.isEmpty()) {
                if (ConnectivityReceiver.isConnected()) {
                    SendMessageToServer(editText.getText().toString(), mPhotoFile,mPdfFile);
                    editText.getText().clear();
                } else {
                    showError();
                }

                return true;
            } else {
                Toast.makeText(this, "Please insert message", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void SendMessageToServer(String stringMsg, File imageFile,File pdfFile ) {

        // convert string to unicode
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(stringMsg);
        Log.e("unicode", "message......" + toServerUnicodeEncoded);
        MultipartBody.Part body = null;
        try {
            if (imageFile != null) {
                // File file = new File(imageString);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
            }
            if (pdfFile != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), pdfFile);
                body = MultipartBody.Part.createFormData("file", pdfFile.getName(), requestFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryReceivedChatActivity.this, "Please wait");
        progressDialog.setMessage("Please wait.."); // Setting Message
        progressDialog.setTitle("Sending message"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        api_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Messages messages = new Messages(QryId, CustID, toServerUnicodeEncoded, RecieverID, 1, null, CustID);
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        Log.e("Sendmsg",json);
        Call<Messages> call = api_interface.SendMessage("bearer " + Token, messages, body);
        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {

                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        getUserMessages();
                        editText.getText().clear();
                        mPhotoFile = null;
                        mPdfFile = null;
                        // to delete the stored images in package directory
                        clearApplicationData();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryReceivedChatActivity.this);
                        break;
                }


            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    public void clearApplicationData() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File appDir = new File(storageDir.getParent());
        Log.e("cache", "path........." + appDir);
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("files")) {
                    deleteDir(new File(appDir, s));
                    Log.e("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu, menu);
        MenuItem call = menu.findItem(R.id.call);
        MenuItem info = menu.findItem(R.id.information);
        call.setOnMenuItemClickListener(this);
        info.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                if (ConnectivityReceiver.isConnected()) {
                    builder = new AlertDialog.Builder(EnquiryReceivedChatActivity.this).setTitle("Call").setMessage(R.string.call).setIcon(R.drawable.phone);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + Phone));
                            startActivity(callIntent);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    showError();
                }
                break;
            case R.id.information:
                if (ConnectivityReceiver.isConnected()) {
                    CallBottomSheet();
                } else {
                    showError();
                }

                break;
        }
        return false;
    }

    private void CallBottomSheet() {
        CustomerInfoDialog bottomSheet = new CustomerInfoDialog(name, email, Phone, purpose, demand, city);
        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.information:
                CallBottomSheet();
                break;

            case R.id.back:
                onBackPressed();
                break;

            case R.id.call:
                builder = new AlertDialog.Builder(this).setTitle("Call").setMessage(R.string.call).setIcon(R.drawable.phone);
                builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + Phone));
                    startActivity(callIntent);
                }).setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

            case R.id.add_image:
                if (ConnectivityReceiver.isConnected()) {
                    selectImage();
                } else {
                    showError();
                }

                break;
            case R.id.attach_image:
                if (ConnectivityReceiver.isConnected()) {
                    attachdoc();
                } else {
                    showError();
                }
                break;

            case R.id.add_image1:
                String str = editText.getText().toString().trim();
                if (!str.startsWith(" ") && !str.isEmpty()) {
                    if (ConnectivityReceiver.isConnected()) {
                        SendMessageToServer(editText.getText().toString(), mPhotoFile,mPdfFile);
                        editText.getText().clear();
                    } else {
                        showError();
                    }

                } else {
                    Toast.makeText(this, "Please insert message", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onButtonClicked(String text) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if(isfromnotification) {
            intent = new Intent(this, MainNotificationsActivity.class);
        }
        else {
            intent = new Intent(this, EnquiryReceivedActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        EnquiryReceivedChatActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachdoc() {

        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(PICKFILE_RESULT_CODE)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withFilterDirectories(false)
                .withTitle("Sample title")
                .withHiddenFiles(true)
                .start();

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EnquiryReceivedChatActivity.this);
        builder.setTitle("Choose your picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    dispatchGalleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.mwbtech.dealer_register" + ".provider",
                        photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ProjectUtilsKt.checkCameraPermission(EnquiryReceivedChatActivity.this, (status -> {
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    return null;
                }));

            }
        }
    }


    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                    if (resultCode == RESULT_OK) {
                        String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                        Uri uri = data.getData();
                        mPdfFile = new File(filePath);
                        //mPdfFile=new File(Objects.requireNonNull(FilePath.getPath(this, uri)));
                        Log.e("File", "file path.........." + mPdfFile);
                        long len_pdf=mPdfFile.length();
                        System.out.println("Lenght...."+len_pdf);
                        if(len_pdf<file_size) {
                            SendMessageToServer(editText.getText().toString(), mPhotoFile, mPdfFile);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please select the file within 5MB",Toast.LENGTH_LONG);
                        }
                    }
                break;
            case REQUEST_GALLERY_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))){
                            mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            Log.e("Gallery", "file path.........." + mPhotoFile);
                            long len_img=mPhotoFile.length();
                            if(len_img<image_size) {
                                SendMessageToServer(editText.getText().toString().trim(), mPhotoFile, mPdfFile);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please select the image within 2MB",Toast.LENGTH_LONG);
                            }
                        }
                        else{
                            CustomToast.showToast(getApplicationContext(), "Please select the image from Gallery");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 5000);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.e("URI", "................." + photoURI.toString());

                    try {
                        mPhotoFile = mCompressor.compressToFile(mPhotoFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Camera", "file path.........." + mPhotoFile);
                    long len_img=mPhotoFile.length();
                    if(len_img<image_size) {
                        SendMessageToServer(editText.getText().toString().trim(), mPhotoFile,mPdfFile);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please select the image within 2MB",Toast.LENGTH_LONG);
                    }
                }
                break;
        }
}
    /**
     * Create file with current timestamp name
     *
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}



