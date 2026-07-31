package com.emir201.dualshare.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String username;

    private String email;

    private String shareCode;

    private String photoUrl;

    private String role;

    private boolean enabled;
}
