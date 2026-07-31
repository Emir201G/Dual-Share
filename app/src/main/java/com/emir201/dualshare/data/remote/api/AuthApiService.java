package com.emir201.dualshare.data.remote.api;

import com.emir201.dualshare.data.remote.dto.TokenRequestDTO;
import com.emir201.dualshare.data.remote.dto.UserResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("api/auth/verify")
    Call<UserResponseDTO> verifyAuthenticate(@Body TokenRequestDTO tokenRequest);


}