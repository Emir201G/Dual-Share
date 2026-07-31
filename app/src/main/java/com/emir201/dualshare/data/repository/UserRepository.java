package com.emir201.dualshare.data.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.emir201.dualshare.data.local.dao.UserDAO;
import com.emir201.dualshare.data.local.database.AppDataBase;
import com.emir201.dualshare.data.local.entity.UserEntity;
import com.emir201.dualshare.data.remote.api.UserApiService;
import com.emir201.dualshare.data.remote.client.RetrofitClient;
import com.emir201.dualshare.data.remote.dto.UpdateUsernameDTO;
import com.emir201.dualshare.data.remote.dto.UserResponseDTO;
import com.emir201.dualshare.mapper.UserMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {


    private final UserDAO userDao;
    private final UserApiService api;
    private final Context context;


    public UserRepository(Context context) {

        this.context = context.getApplicationContext();


        AppDataBase database =
                AppDataBase.getInstance(this.context);


        this.userDao = database.userDao();


        this.api = RetrofitClient.getUserApiService();
    }


    public LiveData<UserEntity> getCurrentUser() {

        return userDao.getUser();
    }


    public void saveUser(UserEntity user) {

        AppDataBase.databaseWriteExecutor.execute(() ->
                userDao.insert(user));
    }


    public void updateUser(UserEntity user) {

        AppDataBase.databaseWriteExecutor.execute(() ->
                userDao.update(user));
    }


    public void logout() {

        AppDataBase.databaseWriteExecutor.execute(
                userDao::deleteUser
        );
    }


    public void refreshUser() {
        api.getUser().enqueue(new Callback<UserResponseDTO>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserResponseDTO> call,
                    @NonNull Response<UserResponseDTO> response) {
                if (response.isSuccessful()
                        && response.body() != null) {

                    UserEntity user = UserMapper.toEntity(response.body());
                    saveUser(user);
                }

            }

            @Override
            public void onFailure(
                    @NonNull Call<UserResponseDTO> call,
                    @NonNull Throwable t) {
            }
        });
    }


    public void updateUsername(String username) {
        api.updateUsername(new UpdateUsernameDTO(username)).enqueue(new Callback<UserResponseDTO>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserResponseDTO> call,
                    @NonNull Response<UserResponseDTO> response) {

                if (response.isSuccessful()
                        && response.body() != null) {
                    UserEntity user =
                            UserMapper.toEntity(response.body());
                    updateUser(user);

                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<UserResponseDTO> call,
                    @NonNull Throwable t) {
            }
        });
    }


    public void updateProfilePhoto(Uri uri) {
        try {
            File file = getFileFromUri(uri);
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            file
                    );

            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "storyFile",
                    file.getName(),
                    requestFile
            );
            api.updateProfilePhoto(body).enqueue(new Callback<UserResponseDTO>() {
                @Override
                public void onResponse(
                        @NonNull Call<UserResponseDTO> call,
                        @NonNull Response<UserResponseDTO> response) {
                    if (response.isSuccessful()
                            && response.body() != null) {

                        UserEntity user =
                                UserMapper.toEntity(
                                        response.body()
                                );
                        updateUser(user);
                    }

                }

                @Override
                public void onFailure(
                        @NonNull Call<UserResponseDTO> call,
                        @NonNull Throwable t) {
                }
            });
        } catch (
                Exception e) {
            Log.e("PHOTO",
                    "Error preparando imagen",
                    e);
        }

    }

    private File getFileFromUri(Uri uri) throws Exception {
        File file = File.createTempFile(
                "profile_",
                ".jpg",
                context.getCacheDir()
        );
        try (InputStream inputStream =
                     context.getContentResolver()
                             .openInputStream(uri);
             OutputStream outputStream =
                     new FileOutputStream(file)
        ) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(
                        buffer,
                        0,
                        length
                );
            }

        }
        return file;
    }

}