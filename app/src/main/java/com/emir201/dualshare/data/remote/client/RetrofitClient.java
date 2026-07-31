package com.emir201.dualshare.data.remote.client;

import android.content.Context;

import com.emir201.dualshare.data.remote.api.AuthApiService;
import com.emir201.dualshare.data.remote.api.UserApiService;
import com.emir201.dualshare.data.local.prefs.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://dualshare-production.up.railway.app/";
    private static Retrofit retrofit = null;
    private static AuthApiService authApiService = null;
    private static UserApiService userApiService = null;

    public static synchronized void init(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();

                        SessionManager sessionManager = new SessionManager(context.getApplicationContext());
                        String token = sessionManager.getToken();

                        if (token != null && !token.isEmpty()) {
                            Request authenticatedRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .build();
                            return chain.proceed(authenticatedRequest);
                        }

                        return chain.proceed(originalRequest);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static UserApiService getUserApiService() {
        if (userApiService == null && retrofit != null) {
            userApiService = retrofit.create(UserApiService.class);
        }
        return userApiService;
    }

    public static AuthApiService getAuthApiService() {
        if (authApiService == null && retrofit != null) {
            authApiService = retrofit.create(AuthApiService.class);
        }
        return authApiService;
    }
}