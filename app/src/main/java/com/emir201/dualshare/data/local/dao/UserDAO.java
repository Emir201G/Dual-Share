package com.emir201.dualshare.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.emir201.dualshare.data.local.entity.UserEntity;
@Dao
public interface UserDAO {
    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserEntity> getUser();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);
    @Update
    void update(UserEntity user);
    @Query("DELETE FROM users")
    void deleteUser();

}
