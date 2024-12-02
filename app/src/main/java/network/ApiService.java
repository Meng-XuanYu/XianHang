package network;

import java.util.List;

import model.*;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login/") // Django 后端登录接口路径
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("register/") // Django 后端注册接口路径
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
    @POST("logout/") // 确保 URL 与后端一致
    Call<LogoutResponse> logout();
    @GET("get/profile/")
    Call<GetProfileResponse> getProfile(@Query("userId") String userId);
    @GET("get/money/")
    Call<GetMoneyResponse> getMoney(@Query("userId") String userId);
    @GET("get/attractiveness/")
    Call<GetAttractivenessResponse> getAttractiveness(@Query("userId") String userId);
    @POST("update/name/")
    Call<UpdateNameResponse> updateProfile(@Body UpdateNameRequest request);
    @POST("update/studentId/")
    Call<UpdateStudentIdResponse> updateProfile(@Body UpdateStudentIdRequest request);
    @POST("update/gender/")
    Call<UpdateGenderResponse> updateProfile(@Body UpdateGenderRequest request);
    @POST("update/text/")
    Call<UpdateTextResponse> updateProfile(@Body UpdateTextRequest request);
    @POST("update/school/")
    Call<UpdateSchoolResponse> updateProfile(@Body UpdateSchoolRequest request);
    @POST("update/identity/")
    Call<UpdateIdentityResponse> updateProfile(@Body UpdateIdentityRequest request);
    @POST("update/phone/")
    Call<UpdatePhoneResponse> updateProfile(@Body UpdatePhoneRequest request);
    @POST("forget_password/")
    Call<ForgetPasswordResponse> forgetPassword(@Body ForgetPasswordRequest request);
    // ApiService.java
    @Multipart
    @POST("update/image/")
    Call<UpdateImageResponse> uploadFile(@Part("userId") RequestBody userId, @Part MultipartBody.Part file);
    @POST("charge/")
    Call<ChargeResponse> charge(@Body ChargeRequest request);
    @POST("add_history/")
    Call<AddHistoryResponse> addHistory(@Body AddHistoryRequest request);
    @GET("get_history/")
    Call<GetHistoryResponse> getHistory(@Query("userId") String userId);
    @POST("clear_history/")
    Call<ClearHistoryResponse> clearHistory(@Body ClearHistoryRequest request);
    @POST("add_collection/")
    Call<AddCollectionResponse> addCollection(@Body AddCollectionRequest request);
    @GET("get_collections/")
    Call<GetCollectionsResponse> getCollections(@Query("userId") String userId);
    @POST("isCollected/")
    Call<IsCollectedResponse> isCollected(@Body IsCollectedRequest request);
    @POST("delete_collection/")
    Call<DeleteCollectionResponse> deleteCollected(@Body DeleteCollectionRequest request);
    @GET("clear_collection/")
    Call<ClearCollectionResponse> clearHistory(@Body ClearCollectionRequest request);
    @GET("searchCommodity/")
    Call<SearchCommodityResponse> searchCommodity(@Query("q") String query);
    @GET("getCommodity/")
    Call<GetCommodityResponse> getCommodity(@Query("commodityId") String commodityId);
    @Multipart
    @POST("publishCommodity/")
    Call<PublishCommodityResponse> publishCommodity(
            @Part("userId") RequestBody userId,
            @Part("commodityName") RequestBody commodityName,
            @Part("commodityDescription") RequestBody commodityDescription,
            @Part("commodityValue") RequestBody commodityValue,
            @Part("commodityKeywords") RequestBody commodityKeywords,
            @Part("commodityClass") RequestBody commodityClass,
            @Part MultipartBody.Part commodityImage1,
            @Part List<MultipartBody.Part> additionalImages
    );
    @POST("buyCommodity/")
    Call<BuyCommodityResponse> buyCommodity(@Body BuyCommodityRequest request);
    @POST("cancelTrade/")
    Call<CancelTradeResponse> cancelTrade(@Body CancelTradeRequest request);
    @POST("confirmSend/")
    Call<ConfirmSendResponse> confirmSend(@Body ConfirmSendRequest request);
    @POST("returnCommodity/")
    Call<ReturnCommodityResponse> returnCommodity(@Body ReturnCommodityRequest request);
    @POST("confirmReceive/")
    Call<ConfirmReceiveResponse> confirmReceive(@Body ConfirmReceiveRequest request);
    @POST("ai_search/")
    Call<AISearchResponse> aiSearch(@Body AISearchRequest requestBody);
    @POST("get/ai_recommend/")
    Call<AIRecommendResponse> aiRecommend(@Body AIRecommendRequest requestBody);
    @POST("ai_extend/")
    Call<AIExtendResponse> aiExtend(@Body AIExtendRequest requestBody);
    @Multipart
    @POST("ai_publish/")
    Call<AIPublishResponse> aiPublish(@Part MultipartBody.Part image);

    @GET("getMyPublish/")
    Call<GetMyPublishResponse> getMyPublish(@Query("userId") String userId);
    @GET("getMyBuy/")
    Call<GetMyBuyResponse> getMyBuy(@Query("userId") String userId);
    @GET("getMySell/")
    Call<GetMySellResponse> getMySell(@Query("userId") String userId);
    @GET("getNeedComment/")
    Call<GetNeedCommentResponse> getNeedComment(@Query("userId") String userId);
    @GET("getSingleTrade/")
    Call<GetSingleTradeResponse> getSingleTrade(@Query("tradeId") String tradeId);


    @POST("commentTrade/")
    Call<CommentTradeResponse> commentTrade(@Body CommentTradeRequest commentTradeRequest);
    @GET("getTradeComment/")
    Call<GetTradeCommentResponse> getTradeComment(@Query("tradeId") String tradeId);
    @POST("addCommodityComment/")
    Call<AddCommodityCommentResponse> addCommodityComment(@Body AddCommodityCommentRequest commentTradeRequest);
    @GET("getCommodityComment/")
    Call<GetCommodityCommentResponse> getCommodityComment(@Query("commodityId") String commodityId);

    @GET("getCommodityListByClass/")
    Call<GetCommodityListByClassResponse> getCommodityListByClass(@Query("commodityClass") String commodityClass);

    @POST("setRead/")
    Call<SetReadResponse> setRead(@Body SetReadRequest request);

    @GET("getCommodityListByUserSchool/")
    Call<GetCommodityListByUserSchoolResponse> getCommodityListByUserSchool(@Query("commoditySchool") String commoditySchool);
    @POST("getChatList/")
    Call<GetChatListResponse> getChatList(@Body GetChatListRequest request);
    @POST("getOrCreateChat/")
    Call<GetOrCreateChatResponse> getOrCreateChat(@Body GetOrCreateChatRequest request);
    @POST("deleteChat/")
    Call<DeleteChatResponse> deleteChat(@Body DeleteChatRequest request);
}
