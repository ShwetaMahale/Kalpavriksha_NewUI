package com.mwbtech.dealer_register.Profile.TaxDetails;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.creation;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.dealerRegister;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
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

import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;


public class TaxRegistrationActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnNext;
    EditText tvGST,tvPan,tvReGST,tvRePan;


    View taxView;
    CallToBankFragment callToBankFragment;
   // CallToBillFragment callToBillFragment;
    int pos = 3;

    Spinner spTax;
    String[] tax = {"Select Taxation Type","Registered","Unregistered"};

    ArrayAdapter taxAdapter;
    String taxType;

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


       // prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome", "customer", DealerRegister.class);
        spTax = taxView.findViewById(R.id.spinnerTax);
        btnNext = taxView.findViewById(R.id.btnNext);
        tvGST = taxView.findViewById(R.id.gstNumber);
        tvReGST = taxView.findViewById(R.id.regstNumber);
        tvPan = taxView.findViewById(R.id.PanNumber);
        tvRePan = taxView.findViewById(R.id.rePanNumber);

        tvGST.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),new InputFilter.AllCaps()});
        tvPan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10),new InputFilter.AllCaps()});
        tvReGST.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),new InputFilter.AllCaps()});
        tvRePan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10),new InputFilter.AllCaps()});



        taxAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, tax){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
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

        btnNext.setOnClickListener(this);

        return taxView;
    }


    private void sharePrefernceData() {
        try {
            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
            if (creation.getRegistrationType() != null) {
                if (creation.getRegistrationType().equals("Select Taxation Type")) {
                    spTax.setSelection(0);
                } else if (creation.getRegistrationType().equals("Registered")) {
                    spTax.setSelection(1);
                } else {
                    spTax.setSelection(2);
                }
                tvGST.setText("" + creation.getTinNumber());
                tvReGST.setText("" + creation.getTinNumber());
                tvPan.setText("" + creation.getPanNumber());
                tvRePan.setText("" + creation.getPanNumber());

            } else {
                spTax.setSelection(0);
                tvGST.setText("");
                tvReGST.setText("");
                tvPan.setText("");
                tvRePan.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }




    @Override
    public void onClick(View v) {
        if (taxType.equals("Select Taxation Type"))
        {
            Toast.makeText(getActivity(), "Please select taxation type", Toast.LENGTH_SHORT).show();
        }else
        {
            if (taxType.equals("Registered")) {

                if(!ValidateGST() | !ValidatePAN() | !ValidateReGST() | !ValidateRePAN()) {
                    return;
                }else {

                        creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                creation.getFirmName(), creation.getPassword(), creation.getUserType(),creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(),
                                creation.getBillingAddress(), creation.getArea(), creation.getCity(), creation.getCityCode(), creation.getState(),
                                creation.getPincode(), creation.getLattitude(), creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(), creation.getIsProfessional(),
                                creation.getInterstCountry(), taxType, tvGST.getText().toString(), tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        callToBankFragment.callingBankingFragment(pos);

                }
            }else
            {
                if (!tvGST.getText().toString().isEmpty())
                {
                    if (!ValidateGST() | !ValidateReGST())
                    {
                        return;
                    }else
                    {
                            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                            dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                    creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                                    creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(),
                                    creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                    creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(),
                                    creation.getShopImage(), creation.getBillingAddress(), creation.getArea(), creation.getCity(),
                                    creation.getCityCode(), creation.getState(),creation.getPincode(), creation.getLattitude(),
                                    creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(), creation.getIsProfessional(),
                                    creation.getInterstCountry(), taxType, tvGST.getText().toString(), tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                            callToBankFragment.callingBankingFragment(pos);

                    }
                }else if (!tvPan.getText().toString().isEmpty())
                {
                    if (!ValidatePAN() | !ValidateRePAN())
                    {
                        return;
                    }else
                    {
                            creation = MainActivity.prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                            dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                    creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                                    creation.getSubCategoryTypeWithCust(),  creation.getBusinessTypeWithCust(),
                                    creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                    creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(),
                                    creation.getShopImage(), creation.getBillingAddress(), creation.getArea(), creation.getCity(),
                                    creation.getCityCode(), creation.getState(),creation.getPincode(), creation.getLattitude(),
                                    creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(), creation.getIsProfessional(),
                                    creation.getInterstCountry(), taxType, tvGST.getText().toString(), tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                            callToBankFragment.callingBankingFragment(pos);
                    }
                }else
                {
                        creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                creation.getFirmName(), creation.getPassword(), creation.getUserType(),creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(),
                                creation.getShopImage(), creation.getBillingAddress(), creation.getArea(), creation.getCity(),
                                creation.getCityCode(), creation.getState(), creation.getPincode(), creation.getLattitude(),
                                creation.getLangitude(), creation.getInterstCity(), creation.getInterstState(),  creation.getIsProfessional(), creation.getInterstCountry(),
                                taxType, tvGST.getText().toString(), tvPan.getText().toString(),creation.getBusinessDemandID(),creation.getBusinessDemandWithCust(),creation.getInterstDistrict());
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        callToBankFragment.callingBankingFragment(pos);

                }
            }
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
            tvReGST.setError("GST number and Re-enter GST number does not match");
           // CustomToast.showToast(getActivity(),"Please enter valid GST number");
            return false;
        }else if (!tvGST.getText().toString().trim().equals(tvReGST.getText().toString().trim()))
        {
            Toast.makeText(getContext(), "GST number and Re-enter GST number does not match", Toast.LENGTH_SHORT).show();
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
            tvRePan.setError("PAN number and Re-enter PAN number does not match");
            //CustomToast.showToast(getActivity(),"Please enter valid Pan number");
            return false;
        }else if (!tvPan.getText().toString().trim().equals(tvRePan.getText().toString().trim()))
        {
            Toast.makeText(getContext(), "PAN number and Re-enter PAN number does not match", Toast.LENGTH_SHORT).show();
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

}
