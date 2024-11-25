package RetrofitClient;

import network.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.192.204.100:8000/"; // 替换为后端地址
//    private static final String BASE_URL = "http://10.0.2.2:8000/"; // 替换为后端地址
    private static Retrofit retrofit;

    // 单例模式获取 Retrofit 实例
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // 提供 ApiService 实例
    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
