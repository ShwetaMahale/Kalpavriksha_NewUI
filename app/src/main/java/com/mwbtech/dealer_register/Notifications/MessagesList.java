package com.mwbtech.dealer_register.Notifications;


import static android.app.Activity.RESULT_OK;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.creation;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.dealerRegister;
import static com.mwbtech.dealer_register.Dashboard.NotificationsTabs.MainNotificationsActivity.prefManager;
import static com.mwbtech.dealer_register.Dashboard.NotificationsTabs.MainNotificationsActivity.updateDealerRegister;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_PASSWORD;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_TYPE;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.BusinessDemandCustomer;
import com.mwbtech.dealer_register.Adapter.BusinessTypeAdapter;
import com.mwbtech.dealer_register.Adapter.ChildCategoryProductAdapter;
import com.mwbtech.dealer_register.Adapter.SubCategoryAdapter;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.MainCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.Profile.CustomerDetails.MyListAdapter;
import com.mwbtech.dealer_register.Profile.CustomerDetails.RecycleitemSelecetd;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.ProjectUtilsKt;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesList extends Fragment implements View.OnClickListener, RecycleitemSelecetd, SubCategoryAdapter.RecycleitemSelecet, BusinessTypeAdapter.BusinessTypeClick {
    Spinner spLedger;
    Button btnNext;
    EditText edChild, edSub, edMain;
    View customerView;
    int pos = 1,CustID=0;
    String Token;
    CallToBillFragment callToBillFragment;
    EditText edProduct;
    Button btnSearch,Clear_txt_btn;
    RecyclerView recyclerViewMain, recyclerViewMother;
    ImageView imageView;
    GridView listBusines;
    Dialog  mDialogSubCategory, mDialogMainCategory;
    EditText edFirmName, edOwnerName, edMobileNo, edMobileNo1, edEmailId, edTelephone, edAdditionPerson;
    CheckBox checkBoxSubCategory;
    BusinessTypeAdapter businessTypeAdapter;
    BusinessDemandCustomer businessDemandAdaper;
    List<BusinessType> businessTypeList;
    List<BusinessDemand> businessDemand;
    Button btnSub, btnMain;
    List<SubCategoryProduct> subCategoryProducts;
    List<MainCategoryProduct> mainCategoryProductList;
    List<BusinessType> checkedBusinessType;
    ChildCategoryProductAdapter childProductAdapter;
    SubCategoryAdapter subCategoryAdapter;
    List<SubCategoryProduct> selectSubIdList = new ArrayList<>();
    List<SubCategoryProduct> FinalSelectedSubList = new ArrayList<>();
    Customer_Interface customer_interface;
    RecyclerView recyclerVieww;
    ChipGroup chipGroupDemo;
    ScrollView chipScroll;
    HorizontalScrollView hsv;
    //String regFname = "^(?![\\s.]+$)[a-zA-Z\\s.]*$";   //"^[A-Za-z]+$";
    String regNumber = "^[5-9][0-9]{9}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    String yahooPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+";

    GridView listBusinessDemand;
    List<BusinessDemand> businessDemandList;
    private AppCompatImageView editBtn;
    //String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+(.[a-z]+)$";
    /*String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"

            +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."

            +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"

            +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
*/
    //String emailPattern = "^(.+)@(.+)$";

    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + ".{10}" + "$");
    boolean isprofessional;
    AlertDialog.Builder builder,builder1;
    ImageView imgPhoto1,imgPhoto2;
    Button btnPhoto,btnPhoto1;
    Dialog mdialogPhoto;
    int imgCount = 0;
    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final long image_size = 2097152;
    File mPhotoFile1,mPhotoFile2;
    FileCompressor mCompressor;
    String imgBase64 = "";
    Button submitBtn;
    CheckBox checkbox1;
    public boolean isSaved=false;
    ConstraintLayout LinearBusinessDemand;
    int DemandID;
