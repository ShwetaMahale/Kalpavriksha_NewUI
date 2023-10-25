package com.mwbtech.dealer_register.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.SlotBookImages;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrefManager {


    public static String CUST_ID = "cust_id";
    public static String ACUST_ID = "cust_id";
    public static String CUST_TYPE = "cust_type";
    public static String CUST_PASSWORD = "cust_pwd";
    public static String CUST_PHONE = "cust_phone";
    public static String TOKEN = "token";
    //public static Text TOKEN1 = null;
    public static String TOKEN_NAME = "token_name";
    public static String STATE_ID = "state_id";
    public static String STATE_NAME = "state_name";
    public static String CITY_ID = "city_id";
    public static String CITY_NAME = "city_name";
    public static String PROD_ID = "prod_id";
    public static String PINCODE = "pincode";
    public static String PROD_NAME = "prod_name";
    public static String C_NAME = "customer_name";
    public static String BTYPELIST = "business_type";
    public static String BIOMETRIC_ENABLE = "BIOMETRIC_ENABLE";
    public static String BIOMETRIC_REGISTRATION_ASKED = "IS_FIRST_LOGIN";
    public static boolean Save_login = false;
    public static boolean Save_isBannerImages = false;
    public static List<SlotBookImages> images = new ArrayList<>();
    public static List<SlotBookImages> textAD = new ArrayList<>();
    public static List<BusinessType> businessTypes = new ArrayList<>();
    public static String TandCVersion = "tandcversion";

    public static String NEWS_ARTICLES = "NEWS_ARTICLES";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String SAVE_LOGIN = "save_login";
    public static String Save_Language = "save_language";

    public static Boolean IsRegistered = false;

    SharedPreferences pref, pref1, preferences;

    //    SharedPreferences.Editor editor1;
//    SharedPreferences.Editor editor2;
//    SharedPreferences.Editor editor4;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;


    // Shared preferences file name
    private static final String PREF_NAME = "mwb-dealer_register";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref1 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        preferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);


