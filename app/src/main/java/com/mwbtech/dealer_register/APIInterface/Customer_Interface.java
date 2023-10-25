package com.mwbtech.dealer_register.APIInterface;

import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.PojoClass.AdvertisementAreaModel;
import com.mwbtech.dealer_register.PojoClass.AdvertisementSlotModel;
import com.mwbtech.dealer_register.PojoClass.AdvertisementTypeModel;
import com.mwbtech.dealer_register.PojoClass.AvailableSlotModel;
import com.mwbtech.dealer_register.PojoClass.Brand;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.CityAD;
import com.mwbtech.dealer_register.PojoClass.CustomerModel;
import com.mwbtech.dealer_register.PojoClass.DashBoardData;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.DeleteInbox;
import com.mwbtech.dealer_register.PojoClass.District;
import com.mwbtech.dealer_register.PojoClass.ErrorBodyResponse;
import com.mwbtech.dealer_register.PojoClass.GetEnquiryConversationRequest;
import com.mwbtech.dealer_register.PojoClass.Holidays;
import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.PojoClass.LoginCustDetailsModel;
import com.mwbtech.dealer_register.PojoClass.Mailmodule;
import com.mwbtech.dealer_register.PojoClass.MainCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.Messages;
import com.mwbtech.dealer_register.PojoClass.Msg_Model;
import com.mwbtech.dealer_register.PojoClass.NewAdvertisementModule;
import com.mwbtech.dealer_register.PojoClass.Notification;
import com.mwbtech.dealer_register.PojoClass.OTPApprove;
import com.mwbtech.dealer_register.PojoClass.OTPValidation;
import com.mwbtech.dealer_register.PojoClass.OneToOneProductModel;
import com.mwbtech.dealer_register.PojoClass.OutboxDealer;
import com.mwbtech.dealer_register.PojoClass.PaymentToken;
import com.mwbtech.dealer_register.PojoClass.PostPayment;
import com.mwbtech.dealer_register.PojoClass.ProfessionalReqModel;
import com.mwbtech.dealer_register.PojoClass.PromoImage;
import com.mwbtech.dealer_register.PojoClass.QuestionAnswerObject;
import com.mwbtech.dealer_register.PojoClass.ReadUnreadModel;
import com.mwbtech.dealer_register.PojoClass.RegisterAccount;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealer;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerRequest;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerResponse;
import com.mwbtech.dealer_register.PojoClass.SendMail;
import com.mwbtech.dealer_register.PojoClass.SlotBookImages;
import com.mwbtech.dealer_register.PojoClass.SocialLoginRequest;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.PojoClass.StateCityResponse;
import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.SubmitQuery;
import com.mwbtech.dealer_register.PojoClass.TermsAndCondition;
import com.mwbtech.dealer_register.PojoClass.TokenCf;
import com.mwbtech.dealer_register.PojoClass.TokenObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Customer_Interface {
    //now working
    //String CREATION = "https://wbtechindia.com/apis/kalpavrikshaDevv2/";

    //live data
    //String CREATION = "https://wbtechindia.com/apis/kalpavrikshav1/";

    //test url latest
   //String CREATION = "https://wbtechindia.com/apis/KalpavrikshaDevV2/";
    // String CREATION = "http://jbnwebapi-dev.ap-south-1.elasticbeanstalk.com/";
    //String CREATION = "http://kalpavrikshawebapi.ap-south-1.elasticbeanstalk.com";
    //String CREION = "http://15.206.170.137/";
    //String CREATION = "http://103.190.95.241/";
    //public ip
    //String CREATION = "http://115.246.208.10/Kalpavriksha/";
    String CREATION = "http://192.168.1.213/Kalpavriksha/";
    //String CREATION = "http://115.245.104.170/Kalpavriksha/";
    //String CREATION = "http://115.246.208.10/Kalpavriksha/";

    //live data
    //String CREATION = "https://www.wbtechindia.com/apis/KalpavrikshaLive/";
    String Payment = "https://api.cashfree.com/api/";

    //new token
    @GET("api/Accounts/GenerateToken")
    Call<TokenObject> generateToken();

    @GET("api/Accounts/GetCustomerOfDeviceID/{DeviceID}")
    Call<AccountVerify> getDetails(@Header("Authorization") String token,
                                   @Path("DeviceID") String DeviceID);

    @GET("api/Accounts/GetQuestion")
    Call<QuestionAnswerObject> GetQuestions();

    @GET("api/Accounts/GetCustomerByMobileNumber/{MobileNumber}")
    Call<Boolean> GetuserDetails(@Path("MobileNumber") String MobileNumber);

    @POST("api/Accounts/AnswerQuestion/{DeviceID}/{MobileNumber}/{QuestionID}/{AnswerForQuestion}")
    Call<AccountVerify> SendAnswer(@Path("DeviceID") String DeviceID,
                                   @Path("MobileNumber") String MobileNumber,
                                   @Path("QuestionID") int QuestionID,
                                   @Path("AnswerForQuestion") String AnswerForQuestion);

    @POST("api/Accounts/RegisterUser")
    Call<DealerRegister> registerCustomerDetailsMethod(
            @Body RegisterAccount dealerRegister);

    @POST("api/Accounts/ResetPassword/{mobile}/{password}")
    Call<DealerRegister> changePassword(@Header("Authorization") String token,
                                        @Path("mobile") String mobile, @Path("password") String password);

    @POST("api/Accounts/ValidateCredentials/{MobileNo}/{Password}/{DeviceID}")
    Call<DealerRegister> loginCustomerDetailsMethod(@Header("Authorization") String token,
                                                    @Path("MobileNo") String mobileNumber,
                                                    @Path("Password") String password,
                                                    @Path("DeviceID") String DeviceID);

    /*
    @POST("api/Accounts/ValidateCredentials/{MobileNo}/{Password}")
    Call<DealerRegister> loginCustomerDetailsMethod(@Path("MobileNo") String MobileNo, @Path("Password") String password);
*/

    //Send OTP
    @POST("api/Accounts/SendOTP/{CustId}/{MobileNo}")
    Call<OTPValidation> sendOTPatLoginMethod(@Header("Authorization") String token,
                                             @Path("CustId") String mobileNumber,
                                             @Path("MobileNo") String password);

    //Update user type
    @POST("api/Accounts/UpdateUserCatogory/{CustID}/{UserCatogoryID}")
    Call<Boolean> updateUserType(@Header("Authorization") String token,
                                             @Path("CustID") String custId,
                                 @Path("UserCatogoryID") String userTypeId);

//    @POST("api/Accounts/SendOTP/{MobileNo}")
//    Call<OTPValidation> getOtp(@Path("MobileNo") String mobile);


    //Approve OTP
//    @POST("api/Accounts/ApproveOTP/{MobileNo}/{otp}")
    @POST("api/Accounts/ApproveOTP/{CustId}/{MobileNo}/{otp}")
    Call<OTPApprove> approveOTPatLoginMethod(@Header("Authorization") String token,
                                             @Path("CustId") String customerId, @Path("MobileNo") String mobileNumber, @Path("otp") String otpnumber);

    @POST("api/Accounts/ApproveOTPForgotPwd/{MobileNo}/{otp}")
    Call<OTPApprove> approveOTPatForgotMethod(@Path("MobileNo") String mobileNumber, @Path("otp") String otpnumber);

    @POST("/api/Accounts/RegisterUserSocialMedia")
    Call<DealerRegister> socialRegister(@Body SocialLoginRequest socialLoginRequest);

    @POST("api/Accounts/ValidateCredentialsSocialMedia/{Token}")
    Call<DealerRegister> socialLogin(@Path ("Token") String socialMediaToken);

    @GET("api/Accounts/GetCustomerDetails/{ID}")
    Call<DealerRegister> getCustomerDetails(@Header("Authorization") String token,
                                            @Path("ID") int custID);


    @GET("api/Product/GetPromoImages")
    Call<List<PromoImage>> getPromoAndTextAds(@Header("Authorization") String token);

    //for statictext ad
    @GET("api/Product/GetPromoTexts")
    Call<List<PromoImage>> getPromoText(@Header("Authorization") String token);

//    @POST("api/Product/ChildCategoryList/{SearchText}/{CustID}/{isProfessional}")
    @GET("api/Product/ChildCategoryList/{SearchText}/{CustID}/{isProfessional}")
    Call<List<SubCategoryProduct>> getSubCategoryChildCategoryList(@Header("Authorization") String token,
                                                                   @Path("SearchText") String SearchText,
                                                                   @Path("CustID") int CustID,
                                                                   @Path("isProfessional") boolean isProfessional);

    @GET("api/Product/GetGEProducts/{SearchText}")
    Call<List<ChildCategoryProduct>> getGESubCategoryChildCategoryList(@Header("Authorization") String token,
                                                                       @Path("SearchText") String SearchText);


//    @POST("api/Account/UpdateCustomerDetails")
//    Call<DealerRegister> updateDealerRegisterDetails(@Header("Authorization") String token,
//                                                     @Body DealerRegister dealerRegister);

    //to save customerdetails
//    @PUT("api/Accounts/PutCustomerDetails")
//    @PUT("api/Accounts/UpdateCustomerDetails")
    @POST("api/Accounts/UpdateCustomerDetails")
    Call<DealerRegister> updateDealerRegister(@Header("Authorization") String token,
                                              @Body DealerRegister dealerRegister);

    //to upload image
    @Multipart
    @POST("api/Accounts/UploadUserProfileImage")
    Call<ResponseBody> updatePhoto(@Header("Authorization") String token,
                                   @Part MultipartBody.Part file,
                                   @Part("customerId") RequestBody CustID);

    //to getBusinessTypes
    @GET("api/Product/BusinessTypeList")
    Call<List<BusinessType>> getBusinessTypes(@Header("Authorization") String token);

//    @POST("api/Product/StateList")
    @GET("api/Product/StateList")
    Call<List<State>> getAllStates(/*@Header("Authorization") String token*/);

//    @POST("api/Product/CityList/{StateId}/{CityName}")
    @GET("api/Product/CityList/{StateId}/{CityName}")
    Call<List<City>> getAllCity(/*@Header("Authorization") String token,*/
                                @Path("StateId") int StateId,
                                @Path("CityName") String CityName);

//    @POST("api/Product/ChildCategoryListForSearch/{SearchText}/{isProfessional}/{IsAdvertisement}")
    @GET("api/Product/ChildCategoryListForSearch/{SearchText}/{isProfessional}/{IsAdvertisement}")
    Call<List<ChildCategoryProduct>> getChildCategoryProduct(@Header("Authorization") String token,
                                                             @Path("SearchText") String SearchText,
                                                             @Path("isProfessional") boolean isProfessional,
                                                             @Path("IsAdvertisement") boolean IsAdvertisement);

    @POST("api/Product/SearchProductDealer")
    Call<SearchProductDealerResponse> searchProductDealerDetails(@Header("Authorization") String token,
                                                                       @Body SearchProductDealerRequest searchProductDealer);

    @POST("api/Product/SubmitCustomerQuery")
    Call<SubmitQuery> sendDealerEnquiry(@Header("Authorization") String token,
                                        @Body SubmitQuery list);

    @GET("http://www.postalpincode.in/api/pincode/{SearchText}")
    Call<Object> getAddress(@Path("SearchText") String pincode);

    @GET("api/Product/GetCityListFromPinCode/{pincode}")
    Call<List<StateCityResponse>> getStateCityFromPincode(@Path("pincode") String pincode);


    @Multipart
    @POST("api/Product/SubmitCustomerQueryWithFiles")
    Call<SubmitQuery> sendDealerEnquiryFile(@Header("Authorization") String token,
                                            @Part("submitQuery") SubmitQuery list,
                                            @Part MultipartBody.Part file1,
                                            @Part MultipartBody.Part file2);

    @GET("api/Product/GetAllCities")
    Call<List<City>> getCities(@Header("Authorization") String token);


    @GET("api/Product/GetCustomerQueries/{CustID}/{IsFavorite}")
    Call<List<InboxDealer>> getInboxDealer(@Header("Authorization") String token,
                                           @Path("CustID") int CustID,
                                           @Path("IsFavorite") int IsFavorite);

    @POST("api/Product/DeleteConversation/{CustID}")
    Call<List<DeleteInbox>> deleteConversion(@Header("Authorization") String token,
                                             @Path("CustID") int CustID,
                                             @Body List<DeleteInbox> inboxDealers);

    @POST("api/Product/PostMessageReadUnread")
    Call<List<ReadUnreadModel>> markChatAsReadUnread(@Header("Authorization") String token,
                                                     @Body List<ReadUnreadModel> readUnreadModels);

    @POST("api/Product/DeleteChat/{CustID}")
    Call<DeleteInbox> deleteChat(@Header("Authorization") String token,
                                 @Path("CustID") int CustID,
                                 @Body List<DeleteInbox> inboxDealers);


    @POST("api/Product/FilterCustomerQueries")
    Call<List<InboxDealer>> getFilterList(@Header("Authorization") String token,
                                          @Body SearchProductDealer searchProductDealer);

    @POST("api/Product/AddOrDeleteFavorite/{CustID}/{QueryId}/{ReceiverID}/{IsFavorite}")
    Call<Void> postFavouriteData(@Header("Authorization") String token,
                                              @Path("CustID") int CustID,
                                              @Path("QueryId") int QueryId,
                                              @Path("ReceiverID") int ReceiverID,
                                              @Path("IsFavorite") int IsFavorite);


    @GET("api/Product/GetCityOfDealer/{CustId}")
    Call<List<City>> getDealerCities(@Header("Authorization") String token,
                                     @Path("CustId") int CustId);

    @GET("api/Product/GetFavoriteCities/{CustId}")
    Call<List<City>> GetFavoriteChatCities(@Header("Authorization") String token,
                                           @Path("CustId") int CustId);

    @GET("api/Product/GetCityOfSentEnquiries/{CustId}")
    Call<List<City>> getOutboxDealerCities(@Header("Authorization") String token,
                                           @Path("CustId") int CustId);


    @GET("api/Product/GetMessages/{CustID}/{QueryId}/{ReceiverID}")
    Call<Msg_Model> getMessages(@Header("Authorization") String token,
                                @Path("CustID") int CustID,
                                @Path("QueryId") int QueryId,
                                @Path("ReceiverID") int ReceiverID);


    @Multipart
    @POST("api/Product/SendMessage")
    Call<Messages> SendMessage(@Header("Authorization") String token,
                               @Part("submitQuery") Messages messages,
                               @Part MultipartBody.Part file);


    @POST("api/Product/GetEnquiries")
    Call<List<OutboxDealer>> GetEnquiryDealerList(@Header("Authorization") String token,
                                                  @Body SearchProductDealer searchProductDealer);


    @POST("api/Product/GetConversationsOfEnquiry")
    Call<List<InboxDealer>> GetOutBoxListDealers(@Header("Authorization") String token,
                                                 @Body GetEnquiryConversationRequest request);

/*
    @GET("api/Product/GetConversationsOfEnquiry/{QueryID}/{CustID}/{ReceiverID}/{IsFavorite}/{SelectedCities}")
    Call<List<InboxDealer>> GetOutBoxListDealers(@Header("Authorization") String token,
                                                 @Path("QueryID") int QueryID,
                                                 @Path("CustID") int CustID,
                                                 @Path("ReceiverID") int ReceiverID,
                                                 @Path("IsFavorite") int IsFavorite,
                                                 @Path("SelectedCities") String selectedCities);

*/
    /*@GET("api/Product/GetConversationsOfEnquiry")
    Call<List<InboxDealer>> GetOutBoxListDealers(@Header("Authorization") String token,
                                                 @Query("QueryID") int QueryID,
                                                 @Query("CustID") int CustID,
                                                 @Query("ReceiverID") int ReceiverID,
                                                 @Query("IsFavorite") int IsFavorite,
                                                 @Query("SelectedCities") String selectedCities);*/


    @GET("api/Product/GetCustomerNamesForFilter/{CustID}")
    Call<List<InboxDealer>> GetUsersFirmName(@Header("Authorization") String token,
                                             @Path("CustID") int CustID);

    @POST("api/Product/FilterCustomerSentEnquiries")
    Call<List<InboxDealer>> GetSentEnquiryFilterData(@Header("Authorization") String token,
                                                     @Body SearchProductDealer searchProductDealer);

    @POST("api/Product/GetFavoriteConversations")
    Call<List<InboxDealer>> GetFavChatFilterList(@Header("Authorization") String token,
                                                 @Body SearchProductDealer searchProductDealer);

    @POST("api/Product/DeleteEnquiry/{CustID}")
    Call<List<DeleteInbox>> DeleteEnquiry(@Header("Authorization") String token,
                                          @Path("CustID") int CustID,
                                          @Body List<DeleteInbox> inboxDealers);


    @POST("api/Product/SendMail")
    Call<Mailmodule> SendEmail(@Header("Authorization") String token,
                               @Body SendMail sendMail);

    @Multipart
    @POST("api/Product/SendMailWithFile/{IsProductRequest}")
    Call<Mailmodule> SendEmailWithFile(@Header("Authorization") String token,
                                       @Path("IsProductRequest") Boolean IsProductRequest,
                                       @Part("sendMailParameter") SendMail sendMail,
                                       @Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2);


    @GET("api/Product/GetAllSubCategoryList")
    Call<List<SubCategoryProduct>> GetAllSubCategory(@Header("Authorization") String token);

    @GET("api/Product/GetChildCatagories/{SubCategoryID}")
    Call<List<ChildCategoryProduct>> GetAllChildCatName(@Header("Authorization") String token,
                                                        @Path("SubCategoryID") int SubCategoryID);

    @GET("api/Product/GetBusinessDemands")
    Call<List<BusinessDemand>> GetBusinessDemand(@Header("Authorization") String token);

    @GET("api/Product/GetProfessionalRequirements")
    Call<List<ProfessionalReqModel>> GetProfessionalsReq(@Header("Authorization") String token);

    @GET("api/Product/GetDashboardData/{CustID}")
    Call<DashBoardData> GetDashBoardData(@Header("Authorization") String token,
                                         @Path("CustID") int CustID);

    @GET("api/Advertisement/GetAdvertisementAreas")
    Call<List<AdvertisementAreaModel>> GetAdAreas(@Header("Authorization") String token);

    @GET("api/Advertisement/GetAdvertisementTypes")
    Call<List<AdvertisementTypeModel>> GetAdType(@Header("Authorization") String token);

    @GET("api/Advertisement/GetAdTimeSlots")
    Call<List<AdvertisementSlotModel>> GetTimeSlot(@Header("Authorization") String token);

    //delete ad
    @GET("api/Advertisement/CancelAdvertisement/{AdvertisementMainID}")
    Call<Mailmodule> DeleteAdvertisement(@Header("Authorization") String token,
                                         @Path("AdvertisementMainID") int adID);

    @POST("api/Advertisement/GetDistricts/{DistrictName}")
    Call<List<District>> GetDistrict(@Header("Authorization") String token,
                                     @Path("DistrictName") String DistrictName,
                                     @Body List<State> stateList);

    @POST("api/Advertisement/GetStateWiseCities/{CityName}")
    Call<List<City>> GetCities(@Header("Authorization") String token,
                               @Path("CityName") String CityName,
                               @Body List<State> stateList);

    @POST("api/Advertisement/GetStateWiseCities/{CityName}")
    Call<List<CityAD>> GetCitiesAD(@Header("Authorization") String token,
                                   @Path("CityName") String CityName,
                                   @Body List<State> stateList);

    @GET("api/Product/GetItemNamesForUser/{CustID}")
    Call<List<OneToOneProductModel>> GetItemList(@Header("Authorization") String token,
                                                 @Path("CustID") int CustID);

    @GET("api/Product/GetBusinessTypesForItem/{ItemID}")
    Call<List<BusinessType>> GetBusinessType(@Header("Authorization") String token,
                                             @Path("ItemID") int ItemID);

    @GET("api/Product/GetStatesListForUser/{CustID}")
    Call<List<State>> GetStateList(@Header("Authorization") String token,
                                   @Path("CustID") int CustID);

    @GET("api/Product/GetCitiesListForUser/{CustID}/{StateID}")
    Call<List<City>> GetCityList(@Header("Authorization") String token,
                                 @Path("CustID") int CustID,
                                 @Path("StateID") int StateID);

    @POST("api/Product/GetReplierList")
    Call<List<CustomerModel>> GetCustomerList(@Header("Authorization") String token,
                                              @Body CustomerModel customerModel);

    @POST("api/Product/FilterEnquiryList")
    Call<List<CustomerModel>> FilterEnquiryList(@Header("Authorization") String token,
                                                @Body CustomerModel customerModel);

    @POST("api/Product/GetLoadEnquiryStatelistOnePlus")
    Call<List<State>> GetLoadEnquiryStatelistOnePlus(@Header("Authorization") String token,
                                                     @Body CustomerModel customerModel);

    @POST("api/Product/GetBusinessTypelistFor_OnePlus")
    Call<List<BusinessType>> GetBusinessTypelistFor_OnePlus(@Header("Authorization") String token,
                                                            @Body CustomerModel customerModel);

    //GetLoadEnquiry_CitylistOnePlus
    @POST("api/Product/GetLoadEnquiry_CitylistOnePlus")
    Call<List<City>> GetLoadEnquiry_CitylistOnePlus(@Header("Authorization") String token,
                                                    @Body CustomerModel customerModel);

    @GET("api/Advertisement/GetBrands")
    Call<List<Brand>> GetBrands(@Header("Authorization") String token);

    @POST("api/Advertisement/CheckSlotAvailability")
    Call<AvailableSlotModel> GetAvailableSlots(@Header("Authorization") String token,
                                               @Body AvailableSlotModel availableSlotModel);


    //new method to call bookAD
    //@POST("api/Advertisement/BookAdvertisementSlots")
    @POST("api/Advertisement/BookBannerAdvertisements")
    Call<NewAdvertisementModule> GetAvailableSlotsNew(@Header("Authorization") String token,
                                                      @Body NewAdvertisementModule availableSlotModel);

    @POST("api/Advertisement/BookFullPageAdvertisements")
    Call<NewAdvertisementModule> FullPageAdSlots(@Header("Authorization") String token,
                                                 @Body NewAdvertisementModule availableSlotModel);


    @GET("api/Advertisement/CreateInvoice/{AdvertisementMainID}")
    Call<String> GetInvoice(@Header("Authorization") String token,
                            @Path("AdvertisementMainID") int AdvertisementMainID);


    @GET("api/Advertisement/GetAdvertisementsOfAnUser/{CustID}")
    Call<List<AdDetailsModel>> GetUserAds(@Header("Authorization") String token,
                                          @Path("CustID") int CustID);

    @GET("api/Advertisement/GetAdvertisementDetails/{AdvertisementMainID}")
    Call<AvailableSlotModel> GetAdDetails(@Header("Authorization") String token,
                                          @Path("AdvertisementMainID") int AdvertisementMainID);

    @GET("api/Advertisement/GetReceivedAdvertisements/{CustID}")
    Call<List<AdDetailsModel>> GetReceivedAds(@Header("Authorization") String token,
                                              @Path("CustID") int CustID);

    @GET("api/Advertisement/GetSentAdvertisementsOfAnUser/{CustID}")
    Call<List<AdDetailsModel>> GetSentAds(@Header("Authorization") String token,
                                          @Path("CustID") int CustID);

    @Multipart
    @POST("api/Advertisements/UploadAdImage")
    Call<AvailableSlotModel> upLoadAdImage(@Header("Authorization") String token,
                                           @Part("jsonString") AvailableSlotModel slotModel,
                                           @Part MultipartBody.Part file);

    @Multipart
    @POST("api/Advertisement/UploadAdImage")
    Call<NewAdvertisementModule> upLoadAd(@Header("Authorization") String token,
                                          @Part MultipartBody.Part file,
                                          @Part("advertisementMainId") RequestBody advertisementMainId);

    /* @Part MultipartBody.Part file,
     @Part("customerId") RequestBody CustID);
 */
    @POST("api/Advertisement/PostAdText")
    Call<AvailableSlotModel> SaveText(@Header("Authorization") String token,
                                      @Body AvailableSlotModel availableSlotModel);

    @GET("api/Advertisement/GetBannerImages/{CustID}")
    Call<List<AdDetailsModel>> GetBannerImages(@Header("Authorization") String token,
                                               @Path("CustID") int CustID);

    //get BannerImages
    @GET("api/Advertisement/GetAdvertisementSlots/{CustID}/{isBanner}")
    Call<List<SlotBookImages>> GetBannerText(@Header("Authorization") String token,
                                             @Path("CustID") int CustID,
                                             @Path("isBanner") boolean isBanner);

    //for textad
    @GET("api/Advertisement/GetTextAdvertisements/{CustID}")
    Call<List<AdDetailsModel>> GetTextAd(@Header("Authorization") String token,
                                         @Path("CustID") int CustID);

    //123/981037
    // @GET("api/Accounts/ChangePassword/{MobileNo}/{Password}")
    @POST("api/Accounts/ChangePassword/{MobileNo}/{OldPassword}/{NewPassowrd}")
    Call<OTPValidation> ChangePassword(@Header("Authorization") String token,
                                       @Path("MobileNo") String MobileNo, @Path("OldPassword") String OldPassword,
                                       @Path("NewPassowrd") String NewPassowrd);

    @POST("api/Accounts/ValidateUserForgotPwd/{MobileNo}")
    Call<OTPValidation> ValidateUserForgotPwd(@Header("Authorization") String token,
                                              @Path("MobileNo") String MobileNo);

    @POST("api/Accounts/ValidateUserForgotPwd/{MobileNo}")
    Call<ErrorBodyResponse> validateUser(@Path("MobileNo") String MobileNo);

    @POST("api/Accounts/ForgotRest_Password/{MobileNo}/{Password}/{SMSOTP}")
    Call<OTPValidation> ForgotRest_Password(@Header("Authorization") String token,
                                            @Path("MobileNo") String MobileNo, @Path("Password") String Password,
                                            @Path("SMSOTP") String SMSOTP);

    @POST("api/Accounts/SendOTPToMobileNumber/{DeviceID}/{MobileNumber}")
    Call<AccountVerify> GetOTP(@Path("DeviceID") String DeviceID, @Path("MobileNumber") String MobileNumber);

    @POST("api/Accounts/ResendSendOTPToMobileNumber/{DeviceID}/{MobileNumber}")
    Call<AccountVerify> ResendOTP(@Path("DeviceID") String DeviceID, @Path("MobileNumber") String MobileNumber);

    @POST("api/Accounts/ApproveOTP/{CustID}/{MobileNumber}/{OTP}")
    Call<AccountVerify> ValidateOTP(@Path("CustID") int customerId, @Path("MobileNumber") String mobileNumber, @Path("OTP") String otpnumber);


    @POST("api/SaveCustomerDetails")
    Call<DealerRegister> postCustomerCreationMethod(@Body DealerRegister dealerRegister);


    @POST("api/Product/SubCategoryList/")
    Call<List<SubCategoryProduct>> getSubCategoryProduct(@Body List<ChildCategoryProduct> list);

    @POST("api/Product/CategoryList/")
    Call<List<MainCategoryProduct>> getMainCategoryProduct(@Body List<SubCategoryProduct> list);

    @GET("api/Accounts/GetDistricts/{StateID}")
    Call<List<District>> getDistrictList(@Path("StateID") int StateID);

    @GET("api/Advertisement/GetStateByName/{StateName}")
    Call<State> getStateByName(@Path("StateName") String StateName);

    @GET("api/Advertisement/GetDistrictByName/{DistrictName}")
    Call<District> getDistrictByName(@Path("DistrictName") String DistrictName);

    @GET("/api/Advertisement/GetCityByName/{CityName}")
    Call<City> getCityByName(@Path("CityName") String CityName);

    @POST("api/Accounts/BlockMobileNumber/{MobileNumber}")
    Call<String> SendMobNumToBlock(@Path("MobileNumber") String mobileNumber);

    @POST("api/Accounts/CheckStatus/{MobileNumber}")
    Call<String> CheckForNumBlock(@Path("MobileNumber") String MobileNumber);

    @GET("api/Accounts/GetCustomerByMobileNumber_New/{MobileNumber}")
    Call<LoginCustDetailsModel> GetCustomerByMobileNumber(@Path("MobileNumber") String MobileNumber);

    //payment api
    @POST("v2/cftoken/order")
    Call<TokenCf> generateToken(@Header("x-client-id") String AppId,
                                @Header("x-client-secret") String Secretkey,
                                @Body PaymentToken paymentToken);

    //post payment
    @POST("api/Advertisement/PostAdPayments")
    Call<PostPayment> postAdPayments(@Header("Authorization") String token,
                                     @Body PostPayment postPayment);

    //Tier1 city
    @GET("api/product/GetTierOneCities")
    Call<List<CityAD>> getTierCities(@Header("Authorization") String token);

    //Notification
    @GET("api/Product/GetPushNotifications/{CustID}")
    Call<List<Notification>> GetUserNotification(@Header("Authorization") String token,
                                                 @Path("CustID") int CustID);

    @POST("api/Product/DeleteAllNotifications/{CustID}")
    Call<Object> deleteAllNotifications(@Header("Authorization") String token, @Path("CustID") int CustID, @Body List<Notification> notification);

    @POST("api/Product/DeleteNotifications/{CustID}")
    Call<List<Notification>> deleteNotification(@Header("Authorization") String token, @Path("CustID") int CustID, @Body List<Notification> notification);

    //agreement
    @GET("api/Accounts/GetTermsAndConditions")
    Call<TermsAndCondition> getAgreement(@Header("Authorization") String token);

    @GET("api/Advertisement/GetHolidaysCount/{currentDate}")
    Call<Holidays> getHolidays(@Header("Authorization") String token,
                               @Path("currentDate") String currentDate);

    @GET("api/Advertisement/GetCustomerBusinessTypes/{CustID}")
    Call<List<BusinessType>> GetBusinessTypesofCustomer(@Header("Authorization") String token,
                                                        @Path("CustID") int CustID);

}