/*
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof CallToBillFragment) {
                callToBillFragment = (CallToBillFragment) activity;
            } else {
                throw new RuntimeException(activity.toString()
                        + " must implement OnGreenFragmentListener");
            }
        } catch (ClassCastException e) {
        }
    }*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customerView = inflater.inflate(R.layout.customer_details, null);
       // GetBusinessDemand();
        businessTypeList = new ArrayList<>();
        checkedBusinessType = new ArrayList<>();
       // CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
        mainCategoryProductList = new ArrayList<>();
        edFirmName = customerView.findViewById(R.id.edFirmName);
        edChild = customerView.findViewById(R.id.edChildCategoryName);
        edSub = customerView.findViewById(R.id.edMCategoryName);
        editBtn = customerView.findViewById(R.id.img_edit_profile);
        edMain = customerView.findViewById(R.id.edFCategoryName);
        edOwnerName = customerView.findViewById(R.id.edCustomer);
        edEmailId = customerView.findViewById(R.id.edEmailId);
        edMobileNo = customerView.findViewById(R.id.edOwnerNo);
        edMobileNo1 = customerView.findViewById(R.id.edMobile);
        edAdditionPerson = customerView.findViewById(R.id.edContactPerson);
        btnNext = customerView.findViewById(R.id.btnNext);
        edTelephone = customerView.findViewById(R.id.edtelephone);
        //spLedger = customerView.findViewById(R.id.spinnerLedger);
        listBusines = customerView.findViewById(R.id.listBusines);
        chipGroupDemo = customerView.findViewById(R.id.chipgroup);
        hsv = customerView.findViewById(R.id.horizontalScrollView1);
        submitBtn = customerView.findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(this);
        imgPhoto1 = customerView.findViewById(R.id.imag1);
        imgPhoto1.setOnClickListener(this);
        editBtn.setOnClickListener((v)->imgPhoto1.performClick());

        checkbox1 = customerView.findViewById(R.id.checkbox1);
        recyclerVieww = customerView.findViewById(R.id.listSubcat);
        listBusinessDemand=customerView.findViewById(R.id.listBusinesDemand);
        LinearBusinessDemand=customerView.findViewById(R.id.linearBusinessDemand);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCompressor = new FileCompressor(getContext());
        sharePreferencesMethod();
        btnNext.setOnClickListener(this);
        edChild.setOnClickListener(this);
        edSub.setOnClickListener(this);
        edMain.setOnClickListener(this);
        return customerView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void sharePreferencesMethod() {
        try {
            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-dealer_register", "customer", DealerRegister.class);
            Gson gson=new Gson();
            Log.e("model...Creation",gson.toJson(creation));
            if (creation != null) {
                if (!TextUtils.isEmpty(creation.getFirmName()))
                {
                    edFirmName.setText("" + creation.getFirmName());
                }else
                {
                    edFirmName.setText("");
                }
                if (!TextUtils.isEmpty(creation.getCustName()))
                {
                    edOwnerName.setText("" +creation.getCustName());
                }else
                {
                    edOwnerName.setText("");
                }
                if (!TextUtils.isEmpty(creation.getMobileNumber()))
                {
                    edMobileNo.setText("" + creation.getMobileNumber());
                }else
                {
                    edMobileNo.setText("");
                }
                if (!TextUtils.isEmpty(creation.getMobileNumber2()))
                {
                    edMobileNo1.setText("" + creation.getMobileNumber2());
                }else
                {
                    edMobileNo1.setText("");
                }

                if (!TextUtils.isEmpty(creation.getTelephoneNumber()))
                {
                    edTelephone.setText("" + creation.getTelephoneNumber());
                }else
                {
                    edTelephone.setText("");
                }
                if (!TextUtils.isEmpty(creation.getAdditionalPersonName()))
                {
                    edAdditionPerson.setText("" + creation.getAdditionalPersonName());
                }else
                {
                    edAdditionPerson.setText("");
                }

                if (!TextUtils.isEmpty(creation.getEmailID()))
                {
                    edEmailId.setText("" + creation.getEmailID());
                }else
                {
                    edEmailId.setText("");
                }
                checkbox1.setChecked(creation.isBGSMember());
                if(mPhotoFile1!=null){
                    Glide.with(this).load(mPhotoFile1).into(imgPhoto1);
                }
                else if (creation.isUserImage() == null) {
                        if(creation.getUserImageFile()!=null){
                            Glide.with(this).load(creation.getUserImageFile()).into(imgPhoto1);
                        }
                        else{
                            Glide.with(this).load(R.drawable.profilephoto).into(imgPhoto1);
                        }
                }
                else{
                    Glide.with(this).load(creation.getUserImage()).into(imgPhoto1);
                    Log.e("IMageUpdate","Creation preference");
                }
                /***
                 * ---Set Business Type---*
                 * **/

                if (creation.getBusinessTypeWithCust().size() != 0)
                {
                    businessTypeList = creation.getBusinessTypeWithCust();
                    //businessTypeList = creation.getBusinessTypeIDList();
                    Log.e("businesslist...",creation.getBusinessTypeWithCust().toString());
                    //Log.e("businesslist...",creation.getBusinessTypeIDList().toString());
                    businessTypeAdapter = new BusinessTypeAdapter(getActivity(), businessTypeList,this);
                    listBusines.setAdapter(businessTypeAdapter);

                    int a=0;
                    for (int i=0;i<businessTypeList.size();i++)
                    {
                        Log.e("businessTypeIndex..",String.valueOf(i));
                        if (businessTypeList.get(i).getNameOfBusiness().equals("Professionals And Services"))
                        {
                            if (businessTypeList.get(i).isChecked())
                            {
                                isprofessional=true;
                                Log.e("condition...",String.valueOf(isprofessional));
                                edSub.setHint("Select Service");
                                LinearBusinessDemand.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                if (creation.getBusinessDemandWithCust().size() != 0) {
                    businessDemandList = creation.getBusinessDemandWithCust();
                    //businessTypeList = creation.getBusinessTypeIDList();
                    Log.e("businessdemandlist...", creation.getBusinessDemandWithCust().toString());
                    //Log.e("businesslist...",creation.getBusinessTypeIDList().toString());
                    businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    businessDemandAdaper.notifyDataSetChanged();
                }

                for (int i = 0; i < creation.getSubCategoryTypeWithCust().size(); i++) {
                    SubCategoryProduct childCategoryProduct = creation.getSubCategoryTypeWithCust().get(i);
                    selectSubIdList.add(new SubCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)), childCategoryProduct.getCategoryProductId(), childCategoryProduct.getSubCategoryId(), childCategoryProduct.getSubCategoryName(), childCategoryProduct.isChecked()));
                }

                if (creation.getBusinessDemandWithCust().size() != 0) {
                    businessDemandList = creation.getBusinessDemandWithCust();
                    //businessTypeList = creation.getBusinessTypeIDList();
                    Log.e("businessdemandlist...", creation.getBusinessDemandWithCust().toString());
                    //Log.e("businesslist...",creation.getBusinessTypeIDList().toString());
                    businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    businessDemandAdaper.notifyDataSetChanged();
                }


                /***
                 * ---Setting Product List OR Service List in Chips ---*
                 * **/

                hsv.setVisibility(View.VISIBLE);
                for (SubCategoryProduct category : creation.getSubCategoryTypeWithCust()) {

                    Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_item, chipGroupDemo, false);
                    mChip.findViewById(R.id.chips);
                    mChip.setText(category.getSubCategoryName());
                    Chip chip = mChip;
                    mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {
                                                              category.setChecked(false);
                                                              chipGroupDemo.removeView(chip);
                                                          }
                                                      }
                    );
                    chipGroupDemo.addView(mChip);
                }
                FinalSelectedSubList = creation.getSubCategoryTypeWithCust();
            }
            /***
             * ---if "Creation" object is null then Setting Fields Using "UpdateDealerRegister"---*
             * **/

            else {
                updateDealerRegister = prefManager.getSavedObjectFromPreference(getContext(), "mwb-dealer_register", "customer", DealerRegister.class);
                Gson gson1=new Gson();
                Log.e("model...Update",gson1.toJson(updateDealerRegister));
                if (!TextUtils.isEmpty(updateDealerRegister.getFirmName()))
                {
                    edFirmName.setText("" + updateDealerRegister.getFirmName());
                }else
                {
                    edFirmName.setText("");
                }

                if (!TextUtils.isEmpty(updateDealerRegister.getCustName()))
                {
                    edOwnerName.setText("" +updateDealerRegister.getCustName());
                }else
                {
                    edOwnerName.setText("");
                }

                if (!TextUtils.isEmpty(updateDealerRegister.getMobileNumber()))
                {
                    edMobileNo.setText("" + updateDealerRegister.getMobileNumber());
                }else
                {
                    edMobileNo.setText("");
                }

                if (!TextUtils.isEmpty(updateDealerRegister.getMobileNumber2()))
                {
                    edMobileNo1.setText("" + updateDealerRegister.getMobileNumber2());
                }else
                {
                    edMobileNo1.setText("");
                }

                if (!TextUtils.isEmpty(updateDealerRegister.getTelephoneNumber()))
                {
                    edTelephone.setText("" + updateDealerRegister.getTelephoneNumber());
                }else
                {
                    edTelephone.setText("");
                }

                if (!TextUtils.isEmpty(updateDealerRegister.getAdditionalPersonName()))
                {
                    edAdditionPerson.setText("" + updateDealerRegister.getAdditionalPersonName());
                }else
                {
                    edAdditionPerson.setText("");
                }

                if (!TextUtils.isEmpty(updateDealerRegister.getEmailID()))
                {
                    edEmailId.setText("" + updateDealerRegister.getEmailID());
                }else
                {
                    edEmailId.setText("");
                }
                checkbox1.setChecked(updateDealerRegister.isBGSMember());
                //image file
                if (updateDealerRegister.isUserImage() == null) {
                    Picasso.get()
                            .load(R.drawable.profilephoto)
                            .into(imgPhoto1);

                }
                else if(updateDealerRegister.isUserImage().isEmpty()) {
                    Picasso.get()
                            .load(R.drawable.profilephoto)
                            .into(imgPhoto1);
                } else if(updateDealerRegister.isUserImage().equals("null")) {
                    Picasso.get()
                            .load(R.drawable.profilephoto)
                            .into(imgPhoto1);
                }
                else{
                    Picasso.get()
                            .load(updateDealerRegister.getUserImage())
                            .into(imgPhoto1);
                }




                //set business types

                if (updateDealerRegister.getBusinessTypeWithCust().size() != 0)
                {
                    businessTypeList = updateDealerRegister.getBusinessTypeWithCust();
                    Log.e("businesstypelist....",updateDealerRegister.getBusinessTypeWithCust().toString());
                    //Log.e("businesstypelist....",updateDealerRegister.getBusinessTypeWithCust().toString());
                    businessTypeAdapter = new BusinessTypeAdapter(getActivity(), businessTypeList,this);
                    listBusines.setAdapter(businessTypeAdapter);

                    for (int i=0;i<businessTypeList.size();i++)
                    {
                        if (businessTypeList.get(i).getNameOfBusiness().equals("Professionals And Services"))
                        {
                            if (businessTypeList.get(i).isChecked())
                            {
                                isprofessional=true;
                                edSub.setHint("Select Service");
                                LinearBusinessDemand.setVisibility(View.GONE);

                            }
                        }
                        else{
                            LinearBusinessDemand.setVisibility(View.VISIBLE);
                        }
                    }
                }


                //setting product list in chips
                hsv.setVisibility(View.VISIBLE);
                for (SubCategoryProduct category : updateDealerRegister.getSubCategoryTypeWithCust()) {

                    Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_item, chipGroupDemo, false);
                    mChip.findViewById(R.id.chips);
                    mChip.setText(category.getSubCategoryName());
                    Chip chip = mChip;
                    mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {
                                                              category.setChecked(false);
                                                              chipGroupDemo.removeView(chip);
                                                          }
                                                      }
                    );
                    chipGroupDemo.addView(mChip);
                }
                FinalSelectedSubList = updateDealerRegister.getSubCategoryTypeWithCust();


                if (updateDealerRegister.getBusinessDemandWithCust().size() != 0)
                {

                    businessDemandList = updateDealerRegister.getBusinessDemandWithCust();
                    //businessTypeList = creation.getBusinessTypeIDList();
                    Log.e("businessdemandlist...",updateDealerRegister.getBusinessDemandWithCust().toString());
                    //Log.e("businesslist...",creation.getBusinessTypeIDList().toString());
                    businessDemandAdaper = new BusinessDemandCustomer(getActivity(),businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    businessDemandAdaper.notifyDataSetChanged();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //sharePreferencesMethod();

    }
    /***
     * ---------Activity result when user tries to upload image-------*
     ***/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    if (imgCount==0)
                    {
                        mPhotoFile1 = mCompressor.compressToFile(mPhotoFile1);
                        // imgPhoto1.setImageBitmap(mPhotoFile1);
                        long len_img=mPhotoFile1.length();
                        if(len_img<image_size) {
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(imgPhoto1);
                            /** Pk Code**/

                            dealerRegister.setUserImageFile(mPhotoFile1);
                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);

                            String a = mPhotoFile1.getPath();
                            final Bitmap selectedImage = BitmapFactory.decodeFile(a);
                            imgBase64 = encodeImage(selectedImage);
                        }
                        else{
                            Toast.makeText(getActivity(),"Please select the image within 2MB",Toast.LENGTH_LONG);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();

                try {
                    if (imgCount==0) {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))){
                            mPhotoFile1 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                        // imgPhoto1.setImageBitmap(mPhotoFile1);
                        long len_img = mPhotoFile1.length();
                        if (len_img < image_size) {
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(imgPhoto1);

                            String a = mPhotoFile1.getPath();
                            final Bitmap selectedImagee = BitmapFactory.decodeFile(a);
                            imgBase64 = encodeImage(selectedImagee);
                            /** Pk Code**/
                            dealerRegister.setUserImageFile(mPhotoFile1);
                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        } else {
                            Toast.makeText(getActivity(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                        }
                    }
                        else{
                            CustomToast.showToast(getActivity(), "Please select the image from Gallery");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 5000);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * ----------------Method used to encode Image------------*
     * */

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    /***
     * ----------Initializing OnclickListeners on Business
     * Types(Retailer,wholesaler,Professional...etc)-----------**/
    @Override
    public void TypeOfBusinessClick(int pos, List<BusinessType> types) {


        if (types.get(pos).getNameOfBusiness().equals("Professionals And Services"))
        {
            isprofessional=true;
            edSub.setHint("Select Service");
            LinearBusinessDemand.setVisibility(View.GONE);
            selectSubIdList.clear();
            callChip(selectSubIdList);
        }else
        {
            for (int i=0; i<types.size(); i++)
            {
                if(types.get(i).getNameOfBusiness().equals("Professionals And Services") && types.get(i).isChecked()){
                    isprofessional=false;
                    edSub.setHint(R.string.text_select_product);
                    LinearBusinessDemand.setVisibility(View.GONE);
                    selectSubIdList.clear();
                    callChip(selectSubIdList);
                } else {
                    isprofessional=false;
                    edSub.setHint(R.string.text_select_product);
                    LinearBusinessDemand.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    public boolean savedata(){
        boolean value=false;
              if (!ValidateOwnwername() | !ValidateBusiness() | !ValidateProducts() | !ValidateMobileNumber() | !ValidateFirmname()) {
                  value=false;
            return value;
        } else {
            isSaved=true;
            Log.d("save","Saved Is "+isSaved);
            selectSubIdList = GetUpdatedSubList();
            Log.e("Üpdated","final....."+selectSubIdList.size());
            if (selectSubIdList.size()==0 | selectSubIdList.isEmpty())
            {
                CustomToast.showToast(getActivity(),"Product must not be empty");
            }else
            {
                value=true;
                if (!TextUtils.isEmpty(edEmailId.getText().toString())) {
                    if (!ValidateEmail()) {
                        value=false;
                        return value;
                    } else {
                           value = true;
                           if(isprofessional){
                               Log.e("Demandid...", creation.getBusinessDemandWithCust().toString());
                               dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                       edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                       prefManager.getCustId().get(CUST_TYPE), selectSubIdList, creation.getBusinessTypeWithCust(),
                                       edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                       edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString(),
                                       "", checkbox1.isChecked(), mPhotoFile1, creation.getBusinessDemandID());
                               if (mPhotoFile1 == null)
                                   dealerRegister.setUserImage(creation.getUserImage());
                               prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                               Gson gson = new Gson();
                               Log.e("modelinKYC...", gson.toJson(dealerRegister));
                               Log.e("ImageSelected", dealerRegister.toString());
                           }
                           else{
                               Log.e("Demandid...", creation.getBusinessDemandWithCust().toString());
                               dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                       edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                       prefManager.getCustId().get(CUST_TYPE), selectSubIdList, creation.getBusinessTypeWithCust(),
                                       edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                       edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString(),
                                       "", checkbox1.isChecked(), mPhotoFile1, creation.getBusinessDemandID(),creation.getBusinessDemandWithCust());
                               if (mPhotoFile1 == null)
                                   dealerRegister.setUserImage(creation.getUserImage());
                               prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                               Gson gson = new Gson();
                               Log.e("modelinKYC...", gson.toJson(dealerRegister));
                               Log.e("ImageSelected", dealerRegister.toString());
                           }

                       //}
                    }
                } else {
                    value=true;
                    if(isprofessional){
                        Log.e("Demandid...", creation.getBusinessDemandWithCust().toString());
                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                prefManager.getCustId().get(CUST_TYPE), selectSubIdList, creation.getBusinessTypeWithCust(),
                                edOwnerName.getText().toString(), "", edMobileNo.getText().toString(),
                                edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString(), "", checkbox1.isChecked(),
                                mPhotoFile1, creation.getBusinessDemandID()); //imgBase64
                        if (mPhotoFile1 == null)
                            dealerRegister.setUserImage(creation.getUserImage());
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        Gson gson = new Gson();
                        Log.e("modelinKYC...", gson.toJson(dealerRegister));
                    }
                    else{
                        Log.e("Demandid...", creation.getBusinessDemandWithCust().toString());
                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                prefManager.getCustId().get(CUST_TYPE), selectSubIdList, creation.getBusinessTypeWithCust(),
                                edOwnerName.getText().toString(), "", edMobileNo.getText().toString(),
                                edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString(), "", checkbox1.isChecked(),
                                mPhotoFile1, creation.getBusinessDemandID(),creation.getBusinessDemandWithCust()); //imgBase64
                        if (mPhotoFile1 == null)
                            dealerRegister.setUserImage(creation.getUserImage());
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        Gson gson = new Gson();
                        Log.e("modelinKYC...", gson.toJson(dealerRegister));
                    }

                   // }
                }
            }


        }
              return value;
    }



    /**
     * ---------------Initializing OnCLick Listeners-------------*
     * */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /* ---method that is Called When User Clicks On "Next" Btn----*/
            case R.id.btnNext:
                callToBillFragment.callingBillingFragment(pos);

               /* if (businessDemandAdaper.getResultList().size()==0)
                {
                    CustomToast.showToast(getActivity(),"Please select business demand");
                }
                else{
                    if(savedata())
                        callToBillFragment.callingBillingFragment(pos);
                }*/
                break;


            case R.id.edMCategoryName:
                /* ---method that is Called When User Clicks On "Select Product" or "Select Service" EditText ----*/
                getSubcategoryDialogue();
                break;


            case R.id.edFCategoryName:
                mDialogMainCategory = new Dialog(getActivity());
                mDialogMainCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogMainCategory.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                mDialogMainCategory.setContentView(R.layout.main_product_list);
                mDialogMainCategory.setCancelable(true);
                edProduct = (EditText) mDialogMainCategory.findViewById(R.id.edState);

                recyclerViewMain = (RecyclerView) mDialogMainCategory.findViewById(R.id.recyclerCategoryMain);
                recyclerViewMain.setHasFixedSize(true);
                recyclerViewMain.setLayoutManager(new LinearLayoutManager(getActivity()));
                imageView = (ImageView) mDialogMainCategory.findViewById(R.id.cancel_category);
                btnMain = (Button) mDialogMainCategory.findViewById(R.id.main_submit);
                mDialogMainCategory.show();
                btnMain.setOnClickListener(this);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogMainCategory.dismiss();
                    }
                });

                edProduct.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        try {
                            childProductAdapter.getFilter().filter(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        try {
                            childProductAdapter.getFilter().filter(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;



            case R.id.sub_submit:
                hsv.setVisibility(View.VISIBLE);

                for (int i = 0; i < subCategoryAdapter.getResultList().size(); i++) {

                    SubCategoryProduct childCategoryProduct = subCategoryAdapter.getResultList().get(i);
                    selectSubIdList.add(new SubCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                            childCategoryProduct.getCategoryProductId(),
                            childCategoryProduct.getSubCategoryId(),
                            childCategoryProduct.getSubCategoryName(),true));
                }

                LinkedHashSet<SubCategoryProduct> listRepatedArryList = new LinkedHashSet<SubCategoryProduct>();
                listRepatedArryList.addAll(selectSubIdList);
                selectSubIdList.clear();
                selectSubIdList.addAll(listRepatedArryList);
                mDialogSubCategory.dismiss();
                if (selectSubIdList.size()!=0 | !selectSubIdList.isEmpty())
                {
                    callChip(selectSubIdList);
                    getAlert();
                }
                Log.e("finaal....",selectSubIdList.toString());
                break;

            case R.id.btnSubmit:
                //sendPhotoTOServer();
                UploadProfilePhoto();
                break;
            case R.id.imag1:
                imgCount = 0;
                selectImage();
                break;

//            case R.id.imag1:
//                imgCount = 0;
//                selectImage();
//                break;

            case R.id.imag2:
                imgCount = 1;
                selectImage();
                break;



            case R.id.btnPhoto1:
                mdialogPhoto.dismiss();
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            File photoFile1 = null;
            try {
                if (imgCount==0)
                {
                    photoFile = createImageFile();
                }else
                {
                    photoFile1 = createImageFile();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (imgCount==0)
            {
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile);
                    mPhotoFile1 = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(requireActivity(), (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));

                }
            }else
            {
                if (photoFile1!=null)
                {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile1);
                    mPhotoFile2 = photoFile1;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(requireActivity(), (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));
                }

            }

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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }
    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
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

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);

    }

    private void UploadProfilePhoto(){
        mdialogPhoto =  new Dialog(getContext());
        mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialogPhoto.setContentView(R.layout.dialogue_profile_photo);
        mdialogPhoto.setCancelable(false);
        mdialogPhoto.show();
        imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
        btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
        btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
        imgPhoto1.setOnClickListener(MessagesList.this);
        btnPhoto.setOnClickListener(MessagesList.this);
        btnPhoto1.setOnClickListener(MessagesList.this);
    }
    private void sendPhotoTOServer() {

        builder1 = new AlertDialog.Builder(getContext()).setMessage(R.string.photo);
        builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mdialogPhoto =  new Dialog(getContext());
                mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialogPhoto.setContentView(R.layout.dialog_photo);
                mdialogPhoto.setCancelable(false);
                mdialogPhoto.show();
                imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
                imgPhoto2 = mdialogPhoto.findViewById(R.id.imag2);
                btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
                btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
                imgPhoto1.setOnClickListener(MessagesList.this);
                imgPhoto2.setOnClickListener(MessagesList.this);
                btnPhoto.setOnClickListener(MessagesList.this);
                btnPhoto1.setOnClickListener(MessagesList.this);

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }

        });
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }

    private List<SubCategoryProduct> GetUpdatedSubList() {
        List<SubCategoryProduct> adapters = new ArrayList<>();
        selectSubIdList.clear();
        if (FinalSelectedSubList.size()!=0)
        {
            for (SubCategoryProduct categoryProduct : FinalSelectedSubList)
            {
                if (categoryProduct.isChecked())
                {
                    adapters.add(new SubCategoryProduct(categoryProduct.getCustID(),
                            categoryProduct.getCategoryProductId(),categoryProduct.getSubCategoryId(), categoryProduct.getSubCategoryName(), true));
                }
            }
        }
        return adapters;
    }



    private void getAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setCancelable(false);
        if (isprofessional==true)
        {
            builder1.setMessage("Would you like to add more services?");
        }else
        {
            builder1.setMessage("Would you like to add more categories?");
        }
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getSubcategoryDialogue();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    private void callChip(List<SubCategoryProduct> selectSubIdList) {

        if (chipGroupDemo.getChildCount() > 0) {
            chipGroupDemo.removeAllViews();
        }

        List<SubCategoryProduct> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (SubCategoryProduct category : selectSubIdList) {
            if (i == 0) {
                withoutDuplicate.add(category);

            } else {
                int k =0;
                for (SubCategoryProduct cat : withoutDuplicate) {
                    if (cat.getSubCategoryId() == category.getSubCategoryId()) {
                        k=1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(category);
                }
            }
            i++;

        }


        for (SubCategoryProduct category : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_item, chipGroupDemo, false);
            mChip.findViewById(R.id.chips);
            mChip.setText(category.getSubCategoryName());
            Chip finalChip= mChip;
            mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    category.setChecked(false);
                    chipGroupDemo.removeView(finalChip);

                }
            });
            chipGroupDemo.addView(mChip);
        }
        FinalSelectedSubList=withoutDuplicate;
        Log.e("Total","count...."+withoutDuplicate.toString());
    }


    private void getSearchSubCategoryMethodToServer(String searchChildCategory) {
        edMain.getText().clear();
        //ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(getActivity());
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getActivity(),"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("is professionals","....."+isprofessional);
        Gson gson=new Gson();
        Log.e("model..",gson.toJson(searchChildCategory));
        Log.e("custid...",String.valueOf(CustID));
        Call<List<SubCategoryProduct>> call = customer_interface.getSubCategoryChildCategoryList("bearer "+Token,searchChildCategory, CustID,isprofessional);
        call.enqueue(new Callback<List<SubCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<SubCategoryProduct>> call, Response<List<SubCategoryProduct>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        subCategoryProducts = new ArrayList<SubCategoryProduct>(response.body());
                        if (subCategoryProducts.size()!=0)
                        {
                            subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryProducts, MessagesList.this);
                            recyclerViewMother.setAdapter(subCategoryAdapter);
                            subCategoryAdapter.notifyDataSetChanged();
                            edMain.setEnabled(true);
                            checkBoxSubCategory.setVisibility(View.VISIBLE);
                            MyListAdapter adapter = new MyListAdapter(getActivity(), selectSubIdList);
                            recyclerVieww.setAdapter(adapter);
                            btnSub.setVisibility(View.VISIBLE);
                        }else
                        {
                            //do nothing
                            CustomToast.showToast(getActivity(),"Not found");
                            btnSub.setVisibility(View.GONE);
                        }

                        break;

                    case 404:
                        progressDialog.dismiss();
                        btnSub.setVisibility(View.GONE);
                        break;


                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<SubCategoryProduct>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ","error..."+t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                }
                else {
                    CustomToast.showToast(getActivity(),"Bad response from server.. Try again later ");
                }

            }
        });

    }


    @Override
    public void onItemSelected(SubCategoryProduct subProduct) {

        chipScroll.setVisibility(View.VISIBLE);
        ChipGroup chipGroupDemoo = mDialogSubCategory.findViewById(R.id.chipgroup);
        Chip mChipp = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_fragment, chipGroupDemoo, false);
        mChipp.findViewById(R.id.chips);

        if (subProduct.isChecked()) {
            mChipp.setText(subProduct.getSubCategoryName());

            Chip finalMChipp = mChipp;
            mChipp.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    subProduct.setChecked(false);
                    chipGroupDemoo.removeView(finalMChipp);
                }
            });
            chipGroupDemoo.addView(mChipp);

        } else {
            for (int i = 0; i < chipGroupDemoo.getChildCount(); i++) {
                mChipp = (Chip) chipGroupDemoo.getChildAt(i);


                if (mChipp.getText().toString().equals(subProduct.getSubCategoryName())) {

                    if (!subProduct.isChecked()) {
                        subProduct.setChecked(false);
                        chipGroupDemoo.removeView(mChipp);
                    }
                }

            }
        }
    }



    public void getSubcategoryDialogue() {
    /*    if (businessTypeAdapter.getTrueResultList().size()==0)
        {
            CustomToast.showToast(getActivity(),"Please select business type");
        }else
        {

            mDialogSubCategory = new Dialog(getActivity());
            mDialogSubCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogSubCategory.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogSubCategory.setContentView(R.layout.sub_product_list);
            mDialogSubCategory.setCancelable(true);
            edProduct = (EditText) mDialogSubCategory.findViewById(R.id.edState);
            if (isprofessional)
            {
                edProduct.setHint("Search Service");
            }else
            {
                edProduct.setHint("Search Product");
            }
            btnSearch = (Button) mDialogSubCategory.findViewById(R.id.btnSearch);
            Clear_txt_btn = mDialogSubCategory.findViewById(R.id.clear_txt_Prise);
            checkBoxSubCategory = (CheckBox) mDialogSubCategory.findViewById(R.id.checkboxSubCategory);
            recyclerViewMother = (RecyclerView) mDialogSubCategory.findViewById(R.id.recyclerCategory);

            recyclerViewMother.setLayoutManager(new LinearLayoutManager(getActivity()));
            imageView = (ImageView) mDialogSubCategory.findViewById(R.id.cancel_category);

            btnSub = (Button) mDialogSubCategory.findViewById(R.id.sub_submit);
            chipScroll = (ScrollView) mDialogSubCategory.findViewById(R.id.chipScrollView);

            mDialogSubCategory.show();
            subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryProducts, UpdateCustomer.this);
            btnSub.setOnClickListener(this);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogSubCategory.dismiss();
                }
            });
            Clear_txt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!edProduct.getText().toString().isEmpty())
                    {
                        edProduct.getText().clear();
                        Clear_txt_btn.setVisibility(View.GONE);

                    }

                }
            });
            checkBoxSubCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (checkBoxSubCategory.isChecked()) {
                            for (SubCategoryProduct subCategoryProduct : subCategoryProducts) {
                                subCategoryProduct.setChecked(true);
                                onItemSelected(subCategoryProduct);

                            }
                        } else {
                            for (SubCategoryProduct subCategoryProduct : subCategoryProducts) {
                                subCategoryProduct.setChecked(false);
                                onItemSelected(subCategoryProduct);
                            }
                        }
                        subCategoryAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (edProduct.getText().toString().isEmpty()) {
                        CustomToast.showToast(getActivity(), "Enter Child Category Name");
                        btnSub.setVisibility(View.GONE);

                    } else {
                        getSearchSubCategoryMethodToServer(edProduct.getText().toString().trim());

                    }
                }
            });
            edProduct.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        subCategoryAdapter.getFilter().filter(s);
                        if (s.length() <=0) {

                            Clear_txt_btn.setVisibility(View.GONE);
                        } else {
                            Clear_txt_btn.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        subCategoryAdapter.getFilter().filter(s);
                        if (s.length()<=0) {

                            Clear_txt_btn.setVisibility(View.GONE);
                        } else {
                            Clear_txt_btn.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }


*/
    }


    private boolean ValidateOwnwername() {
        String ownName = edOwnerName.getText().toString();

        if (ownName.isEmpty()) {
            edOwnerName.setError("Please enter ownername");
            CustomToast.showToast(getContext(), "Please enter ownername");
            return false;
        }
        else if (ownName.startsWith(" "))
        {
            edOwnerName.setError("Please enter proper ownername");
            CustomToast.showToast(getContext(), "Please enter proper ownername");
            return  false;
        }
        else {
            return true;
           /* if (!ownName.matches(regFname)) {
                edOwnerName.setError("please enter proper owner name");
                return false;
            } else {
                return true;
            }*/
        }
    }

    private boolean ValidateFirmname() {
        String ownName = edFirmName.getText().toString();

        if (ownName.isEmpty()) {
            edFirmName.setError("Please enter Firmname");
            CustomToast.showToast(getContext(), "Please enter Firmname");
            return false;
        } else if (ownName.startsWith(" "))
        {
            edFirmName.setError("Please enter proper Firmname");
            CustomToast.showToast(getContext(), "Please enter proper Firmname");
            return  false;
        } else {
            return true;
            /*if(!ownName.matches(regFname)){
                edOwnerName.setError("please enter proper owner name");
                return  false;
            } else {
                return true;
            }*/
        }
    }

    private boolean ValidateEmail() {
        String email = edEmailId.getText().toString();

        if (!(email.matches(emailPattern) || email.matches(yahooPattern))) {
            edEmailId.setError("Please enter valid email");
            CustomToast.showToast(getContext(), "Please enter valid email");
            return false;
        } else {
            return true;
        }
    }


    private boolean ValidateProducts() {
        if (selectSubIdList.isEmpty() | selectSubIdList.size() == 0 ) {
            CustomToast.showToast(getActivity(), "Please select products");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateBusiness() {
        if (businessTypeAdapter.getTrueResultList().size() == 0 | businessTypeAdapter.getTrueResultList().isEmpty()) {
            CustomToast.showToast(getActivity(), "Please select business type");
            return false;
        } else {
            return true;
        }
    }


    private boolean ValidateMobileNumber() {
        String NumberInput = edMobileNo.getText().toString();

        if (!PHONE_PATTERN.matcher(NumberInput).matches()) {
            edMobileNo.setError("please enter valid mobile number");
            return false;
        } else if (NumberInput.isEmpty()) {
            edMobileNo.setError("Please enter valid mobile number");
            return false;
        } else {

            if (!NumberInput.matches(regNumber)) {
                edMobileNo.setError("please enter valid mobile number");
                return false;
            } else {
                return true;
            }

        }
    }


    public interface CallToBillFragment {

        void callingBillingFragment(int pos);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("navigation","OnPause calleddddd");
       // savedata();
    }
}