//        editor1 = pref.edit();
//        editor2 = pref1.edit();
//        editor4 = preferences.edit();
    }

    public void savelog(String key, boolean defValue) {
    }

    public void SaveToken(String Token) {
        preferences.edit().putString(TOKEN, Token).apply();
    }

    public void SaveTandCversion(String Tandcversion) {
        preferences.edit().putString(TandCVersion, Tandcversion).apply();
    }

    public HashMap<String, String> getTandcversion() {
        HashMap<String, String> tandcversion = new HashMap<String, String>();
        tandcversion.put(TandCVersion, preferences.getString(TandCVersion, null));
        //token.put(TOKEN_NAME,preferences.getString(TOKEN_NAME,null));
        return tandcversion;
    }
    public void SaveLangauge(String language) {
        preferences.edit().putString(Save_Language, language).apply();
    }

    public HashMap<String, String> getLanguage() {
        HashMap<String, String> languageSelected = new HashMap<String, String>();
        languageSelected.put(Save_Language, preferences.getString(Save_Language, null));
        return languageSelected;
    }

    public HashMap<String, String> getToken() {
        HashMap<String, String> token = new HashMap<String, String>();
        token.put(TOKEN, preferences.getString(TOKEN, null));
//        token.put(TOKEN_NAME, preferences.getString(TOKEN_NAME, null));
        return token;
    }

    public void saveCustId(int custId, String password, int usertype, String phone) {
        pref.edit().putString(CUST_ID, String.valueOf(custId)).apply();
        pref.edit().putString(CUST_PASSWORD, password).apply();
        pref.edit().putString(CUST_TYPE, String.valueOf(usertype)).apply();
        pref.edit().putString(CUST_PHONE, String.valueOf(phone)).apply();
    }

    public void saveCustPincode(String pincode) {
        pref.edit().putString(PINCODE, pincode).apply();
    }

    public void saveLoginDetails(int cityId, String cityName, int stateId, String stateName,
                                 int prodId, String prodName, String custName) {
        pref.edit().putString(CITY_ID, String.valueOf(cityId)).apply();
        pref.edit().putString(CITY_NAME, cityName).apply();
        pref.edit().putString(STATE_ID, String.valueOf(stateId)).apply();
        pref.edit().putString(STATE_NAME, stateName).apply();
        pref.edit().putString(PROD_ID, String.valueOf(prodId)).apply();
        pref.edit().putString(PROD_NAME, prodName).apply();
        pref.edit().putString(C_NAME, custName).apply();
    }


    public HashMap<String, String> getsaveLoginDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(CUST_ID, pref.getString(CUST_ID, null));
        user.put(CITY_ID, pref.getString(CITY_ID, null));
        user.put(CITY_NAME, pref.getString(CITY_NAME, null));
        user.put(STATE_ID, pref.getString(STATE_ID, null));
        user.put(STATE_NAME, pref.getString(STATE_NAME, null));
        user.put(PROD_ID, pref.getString(PROD_ID, null));
        user.put(PROD_NAME, pref.getString(PROD_NAME, null));
        user.put(C_NAME, pref.getString(C_NAME, null));
        return user;
    }

    public <BusinessType> void setBTypeList(List<BusinessType> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        pref.edit().putString(BTYPELIST, json).apply();
    }

    public List<BusinessType> getList() {
        List<BusinessType> arrayItems;
        HashMap<String, String> user = new HashMap<String, String>();
        String serializedObject = pref.getString(BTYPELIST, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<BusinessType>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        } else
            return null;
    }

    public HashMap<String, String> getCustId() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(CUST_ID, pref.getString(CUST_ID, null));
        user.put(CUST_PASSWORD, pref.getString(CUST_PASSWORD, null));
        user.put(CUST_TYPE, pref.getString(CUST_TYPE, null));
        user.put(CUST_PHONE, pref.getString(CUST_PHONE, null));
        return user;
    }


    public void saveObjectToSharedPreference(String serializedObjectKey, Object object) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        pref1.edit().putString(serializedObjectKey, serializedObject).apply();
    }

    public <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        if (pref1.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref1.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public boolean isSave_isBannerImages() {
        return Save_isBannerImages;
    }

    public void setSave_isBannerImages(boolean save_isBannerImages) {
        Save_isBannerImages = save_isBannerImages;
    }
    public boolean isIsRegistered() {
        return IsRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        IsRegistered = isRegistered;
    }

    public boolean isSaveLogin() {
//        return Save_login;
        return preferences.getBoolean(SAVE_LOGIN, false);
    }

    public void setSaveLogin(boolean save_login) {
//        Save_login = save_login;
        preferences.edit().putBoolean(SAVE_LOGIN, save_login).apply();
    }

    public String getUserName(){
        return preferences.getString(USERNAME,"");
    }

    public void  saveUserName(String userName){
        preferences.edit().putString(USERNAME, userName).apply();
    }

    public String getPassword(){
        return preferences.getString(PASSWORD,"");
    }

    public void  savePassword(String password){
        preferences.edit().putString(PASSWORD, password).apply();
    }

    public static List<SlotBookImages> getImages() {
        return images;
    }


    public void setImages(List<SlotBookImages> images) {
        PrefManager.images = images;
    }

    public List<SlotBookImages> getTextAD() {
        return textAD;
    }

    public void setTextAD(List<SlotBookImages> textAD) {
        PrefManager.textAD = textAD;
    }

    public List<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<BusinessType> businessTypes) {
        PrefManager.businessTypes = businessTypes;
    }

    public void logout() {
        boolean biometricStatus = getBiometricStatus();
        boolean biometricAskedStatus = isBiometricRegistrationAsked();
        boolean isLoginSaved = isSaveLogin();
        String username = getUserName();
        String password = getPassword();
        pref.edit().remove(BTYPELIST)
                .remove(CITY_ID)
                .remove(CITY_NAME)
                .remove(STATE_ID)
                .remove(STATE_NAME)
                .remove(PROD_ID)
                .remove(PROD_NAME)
                .remove(PINCODE)
                .remove(C_NAME)
                .remove(CUST_ID)
                .remove(CUST_PASSWORD)
                .remove(CUST_TYPE)
                .remove(CUST_PHONE)
                .apply();
        pref.edit().clear().apply();
        pref1.edit().clear().apply();
        preferences.edit().putString(TOKEN, null).apply();
        preferences.edit()
                .remove(TOKEN)
                .remove(TandCVersion)
                .apply();
        preferences.edit().clear().apply();
        if (isLoginSaved){
            setSaveLogin(isLoginSaved);
            saveUserName(username);
            savePassword(password);
        }
        saveBiometricStatus(biometricStatus);
        setBiometricRegistrationAsked(biometricAskedStatus);
    }

    public void saveBiometricStatus(boolean status) {
        preferences.edit().putBoolean(BIOMETRIC_ENABLE, status).apply();
    }

    public boolean getBiometricStatus() {
        return preferences.getBoolean(BIOMETRIC_ENABLE,false);
    }

    public void setBiometricRegistrationAsked(boolean status) {
        preferences.edit().putBoolean(BIOMETRIC_REGISTRATION_ASKED, status).apply();
    }

    public boolean isBiometricRegistrationAsked() {
        return preferences.getBoolean(BIOMETRIC_REGISTRATION_ASKED,false);
    }

    public String getArticleResponse(){
        return preferences.getString(NEWS_ARTICLES,"");
    }

    public void  saveNewsArticle(String response){
        Log.d("Sonu", "saveNewsArticle:1 "+response);
        preferences.edit().putString(response,NEWS_ARTICLES).commit();
        Log.d("Sonu", "saveNewsArticle: "+getArticleResponse());
    }

}
