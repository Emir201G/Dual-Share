package com.emir201.dualshare.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.emir201.dualshare.data.local.entity.UserEntity;
import com.emir201.dualshare.data.repository.UserRepository;

import lombok.Getter;

@Getter
public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final LiveData<UserEntity> currentUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = new UserRepository(application);
        this.currentUser = userRepository.getCurrentUser();
    }

    public void refreshUser() {
        userRepository.refreshUser();
    }

    public void saveUser(UserEntity user) {
        userRepository.saveUser(user);
    }

    public void updateUser(UserEntity user) {
        userRepository.updateUser(user);
    }

    public void logout() {
        userRepository.logout();
    }
    public void updateUsername(String username) {
        Log.d("VIEWMODEL", "updateUsername: " + username);
        userRepository.updateUsername(username);
    }
}
