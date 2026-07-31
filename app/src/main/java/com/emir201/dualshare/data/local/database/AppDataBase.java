package com.emir201.dualshare.data.local.database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.emir201.dualshare.data.local.dao.UserDAO;
import com.emir201.dualshare.data.local.entity.UserEntity;

@Database(entities = {UserEntity.class},
        version = 1,
        exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {


    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);
    public abstract UserDAO userDao();

    private static volatile AppDataBase INSTANCE;

    public static AppDataBase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "dualshare_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

}
