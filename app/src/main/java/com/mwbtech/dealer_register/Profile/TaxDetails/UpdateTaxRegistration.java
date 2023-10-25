package com.mwbtech.dealer_register.Profile.TaxDetails;

import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.creation;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.dealerRegister;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.isUpdate;
import static com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity.prefManager;
import static com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity.updateDealerRegister;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTaxRegistration extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnNext, btnExit;
    String Token;
    private UpdateTaxRegistration taxRegistrationActivity;
    EditText tvGST,tvPan,tvReGST,tvRePan;

    Customer_Interface customer_interface;
    // public boolean saveData =false;

    View taxView;
    UpdateMainActivity mainActivity;
    CallToBankFragment callToBankFragment;
    //CallToBillFragment callToBillFragment;
    int pos = 3;

    Spinner spTax;
    String[] tax = {"Select Taxation Type","Registered","Unregistered"};

    ArrayAdapter taxAdapter;
    String taxType;

    File imgFile;

    final String pan_pattern = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
//    public static final String GST_pattern = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}";
public static final String GST_pattern = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if(activity instanceof CallToBankFragment) {
                callToBankFragment = (CallToBankFragment) activity;
            }else {
                throw new RuntimeException(activity.toString()
                        + " must implement OnGreenFragmentListener");
            }
        }catch (ClassCastException e){

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taxView = inflater.inflate(R.layout.tax_registration, null);
        mainActivity = (UpdateMainActivity) this.getActivity();
        spTax = taxView.findViewById(R.id.spinnerTax);

       // prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome", "customer", DealerRegister.class);

        btnNext = taxView.findViewById(R.id.btnNext);
        btnExit = taxView.findViewById(R.id.btnExit);

        tvGST = taxView.findViewById(R.id.gstNumber);
        tvPan = taxView.findViewById(R.id.PanNumber);
        tvReGST = taxView.findViewById(R.id.regstNumber);
        tvRePan = taxView.findViewById(R.id.rePanNumber);

        tvGST.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),new InputFilter.AllCaps()});
        tvPan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10),new InputFilter.AllCaps()});
        tvReGST.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),new InputFilter.AllCaps()});
        tvRePan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10),new InputFilter.AllCaps()});

        taxAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, tax){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(18);
                return view;
            }
        };
        taxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTax.setAdapter(taxAdapter);
        spTax.setOnItemSelectedListener(this);

        sharePrefernceData();
        Token = prefManager.getToken().get(TOKEN);
        btnNext.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        return taxView;
    }



    private void sharePrefernceData() {
        try {
            if(creation == null){
                saveBankData();
                prefManager.saveObjectToSharedPreference("customer", creation);
            }else {

                creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                if (creation.getRegistrationType() != null) {
                    if (creation.getRegistrationType().equals("Select Taxation Type")) {
                        spTax.setSelection(0);
                    } else if (creation.getRegistrationType().equals("Registered")) {
                        spTax.setSelection(1);
                    } else {
                        spTax.setSelection(2);
                    }

                    if (!TextUtils.isEmpty(creation.getTinNumber()))
                    {
                        tvGST.setText("" + creation.getTinNumber());
                        tvReGST.setText("" + creation.getTinNumber());
                    }else
                    {
                        tvGST.setText("");
                        tvReGST.setText("");
                    }
                    if (!TextUtils.isEmpty(creation.getPanNumber()))
                    {
                        tvPan.setText("" + creation.getPanNumber());
                        tvRePan.setText("" + creation.getPanNumber());
                    }else
                    {
                        tvPan.setText("");
                        tvRePan.setText("");
                    }



                } else {
                    if (updateDealerRegister.getRegistrationType().equals("Select Taxation Type")) {
                        spTax.setSelection(0);
                    } else if (updateDealerRegister.getRegistrationType().equals("Registered")) {
                        spTax.setSelection(1);
                    } else {
                        spTax.setSelection(2);
                    }
                    if (!TextUtils.isEmpty(updateDealerRegister.getTinNumber()))
                    {
                        tvGST.setText("" + updateDealerRegister.getTinNumber());
                        tvReGST.setText("" + updateDealerRegister.getTinNumber());
                    }else
                    {
                        tvGST.setText("");
                        tvReGST.setText("");
                    }
                    if (!TextUtils.isEmpty(updateDealerRegister.getPanNumber()))
                    {
                        tvPan.setText("" + updateDealerRegister.getPanNumber());
                        tvRePan.setText("" + updateDealerRegister.getPanNumber());
                    }else
                    {
                        tvPan.setText("");
                        tvRePan.setText("");
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharePrefernceData();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnNext) {
            if (SaveData(false))
                callToBankFragment.callingBankingFragment(pos);
        }
        if(v.getId()==R.id.btnExit){
            SaveData(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        taxType = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private boolean ValidateGST()
    {
        String input = tvGST.getText().toString();
        if (!input.matches(GST_pattern))
        {
            tvGST.setError("Please enter valid GST number");
            CustomToast.showToast(getActivity(),"Please enter valid GST number");
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean ValidateReGST()
    {
        String input = tvReGST.getText().toString();
        if (!input.matches(GST_pattern))
        {
            tvReGST.setError("Please enter valid GST number");
            //CustomToast.showToast(getActivity(),"GST number and Re-enter GST number does not match");
            return false;
        }else if (!tvGST.getText().toString().trim().equals(tvReGST.getText().toString().trim()))
        {
            tvReGST.setError("GST number and Re-enter GST number does not match");
            //Toast.makeText(getContext(), "GST number and Re-enter GST number does not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean ValidatePAN()
    {
        String nameInput = tvPan.getText().toString();

        if (!nameInput.matches(pan_pattern))
        {
            tvPan.setError("Please enter valid Pan number");
            //CustomToast.showToast(getActivity(),"Please enter valid Pan number");
            return false;
        }else
        {
            return true;
        }

    }

    private boolean ValidateRePAN()
    {
        String nameInput = tvRePan.getText().toString();

        if (!nameInput.matches(pan_pattern))
        {
            tvRePan.setError("Please enter valid Pan number");
            //CustomToast.showToast(getActivity(),"PAN number and Re-enter PAN number does not match");
            return false;
        }else if (!tvPan.getText().toString().trim().equals(tvRePan.getText().toString().trim()))
        {
            tvRePan.setError("PAN number and Re-enter PAN number does not match");
            //Toast.makeText(getContext(), "PAN number and Re-enter PAN number does not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }

    }

    public void onBackClicked() {
        callToBankFragment.callingBankingFragment(1);
    }


    public interface CallToBankFragment {
        void callingBankingFragment(int pos);
    }

    public boolean SaveData(boolean exit) {

            boolean saveData ;
            if (taxType.equals("Select Taxation Type"))
            {
                saveData = false;
                Toast.makeText(mainActivity, "Please select taxation type", Toast.LENGTH_SHORT).show();
            } else {
                if (taxType.equals("Registered")) {
                    if (!ValidateGST() | !ValidatePAN() | !ValidateReGST() | !ValidateRePAN()) {
                        saveData = false;
                        return saveData;
                    } else {
                        creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                creation.getFirmName(), creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                creation.getTelephoneNumber(), creation.getShopImage(), creation.getBillingAddress(),
                                creation.getArea(), creation.getCity(), creation.getCityCode(), creation.getState(),
                                creation.getPincode(), creation.getLattitude(), creation.getLangitude(), creation.getInterstCity(),
                                creation.getInterstState(), creation.getIsProfessional(), creation.getInterstCountry(), taxType, tvGST.getText().toString(),
                                tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                        if (creation.getUserImageFile() == null) {
                            Log.e("TAX", "new image is not selected");
                            dealerRegister.setUserImage(creation.getUserImage());
                        } else {
                            dealerRegister.setUserImageFile(creation.getUserImageFile());
                            Log.e("TAX", "new image is selected");

                        }
                        saveBankData();
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        saveData = true;
                        if(exit){
                              imgFile=dealerRegister.getUserImageFile();
                              sendCustomerDetails(creation.getCustID(), dealerRegister);
                        }
                        // callToBankFragment.callingBankingFragment(pos);
                    }
                }
                else {
                    if (!tvGST.getText().toString().isEmpty()) {
                        if (!ValidateGST() | !ValidateReGST()) {
                            saveData = false;
                            return saveData;
                        }
                        else {
                            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                            dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                    creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                                    creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(),
                                    creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                    creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                    creation.getTelephoneNumber(), creation.getShopImage(),
                                    creation.getBillingAddress(), creation.getArea(),
                                    creation.getCity(), creation.getCityCode(),
                                    creation.getState(), creation.getPincode(),
                                    creation.getLattitude(), creation.getLangitude(),
                                    creation.getInterstCity(), creation.getInterstState(),creation.getIsProfessional(),
                                    creation.getInterstCountry(), taxType, tvGST.getText().toString(),
                                    tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                            if(creation.getUserImageFile()==null){
                                Log.e("TAX", "new image is not selected");
                                dealerRegister.setUserImage(creation.getUserImage());
                            } else {
                                dealerRegister.setUserImageFile(creation.getUserImageFile());
                                Log.e("TAX", "new image is selected");

                            }


                            saveBankData();
                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                            saveData = true;
                            if(exit){
                                imgFile=dealerRegister.getUserImageFile();
                                sendCustomerDetails(creation.getCustID(), dealerRegister);
                            }
                            // callToBankFragment.callingBankingFragment(pos);
                        }
                    }
                    else if (!tvPan.getText().toString().isEmpty()) {
                        if (!ValidatePAN() | !ValidateRePAN()) {
                            saveData = false;
                            return saveData;
                        } else {

                            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                            dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                    creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                                    creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(),
                                    creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                    creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(),
                                    creation.getShopImage(), creation.getBillingAddress(), creation.getArea(), creation.getCity(),
                                    creation.getCityCode(), creation.getState(), creation.getPincode(), creation.getLattitude(),
                                    creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(),creation.getIsProfessional(),
                                    creation.getInterstCountry(), taxType, tvGST.getText().toString(), tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                            if(creation.getUserImageFile()==null){
                                Log.e("TAX", "new image is not selected");
                                dealerRegister.setUserImage(creation.getUserImage());
                            } else {
                                dealerRegister.setUserImageFile(creation.getUserImageFile());
                                Log.e("TAX", "new image is selected");

                            }

                            saveBankData();
                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                            saveData = true;
                            if(exit){
                                imgFile=dealerRegister.getUserImageFile();
                                sendCustomerDetails(creation.getCustID(), dealerRegister);
                            }
                            // callToBankFragment.callingBankingFragment(pos);
                        }
                    }
                    else {
                        creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                creation.getFirmName(), creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(),
                                creation.getShopImage(), creation.getBillingAddress(), creation.getArea(), creation.getCity(),
                                creation.getCityCode(), creation.getState(), creation.getPincode(), creation.getLattitude(),
                                creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(),creation.getIsProfessional(),  creation.getInterstCountry(),
                                taxType, tvGST.getText().toString(), tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                        if (creation.getUserImageFile() == null) {
                            Log.e("TAX", "new image is not selected");
                            dealerRegister.setUserImage(creation.getUserImage());
                        } else {
                            dealerRegister.setUserImageFile(creation.getUserImageFile());
                            Log.e("TAX", "new image is selected");

                        }
                        saveBankData();
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        saveData = true;
                        if(exit){
                            imgFile=dealerRegister.getUserImageFile();
                            sendCustomerDetails(creation.getCustID(), dealerRegister);
                        }

                        //callToBankFragment.callingBankingFragment(pos);
                    }
                }
            }

        return saveData;
    }

    private void saveBankData() {

        dealerRegister.setAccountnumber(creation.getAccountnumber());
        dealerRegister.setiFSCCode(creation.getiFSCCode());
        dealerRegister.setBankCity(creation.getBankCity());
        dealerRegister.setBankname(creation.getBankname());
        dealerRegister.setBankBranchName(creation.getBankBranchName());
    }

    private void sendCustomerDetails(Integer custID, DealerRegister dealerRegister) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(),"Please wait");
        //progressDialog.setMessage("Please wait..."); // Setting Message
        //progressDialog.setTitle("Profile"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        MultipartBody.Part body = null,body1 = null;
        // File file1,file2;
        RequestBody requestBody1,requestBody2;
        try {
            //imgFile=dealerRegister.getUserImageFile();
            //file1 = new File(ImagePath);
            requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"),imgFile );
            body = MultipartBody.Part.createFormData("picture", imgFile.getName(), requestBody1);

        }catch (Exception e){
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String json = gson.toJson(dealerRegister);
        Log.e("json.......",json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.updateDealerRegister("bearer " + Token, dealerRegister);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        Log.e("insideAPI..",String.valueOf(statusCode));
                        progressDialog.dismiss();
                        isUpdate = true;
                        CustomToast.showToast(getActivity(), "Successfully updated");
                        Intent intent = new Intent(getActivity(), DashboardActivity.class).putExtra("isNewUser", false);
                        startActivity(intent);
                        break;

                    case 404:
                        CustomToast.showToast(getActivity(), "Invalid pincode");
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;
                    case 400:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Request not granted");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;
                }

            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Failure", "..........." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });
    }
}





