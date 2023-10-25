package com.mwbtech.dealer_register.Profile.BankDetails;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.creation;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.dealerRegister;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.SessionDialog;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BankDetailsActvity extends Fragment implements View.OnClickListener {


    public Button btnNext;
    public View bankView;
    EditText edBankName, edBranch, edCity, edAccNo, edIfsc;
    Customer_Interface customer_interface;
    final String pan_pattern = "^[A-Z]{4}[0][0-9]{6}$";
    int CustID;
    String Token;
    CallToTaxFragment callToFragment;

    File UserImageFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bankView = inflater.inflate(R.layout.bank_details, null);

        btnNext = bankView.findViewById(R.id.btnNext);
        edBankName = bankView.findViewById(R.id.edBankName);
        edBranch = bankView.findViewById(R.id.edBranch);
        edCity = bankView.findViewById(R.id.edBankCity);
        edAccNo = bankView.findViewById(R.id.edAccountNo);
        edIfsc = bankView.findViewById(R.id.edIFSC);

        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
        btnNext.setOnClickListener(this);
        edIfsc.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11), new InputFilter.AllCaps()});
        sharePrefernceData();
        return bankView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if (activity instanceof CallToTaxFragment) {
                callToFragment = (CallToTaxFragment) activity;
            } else {
                throw new RuntimeException(activity.toString()
                        + " must implement OnGreenFragmentListener");
            }
        } catch (ClassCastException e) {

        }
    }

    private void sharePrefernceData() {
        try {
            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
            if (creation != null) {
                edBankName.setText("" + creation.getBankname());
                edBranch.setText("" + creation.getBankBranchName());
                edCity.setText("" + creation.getBankCity());
                edAccNo.setText("" + creation.getAccountnumber());
                edIfsc.setText("" + creation.getiFSCCode());
            } else {
                edBankName.getText().clear();
                edBranch.getText().clear();
                edCity.getText().clear();
                edAccNo.getText().clear();
                edIfsc.getText().clear();
            }
            UserImageFile=creation.getUserImageFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (!ValidateBankName() | !ValidateBankBranch() | !ValidateBankCityName() | !ValidateBankAccNumber() | !ValidateBankIFSC()) {
            return;
        } else {
            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
//            dealerRegister = new DealerRegister(creation.getCustID(), creation.getFirmName(), creation.getPassword(),
//                    creation.getUserType(), creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(),
//                    creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(), creation.getAdditionalPersonName(),
//                    creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(), creation.getBillingAddress(),
//                    creation.getArea(), creation.getCity(), creation.getCityCode(), creation.getState(), creation.getPincode(),
//                    creation.getLattitude(), creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(),
//                    creation.getInterstCountry(), creation.getRegistrationType(), creation.getTinNumber(), creation.getPanNumber(),
//                    edBankName.getText().toString(), edBranch.getText().toString(), edAccNo.getText().toString(), edIfsc.getText().toString(),
//                    edCity.getText().toString(), salesmanID, creation.getCustID(), 1, creation.getCreatedDate(),creation.isBGSMember());
            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
            sendCustomerDetails(creation.getCustID(), dealerRegister);
        }
    }

    private void sendCustomerDetails(Integer custID, DealerRegister dealerRegister) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..."); // Setting Message
        //progressDialog.setTitle("Profile"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);


        MultipartBody.Part body = null,body1 = null;
        // File file1,file2;
        RequestBody requestBody1,requestBody2;
        try {
            //file1 = new File(ImagePath);
            requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"),UserImageFile );
            body = MultipartBody.Part.createFormData("picture", UserImageFile.getName(), requestBody1);

        }catch (Exception e){
            e.printStackTrace();
        }
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        //Call<DealerRegister> call = customer_interface.updateDealerRegisterDetails("bearer " + Token, custID, dealerRegister,body);
        Call<DealerRegister> call = customer_interface.updateDealerRegister("bearer " + Token, dealerRegister);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Congratulations!! You are now registered on Kalpavriksha App");
                        startActivity(new Intent(getActivity(), LoginActivity.class));
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
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });


    }

    private boolean ValidateBankName() {
        String InputName = edBankName.getText().toString();
        if (InputName.isEmpty()) {
            edBankName.setError("Please enter bank name");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateBankBranch() {
        String InputName = edBranch.getText().toString();
        if (InputName.isEmpty()) {
            edBranch.setError("Please enter bank branch");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateBankCityName() {
        String InputName = edCity.getText().toString();
        if (InputName.isEmpty()) {
            edCity.setError("Please enter bank city name");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateBankAccNumber() {
        String InputName = edAccNo.getText().toString();
        if (InputName.isEmpty()) {
            edAccNo.setError("Please enter bank account number");
            return false;
        }
        else if(InputName.length()<9){
            edAccNo.setError("Please enter valid bank account number");
            return false;
        }else {
            return true;
        }
    }

    private boolean ValidateBankIFSC() {
        String InputName = edIfsc.getText().toString();
        if (!InputName.matches(pan_pattern)) {
            edIfsc.setError("Please enter bank IFSC code");
            return false;
        } else if (InputName.isEmpty()) {
            edIfsc.setError("Please enter bank IFSC code");
            return false;
        } else {
            return true;
        }
    }


    public void onBackClicked() {
        callToFragment.communicateFragment(2);
    }

    public interface CallToTaxFragment {
        void communicateFragment(int pos);
    }

}
