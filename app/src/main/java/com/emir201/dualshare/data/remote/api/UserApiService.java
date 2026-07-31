package com.emir201.dualshare.data.remote.api;

import com.emir201.dualshare.data.remote.dto.UpdateUsernameDTO;
import com.emir201.dualshare.data.remote.dto.UserResponseDTO;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserApiService {
    @PUT("/api/users/profile/update-username")
    Call<UserResponseDTO> updateUsername(@Body UpdateUsernameDTO dto);

    @Multipart
    @PUT("/api/users/profile/update-photo")
    Call<UserResponseDTO> updateProfilePhoto(
            @Part MultipartBody.Part storyFile
    );
    @GET("/api/users/profile/user")
    Call<UserResponseDTO> getUser();
}
