package com.mwbtech.dealer_register.LoginRegister.ValidateUserForFirstTime;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.LoginRegister.RegisterActivity;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.LoginCustDetailsModel;
import com.mwbtech.dealer_register.PojoClass.QuestionAnswerObject;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAnswerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^" +"(?=.*[0-9])"+ ".{10}" + "$");
    String ValidNumber = "^[7-9][0-9]{9}$";
    EditText EdMobileNumber;
    RelativeLayout QuestionLayout,MobileLayout;
    Button NextButton;
    TextView txt_Question,txtQuestion_hindi,txtAns1,txtAns2,txtAns3,txtAns4;
    CheckBox OptionA_Radio,OptionB_Radio,OptionC_Radio,OptionD_Radio;
    Button Submit_AnswerBtn;
    Customer_Interface customer_interface;
    String Question,OptionA,OptionB,OptionC,OptionD,AnswerSelected,MobileNumber,QuestionHindi,OptionAHindi,OptionBHindi,OptionCHindi,OptionDHindi;
    int QuestionID;
    boolean IsAnswered = false;
    String prevStarted = "prevStarted";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    AlertDialog.Builder builder;

    String FCMtoken;


    //notification
    NotificationManager mNotificationManager;

    public static int activities_num = 0;
    int key=0;
    int counter=0;
    int LoadQuestionCount = 0;
    private String android_id;


    @Override
    protected void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.FALSE);
            editor.apply();
        } else {
            Intent intent = new Intent(QuestionAnswerActivity.this, LoginActivity.class);
            startActivity(intent);
            QuestionAnswerActivity.this.finish();
        }
    }
    private void CheckForQuestionCounter()
    {
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call function
                if(LoadQuestionCount>=3)
                {
                    if(IsAnswered==false) {
                        SendMobNumToBlock(MobileNumber);
                    }
                }else if(LoadQuestionCount<3){
                    ha.postDelayed(this, 30000);
                    GetQuestionFromServer();
                }
            }
        }, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        builder=new AlertDialog.Builder(this);
        initializeView();
        getFCMtoken();
        initializeAPI();
        initializeClickEvent();
    }
    private void initializeClickEvent() {
        OptionA_Radio.setOnClickListener(this);
        OptionB_Radio.setOnClickListener(this);
        OptionC_Radio.setOnClickListener(this);
        OptionD_Radio.setOnClickListener(this);
        Submit_AnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(AnswerSelected))
                {
                    SendSelectedAnswer(MobileNumber,QuestionID,AnswerSelected);
                }else
                {
                    Toast.makeText(QuestionAnswerActivity.this, "Please select your answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateMobileNumber()){
                    return;
                }else
                {
                    MobileNumber = EdMobileNumber.getText().toString();
                    GetLoginDetailsFromServer(MobileNumber);
                }
            }
        });
    }

    private void GetUserDetailsIfExistorNot(String mobileNumber) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(QuestionAnswerActivity.this,"Please wait");
        Call<Boolean> call = customer_interface.GetuserDetails(mobileNumber);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                int statusCode = response.code();
                switch (statusCode)
                {
                    case 200:
                        progressDialog.dismiss();
                        boolean isExist = response.body();
                        if (isExist)
                        {
                            Intent intent = new Intent(QuestionAnswerActivity.this, LoginActivity.class);
                            startActivity(intent);
                            QuestionAnswerActivity.this.finish();
                            editor.putBoolean(prevStarted, Boolean.TRUE);
                            editor.apply();
                            key=0;
                        }else
                        {
                            MobileLayout.setVisibility(View.GONE);
                            key=1;
                            CheckForQuestionCounter();
                            editor.putBoolean(prevStarted, Boolean.FALSE);
                            editor.apply();
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        MobileLayout.setVisibility(View.GONE);
                        key=1;
                        CheckForQuestionCounter();
                        editor.putBoolean(prevStarted, Boolean.FALSE);
                        editor.apply();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        Toast.makeText(QuestionAnswerActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                        editor.putBoolean(prevStarted, Boolean.FALSE);
                        editor.apply();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                editor.putBoolean(prevStarted, Boolean.FALSE);
                editor.apply();
            }
        });
    }

    private void GetQuestionFromServer() {
        if (IsAnswered == false) {
            ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(QuestionAnswerActivity.this,"Please wait");
            Call<QuestionAnswerObject> call = customer_interface.GetQuestions();
            call.enqueue(new Callback<QuestionAnswerObject>() {
                @Override
                public void onResponse(Call<QuestionAnswerObject> call, Response<QuestionAnswerObject> response) {
                    int status_code = response.code();
                    switch (status_code) {
                        case 200:
                            progressDialog.dismiss();
                            LoadQuestionCount = LoadQuestionCount + 1;
                            QuestionLayout.setVisibility(View.VISIBLE);
                            QuestionAnswerObject questionAnswerObject = response.body();
                            Question = questionAnswerObject.getQuestionName();
                            QuestionID = questionAnswerObject.getQuestionID();
                            OptionA = questionAnswerObject.getOptionA();
                            OptionB = questionAnswerObject.getOptionB();
                            OptionC = questionAnswerObject.getOptionC();
                            OptionD = questionAnswerObject.getOptionD();
                            QuestionHindi = questionAnswerObject.getQuestionNameHindi();
                            OptionAHindi = questionAnswerObject.getOptionAHindi();
                            OptionBHindi = questionAnswerObject.getOptionBHindi();
                            OptionCHindi = questionAnswerObject.getOptionCHindi();
                            OptionDHindi = questionAnswerObject.getOptionDHindi();
                            SetValuesTOUI(Question, OptionA, OptionB, OptionC, OptionD, QuestionHindi, OptionAHindi, OptionBHindi, OptionCHindi, OptionDHindi);
                            OptionA_Radio.setChecked(false);
                            OptionB_Radio.setChecked(false);
                            OptionC_Radio.setChecked(false);
                            OptionD_Radio.setChecked(false);

                            OptionA_Radio.setEnabled(true);
                            OptionB_Radio.setEnabled(true);
                            OptionC_Radio.setEnabled(true);
                            OptionD_Radio.setEnabled(true);
                            editor.putBoolean(prevStarted, Boolean.FALSE);
                            editor.apply();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            MobileLayout.setVisibility(View.VISIBLE);
                            editor.putBoolean(prevStarted, Boolean.FALSE);
                            editor.apply();
                            Toast.makeText(QuestionAnswerActivity.this, "Server Error..", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<QuestionAnswerObject> call, Throwable t) {
                    progressDialog.dismiss();
                    editor.putBoolean(prevStarted, Boolean.FALSE);
                    editor.apply();
                    Toast.makeText(QuestionAnswerActivity.this, "Time out..", Toast.LENGTH_SHORT).show();
                }
            });
        } else{}
    }

    private void initializeView() {
        NextButton = findViewById(R.id.NextBtn);
        QuestionLayout = findViewById(R.id.layout1);
        MobileLayout = findViewById(R.id.layoutNumber);
        EdMobileNumber = findViewById(R.id.MobileNumber);
        txt_Question = findViewById(R.id.question_txt);

        txtAns1 = findViewById(R.id.txt1Hindi);
        txtAns2 = findViewById(R.id.txt2Hindi);
        txtAns3 = findViewById(R.id.txt3Hindi);
        txtAns4 = findViewById(R.id.txt4Hindi);
        txtQuestion_hindi = findViewById(R.id.question_txt_hindi);
        OptionA_Radio = findViewById(R.id.option_a);
        OptionB_Radio = findViewById(R.id.option_b);
        OptionC_Radio = findViewById(R.id.option_c);
        OptionD_Radio = findViewById(R.id.option_d);
        Submit_AnswerBtn = findViewById(R.id.submit);
    }
    private void getFCMtoken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        FCMtoken = task.getResult();
                        String msg = getString(R.string.msg_token_fmt, FCMtoken);
                    }
                });

    }

    private void initializeAPI() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
    }

    private void SetValuesTOUI(String question, String optionA, String optionB, String optionC, String optionD,String questionHindi,
                               String optionAHindi,String optionBHindi,String optionCHindi,String optionDHindi) {

        txt_Question.setText(question);
        txtQuestion_hindi.setText(questionHindi);
        OptionA_Radio.setText(optionA);
        OptionB_Radio.setText(optionB);
        OptionC_Radio.setText(optionC);
        OptionD_Radio.setText(optionD);
        txtAns1.setText(optionAHindi.trim());
        txtAns2.setText(optionBHindi.trim());
        txtAns3.setText(optionCHindi.trim());
        txtAns4.setText(optionDHindi.trim());
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId())
        {
            case R.id.option_a:
                if (checked)
                {
                    AnswerSelected = OptionA_Radio.getText().toString();
                    OptionB_Radio.setEnabled(false);
                    OptionC_Radio.setEnabled(false);
                    OptionD_Radio.setEnabled(false);
                }else
                {
                    OptionB_Radio.setEnabled(true);
                    OptionC_Radio.setEnabled(true);
                    OptionD_Radio.setEnabled(true);
                }
                break;

            case R.id.option_b:
                if (checked)
                {
                    AnswerSelected = OptionB_Radio.getText().toString();
                    OptionA_Radio.setEnabled(false);
                    OptionC_Radio.setEnabled(false);
                    OptionD_Radio.setEnabled(false);
                }else
                {

                    OptionA_Radio.setEnabled(true);
                    OptionC_Radio.setEnabled(true);
                    OptionD_Radio.setEnabled(true);
                }
                break;


            case R.id.option_c:
                if (checked)
                {
                    AnswerSelected = OptionC_Radio.getText().toString();
                    OptionA_Radio.setEnabled(false);
                    OptionB_Radio.setEnabled(false);
                    OptionD_Radio.setEnabled(false);
                }else
                {

                    OptionA_Radio.setEnabled(true);
                    OptionB_Radio.setEnabled(true);
                    OptionD_Radio.setEnabled(true);
                }
                break;

            case R.id.option_d:
                if (checked)
                {
                    AnswerSelected = OptionD_Radio.getText().toString();
                    OptionA_Radio.setEnabled(false);
                    OptionB_Radio.setEnabled(false);
                    OptionC_Radio.setEnabled(false);
                }else
                {

                    OptionA_Radio.setEnabled(true);
                    OptionB_Radio.setEnabled(true);
                    OptionC_Radio.setEnabled(true);
                }
                break;
        }

    }

    private void SendSelectedAnswer(String mobileNumber, int questionID, String answerSelected) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(QuestionAnswerActivity.this,"");
        Call<AccountVerify> call =customer_interface.SendAnswer(FCMtoken,mobileNumber,questionID,answerSelected);
        call.enqueue(new Callback<AccountVerify>() {
            @Override
            public void onResponse(Call<AccountVerify> call, Response<AccountVerify> response) {
                int status_code = response.code();
                switch (status_code)
                {
                    case 200:
                        progressDialog.dismiss();
                        String NumberInput = EdMobileNumber.getText().toString();
                        AccountVerify accountVerify1 = response.body();
                        if (accountVerify1.isQuestionAnswered()==true)
                        {
                            Toast.makeText(QuestionAnswerActivity.this, "Good.! you have selected correct answer", Toast.LENGTH_SHORT).show();
                            ExitApp(0);
                            IsAnswered= true;
                            editor.putBoolean(prevStarted, Boolean.TRUE);
                            editor.apply();
                        }else
                        {
                            Toast.makeText(QuestionAnswerActivity.this, "You have selected wrong answer", Toast.LENGTH_SHORT).show();
                            counter = counter + 1;
                            if(counter==3) {
                                SendMobNumToBlock(mobileNumber);
                            }
                            else{
                                CheckForQuestionCounter();
                            }
                            editor.putBoolean(prevStarted, Boolean.FALSE);
                            editor.apply();
                        }
                        break;

                    case 500:
                        progressDialog.dismiss();
                        editor.putBoolean(prevStarted, Boolean.FALSE);
                        editor.apply();
                        Toast.makeText(QuestionAnswerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        progressDialog.dismiss();
                        editor.putBoolean(prevStarted, Boolean.FALSE);
                        editor.apply();
                        Toast.makeText(QuestionAnswerActivity.this, "3 wrong attempt........", Toast.LENGTH_SHORT).show();
                        SendMobNumToBlock(mobileNumber);
                        break;
                }
            }

            @Override
            public void onFailure(Call<AccountVerify> call, Throwable t) {
                progressDialog.dismiss();
                editor.putBoolean(prevStarted, Boolean.FALSE);
                editor.apply();
                Toast.makeText(QuestionAnswerActivity.this, "Time out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ExitApp(int a) {
        if (a==1)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    QuestionAnswerActivity.this.finish();
                    System.exit(0);
                }
            }, 2000);
        }else
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(QuestionAnswerActivity.this, RegisterActivity.class).putExtra("mobilenumber",MobileNumber);
                    startActivity(intent);
                    QuestionAnswerActivity.this.finish();
                }
            }, 2000);
        }
    }

    private boolean ValidateMobileNumber()
    {
        String NumberInput = EdMobileNumber.getText().toString();

        if (!PHONE_PATTERN.matcher(NumberInput).matches()) {
            EdMobileNumber.setError("please enter valid mobile number");
            return false;
        }else if (NumberInput.isEmpty())
        {
            EdMobileNumber.setError("Please enter valid mobile number");
            return false;
        }else {

            if(!NumberInput.matches(ValidNumber)){
                EdMobileNumber.setError("please enter valid mobile number");
                return  false;
            } else {
                return true;
            }

        }
    }
    private void SendMobNumToBlock(String mobileNumber) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(QuestionAnswerActivity.this,"");
        Call<String> call =customer_interface.SendMobNumToBlock(mobileNumber);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        builder.setMessage("Your attempt to answer failed, so app is blocked for 24hours")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        ExitApp(1);
                                    }
                                })
                                .setNegativeButton("", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Alert");
                        alertDialog.show();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        Toast.makeText(QuestionAnswerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        Toast.makeText(QuestionAnswerActivity.this, "Have reached maximum number of attempt...!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void VarifyForBlockNumber(String mobileNumber) {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(this);
        Call<String> call =customer_interface.CheckForNumBlock(mobileNumber);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        String checkstatus = response.body();
                        if(checkstatus.equals("true"))
                        {
                            GetUserDetailsIfExistorNot(mobileNumber);
                        }
                        else {
                            GetQuestionFromServer();
                            Toast.makeText(QuestionAnswerActivity.this, "Sorry!,You cannot login for 24Hrs", Toast.LENGTH_SHORT).show();
                            ExitApp(1);
                            break;
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        Toast.makeText(QuestionAnswerActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        Toast.makeText(QuestionAnswerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void GetLoginDetailsFromServer(String MobileNumber) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(QuestionAnswerActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<LoginCustDetailsModel> call = customer_interface.GetCustomerByMobileNumber(MobileNumber);
        call.enqueue(new Callback<LoginCustDetailsModel>() {
            @Override
            public void onResponse(Call<LoginCustDetailsModel> call, Response<LoginCustDetailsModel> response) {
                int status_code = response.code();
                switch (status_code)
                {
                    case 200:
                        progressDialog.dismiss();
                        LoginCustDetailsModel LogDetails = response.body();
                        if(LogDetails.getQuestionRequired()==true){
                            if(LogDetails.getBlockstatus()==false) {
                                if (LogDetails.getQuestionAnswered() == true)
                                {
                                    if(LogDetails.getFirmName()==null){
                                        Intent intent = new Intent(QuestionAnswerActivity.this, RegisterActivity.class).putExtra("mobilenumber",MobileNumber);
                                        startActivity(intent);
                                        QuestionAnswerActivity.this.finish();
                                        editor.putBoolean(prevStarted, Boolean.TRUE);
                                        editor.apply();
                                        key = 0;
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(QuestionAnswerActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        QuestionAnswerActivity.this.finish();
                                        editor.putBoolean(prevStarted, Boolean.TRUE);
                                        editor.apply();
                                        key = 0;
                                    }
                                }
                                else {
                                    MobileLayout.setVisibility(View.GONE);
                                    key=1;
                                    CheckForQuestionCounter();
                                    editor.putBoolean(prevStarted, Boolean.FALSE);
                                    editor.apply();
                                }
                            }
                            else
                            {
                                builder.setMessage("Your attempt to answer failed, so app is blocked for 24hours")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setTitle("Alert");
                                alertDialog.show();
                            }
                        }
                        else{
                            Intent intent = new Intent(QuestionAnswerActivity.this, RegisterActivity.class).putExtra("mobilenumber",MobileNumber);
                            startActivity(intent);
                        }

                        break;

                    case 404:
                        progressDialog.dismiss();
                        Toast.makeText(QuestionAnswerActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<LoginCustDetailsModel> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        activities_num--;
        if(activities_num == 0){
        }
    }
}