package network;


import model.LoginRequest;
import model.LoginResponse;
import model.LogoutResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login/") // Django 后端登录接口路径
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("register/") // Django 后端注册接口路径
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
    @POST("logout/") // 确保 URL 与后端一致
    Call<LogoutResponse> logout();
}
