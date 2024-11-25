package network;

import model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @POST("update_profile/name/")
    Call<UpdateNameResponse> updateProfile(@Body UpdateNameRequest request);
    @POST("update_profile/gender/")
    Call<UpdateGenderResponse> updateProfile(@Body UpdateGenderRequest request);
    @POST("update_profile/text/")
    Call<UpdateTextResponse> updateProfile(@Body UpdateTextRequest request);
    @POST("update_profile/school/")
    Call<UpdateSchoolResponse> updateProfile(@Body UpdateSchoolRequest request);
    @POST("update_profile/identity/")
    Call<UpdateIdentityResponse> updateProfile(@Body UpdateIdentityRequest request);
    @POST("update_profile/phone/")
    Call<UpdatePhoneResponse> updateProfile(@Body UpdatePhoneRequest request);
    @POST("forget_password/")
    Call<ForgetPasswordResponse> forgetPassword(@Body ForgetPasswordRequest request);
}
